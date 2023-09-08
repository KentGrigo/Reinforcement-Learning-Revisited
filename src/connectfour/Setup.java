package connectfour;

import auxiliaries.*;
import static connectfour.Settings.*;
import game.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;
import javax.swing.JFrame;
import neuralnetworks.*;
import org.jfree.ui.RefineryUtilities;
import reinforcementlearning.*;

public class Setup {

    private static GamePanel gp;
    private static PlotPanel pp;
    private static SettingsPanel sp;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - HH.mm.ss");

    public static void main(String[] args) {
        List<Parameters> combinationOfParameters = Parameters.getCombinations(POSSIBLE_LEARNER_LAYER_SIZES, POSSIBLE_MOVE_SELECTOR_RATES, POSSIBLE_MOVE_SELECTOR_RATE_DECAY_FACTORS, POSSIBLE_LEARNING_RATES, POSSIBLE_LEARNING_RATE_DECAY_FACTORS, POSSIBLE_DECAY_RATES, POSSIBLE_ETAS, POSSIBLE_WEIGHT_DECAYS, POSSIBLE_LAMBDAS, POSSIBLE_LEARNER_BOARD_ENCODERS);

        if (IS_GRID_SEARCH && SAVE_NETWORKS) {
            System.out.println("Grid search with tournament.");
            combinationOfParameters = setupGridSearch(combinationOfParameters);
            System.out.println("Grid search finished.");
            System.out.println("Starting tournament.");
            PrintUtil.printSettings();
            setupTournament(combinationOfParameters);
            System.out.println("Tournament finished.");
//            PrintUtil.writeParametersToFile(combinationOfParameters, "grid_search");
        } else if (IS_GRID_SEARCH) {
            System.out.println("Grid search with random iterations.");
            combinationOfParameters = runGridSearch(combinationOfParameters);
            System.out.println("Grid search finished.");
            PrintUtil.printSettings();
            PrintUtil.writeParametersToFile(combinationOfParameters, "grid_search");
        } else {
            System.out.println("Training.");
            Game game = new ConnectFourGame(BOARD_ROWS, BOARD_COLUMNS);
            setupGraphPanel();
//          setupSettings();
//          setupGamePanel(game);

            Parameters p1Parameters = combinationOfParameters.get(0);
            PlayerSettings opponentSettings = new PlayerSettings(OPPONENT_PLAYER, OPPONENT_BOARD_ENCODER, OPPONENT_MOVE_SELECTOR, OPPONENT_SEARCH_METHOD, OPPONENT_SEARCH_POLICY, OPPONENT_SEARCH_BUDGET);
            Parameters p2Parameters = new Parameters(OPPONENT_LAYER_SIZES, POSSIBLE_MOVE_SELECTOR_RATES[0], POSSIBLE_MOVE_SELECTOR_RATE_DECAY_FACTORS[0], POSSIBLE_LEARNING_RATES[0], POSSIBLE_LEARNING_RATE_DECAY_FACTORS[0], POSSIBLE_DECAY_RATES[0], POSSIBLE_ETAS[0], POSSIBLE_WEIGHT_DECAYS[0], POSSIBLE_LAMBDAS[0], opponentSettings);
            setupTraining(game, p1Parameters, p2Parameters, LEARNER_NETWORK_PATH, OPPONENT_NETWORK_PATH);
        }
    }

    private static List<Parameters> evaluateAndTrim(List<Parameters> combinationOfParameters) {
        combinationOfParameters = setupGridSearch(combinationOfParameters);
        Collections.sort(combinationOfParameters);
        combinationOfParameters = combinationOfParameters.subList(0, Math.min(combinationOfParameters.size(), GRID_SEARCH_KEEP));
        return combinationOfParameters;
    }

    private static List<Parameters> runGridSearch(List<Parameters> combinationOfParameters) {
        List<Parameters> resultingCombinations = combinationOfParameters;
        for (int i = 1; i < GRID_SEARCH_ITERATIONS; i++) {
            resultingCombinations = evaluateAndTrim(resultingCombinations);
            PrintUtil.writeParametersToFile(resultingCombinations, "grid_search" + i);
            resultingCombinations = Parameters.getRandomCombinationOfParametersForParameterDecayFactorExperiment(resultingCombinations);
            System.out.println("#############################");
        }
        return evaluateAndTrim(resultingCombinations);
    }

    private static List<Parameters> setupGridSearch(List<Parameters> combinationOfParameters) {
        final Counter counter = new Counter();
        final Counter completionCounter = new Counter();
        List<Parameters> resultingCombinations = new ArrayList<>();

        Parameters tmpParameters;
        for (Iterator<Parameters> it = combinationOfParameters.iterator(); it.hasNext();) {
            tmpParameters = it.next();
            if (tmpParameters.getBenchmarkStatistics() != null) {
                resultingCombinations.add(tmpParameters);
                it.remove();
            }
        }
        int totalRuns = combinationOfParameters.size() * GRID_SEARCH_RERUNS;

        combinationOfParameters.parallelStream().forEach((parameters) -> {
            Statistics trainingStatistics = new Statistics();
            Statistics benchmarkStatistics = new Statistics();
            System.out.println("Combination " + counter.incrAndGet() + " of " + combinationOfParameters.size());
            IntStream.range(0, GRID_SEARCH_RERUNS).parallel().forEach(i -> {
                Game game = new ConnectFourGame(BOARD_ROWS, BOARD_COLUMNS);
                String p1NetworkPath = "";
                if (SAVE_NETWORKS) {
                    p1NetworkPath = GRID_SEARCH_FOLDER + PrintUtil.formatArray(parameters.getLayerSizes()) + "-run" + i + "-p1";
                }
                Pair<Statistics, Statistics> p = setupTraining(game, parameters, parameters, p1NetworkPath, ""); // assuming self-play at grid search
                synchronized (parameters) {
                    if (parameters.getNetworkPath().equals("")) {
                        parameters.setNetworkPath(p1NetworkPath);
                        parameters.setNetworkStatistics(p.getSecond());
                    } else if (parameters.getNetworkStatistics().getWinRateLastEpoch() < p.getSecond().getWinRateLastEpoch()) {
                        parameters.setNetworkPath(p1NetworkPath);
                        parameters.setNetworkStatistics(p.getSecond());
                    }
                    trainingStatistics.combine(p.getFirst());
                    benchmarkStatistics.combine(p.getSecond());
                }
                System.out.println("Completed " + (completionCounter.incrAndGet() * 100 / totalRuns) + "%");
            });
            parameters.setBenchmarkStatistics(benchmarkStatistics);
            resultingCombinations.add(parameters);
        });

        return resultingCombinations;
    }

    private static Pair<Statistics, Statistics> setupTraining(Game game, Parameters p1Parameters, Parameters p2Parameters, String p1NetworkPath, String p2NetworkPath) {
        int[] p1LayerSizes = p1Parameters.getLayerSizes();
        int[] p2LayerSizes = p2Parameters.getLayerSizes();
        double moveSelectorRate = p1Parameters.getMoveSelectorRate();
        double moveSelectorRateDecayFactor = p1Parameters.getMoveSelectorRateDecayFactor();
        double learningRate = p1Parameters.getLearningRate();
        double learningRateDecayFactor = p1Parameters.getLearningRateDecayFactor();
        double eta = p1Parameters.getEta();
        NETWORK_HANDLER.setupNetwork(p1Parameters.getLayerSizes(), p1NetworkPath);
        NETWORK_HANDLER.setupNetwork(p2Parameters.getLayerSizes(), p2NetworkPath);

        Network p1Network = NETWORK_HANDLER.loadNetwork(p1NetworkPath, p1LayerSizes);
        Network p2Network = NETWORK_HANDLER.loadNetwork(p2NetworkPath, p2LayerSizes);
        Network benchmarkNetwork = (Network) ObjectLoader.loadObject(BENCHMARK_NETWORK_PATH);

        if (LEARNER_PLAYER == PlayerType.HUMAN || OPPONENT_PLAYER == PlayerType.HUMAN || BENCHMARK_PLAYER == PlayerType.HUMAN) setupGamePanel(game);
        Player p1 = PlayerType.getPlayer(p1Parameters.getPlayerSettings(), p1Network, p1Parameters, LEARNER_IS_LEARNING, DEBUG, gp);
        Player p2 = PlayerType.getPlayer(p2Parameters.getPlayerSettings(), p2Network, p2Parameters, OPPONENT_IS_LEARNING, false, gp);
        PlayerSettings benchmarkSettings = new PlayerSettings(BENCHMARK_PLAYER, BENCHMARK_BOARD_ENCODER, BENCHMARK_MOVE_SELECTOR, BENCHMARK_SEARCH_METHOD, BENCHMARK_SEARCH_POLICY, BENCHMARK_SEARCH_BUDGET);
        Player benchmarkPlayer = PlayerType.getPlayer(benchmarkSettings, benchmarkNetwork, p1Parameters, true, false, gp);

        Statistics trainingStatistics = new Statistics();
        Statistics benchmarkStatistics = new Statistics();

        double timeSpentTraining = 0;
        double timeSpentBenchmarking = 0;
        for (int epoch = 0; epoch < EPOCHS; epoch++) {
            double time = 0;
            if (!IS_GRID_SEARCH) {
                System.out.print("Epoch " + (epoch + 1) + "/" + EPOCHS + "\t");
                time = System.currentTimeMillis();
            }
            trainingStatistics.append(GameHandler.train(game, p1, p2, GAMES_PER_EPOCH, p1NetworkPath, p2NetworkPath));
            if (!IS_GRID_SEARCH) {
                timeSpentTraining += System.currentTimeMillis() - time;
                System.out.println(trainingStatistics.entryToString(epoch));
            }
            if (pp != null) pp.addTrainingData(epoch, trainingStatistics.getWinRate(epoch));

            if (BENCHMARK_GAMES > 0) {
                if (!IS_GRID_SEARCH) {
                    System.out.print("Benchmark\t");
                    time = System.currentTimeMillis();
                }
                benchmarkStatistics.append(GameHandler.benchmark(game, p1, benchmarkPlayer, BENCHMARK_GAMES));
                if (!IS_GRID_SEARCH) {
                    timeSpentBenchmarking += System.currentTimeMillis() - time;
                    System.out.println(benchmarkStatistics.entryToString(epoch));
                }
                if (pp != null) pp.addBenchmarkData(epoch, benchmarkStatistics.getWinRate(epoch));
                eta *= ETA_DECAY_FACTOR;
                moveSelectorRate *= moveSelectorRateDecayFactor;
                moveSelectorRate = Math.max(MIN_MOVE_SELECTOR_RATE, moveSelectorRate);
                learningRate *= learningRateDecayFactor;
                updateNNPlayer(p1, moveSelectorRate, learningRate, eta);
                updateNNPlayer(p2, moveSelectorRate, learningRate, eta);
            }
        }

        Pair<Statistics, Statistics> statistics = new Pair<>(trainingStatistics, benchmarkStatistics);
        if (!IS_GRID_SEARCH) {
            System.out.println();
            System.out.println("Training:");
            System.out.println(trainingStatistics);
            System.out.println("Benchmarking: ");
            System.out.println(benchmarkStatistics);

            PrintUtil.writeSessionToFile(statistics, p1Parameters, new PlayerSettings[]{p1Parameters.getPlayerSettings(), p2Parameters.getPlayerSettings(), benchmarkSettings}, timeSpentTraining, timeSpentBenchmarking, dateFormat.format(new Date()));
        }

        return statistics;
    }

    private static void setupGamePanel(Game game) {
        gp = new GamePanel(game);
        JFrame mainFrame = new JFrame("Game");
        mainFrame.add(gp);
        mainFrame.pack();
        mainFrame.repaint();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private static void setupSettings() {
        sp = new SettingsPanel();
        JFrame mainFrame = new JFrame("Settings");
        mainFrame.add(sp);
        mainFrame.pack();
        mainFrame.repaint();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private static void setupGraphPanel() {
        pp = new PlotPanel("Game: graph", EPOCHS);
        pp.pack();
        RefineryUtilities.centerFrameOnScreen(pp);
        pp.setVisible(true);
    }

    private static void updateNNPlayer(Player p, double moveSelectorRate, double learningRate, double eta) {
        if (p instanceof NeuralNetworkPlayer) {
            NeuralNetworkPlayer nnp = (NeuralNetworkPlayer) p;
            nnp.setMoveSelectorRate(moveSelectorRate);
            nnp.setLearningRate(learningRate);
            nnp.setEta(eta);
        }
    }

    private static void setupTournament(List<Parameters> combinationOfParameters) {
        List<Player> players = new ArrayList<>();
        Player player;
        Network network;

        for (Parameters parameters : combinationOfParameters) {
            network = NETWORK_HANDLER.loadNetwork(parameters.getNetworkPath(), parameters.getLayerSizes());
            PlayerSettings playerSettings = parameters.getPlayerSettings();
            parameters.setMoveSelectorRate(0.1);
            playerSettings.setMoveSelectorType(MoveSelectorType.SOFTMAX);
            player = PlayerType.getPlayer(playerSettings, network, parameters, false, false, null);
            System.out.println(player + " benchmark: " + parameters.getNetworkStatistics().getWinRateLastEpoch() + " from " + parameters.getNetworkPath());
            players.add(player);
        }
        IntStream.range(0, players.size()).parallel().forEach(i -> {
            IntStream.range(i + 1, players.size()).parallel().forEach(j -> {
                Game game;
                Player p1 = players.get(i);
                Player p2 = players.get(j);
                game = new ConnectFourGame(BOARD_ROWS, BOARD_COLUMNS);
                Statistics statistics = GameHandler.playGames(game, p1, p2, GAMES_PER_EPOCH, false, 0);
                System.out.println("Result from " + p1 + "\tversus " + p2 + ":  \t" + statistics.getWinRateLastEpoch());
            });
        });
    }
}

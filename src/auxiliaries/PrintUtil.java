package auxiliaries;

import static connectfour.Settings.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import reinforcementlearning.Parameters;
import reinforcementlearning.PlayerSettings;

public class PrintUtil {

    public static void writeParametersToFile(List<Parameters> combinationOfParameters, String filename) {

        FileWriter writer = null;
        try {
            writer = new FileWriter(filename + ".txt");
            for (Parameters tmp : combinationOfParameters) {
                writer.write(tmp.toString() + System.lineSeparator());
                writer.write(tmp.getBenchmarkStatistics().toString() + System.lineSeparator() + System.lineSeparator());
            }
        } catch (IOException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void writeSessionToFile(Pair<Statistics, Statistics> statistics, Parameters parameters, PlayerSettings[] playerSettings, double trainingTime, double benchmarkingTime, String filename) {

        FileWriter writer = null;
        try {
            writer = new FileWriter("training statistics/" + filename + ".txt");
            writer.write("Training time: " + trainingTime / 1000 + "s" + lineSeparators(1));
            writer.write("Benchmarking time " + benchmarkingTime / 1000 + "s" + lineSeparators(2));
            writer.write("Parameters:" + lineSeparators(1) + parameters.toString() + lineSeparators(2));
            writer.write(EPOCHS + " epochs" + lineSeparators(1) + GAMES_PER_EPOCH + " training games per epoch" + lineSeparators(1) + BENCHMARK_GAMES + " benchmark games per epoch" + lineSeparators(2));
            writer.write("Learner:" + lineSeparators(1) + playerSettings[0] + lineSeparators(1));
            writer.write("Opponent:" + lineSeparators(1) + playerSettings[1] + lineSeparators(1));
            writer.write("Benchmark:" + lineSeparators(1) + playerSettings[2] + lineSeparators(2));
            writer.write("Training statistics:" + lineSeparators(1) + statistics.getFirst().toString() + lineSeparators(1));
            writer.write("Benchmark statistics:" + lineSeparators(1) + statistics.getSecond().toString());
        } catch (IOException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String lineSeparators(int amount) {
        String res = "";
        for (int i = 0; i < amount; i++) {
            res += System.lineSeparator();
        }
        return res;
    }

    public static String formatArray(int[] arr) {
        String res = "{";
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) res += ", ";
            res += arr[i];
        }
        res += "}";
        return res;
    }

    public static String formatArray(double[] arr) {
        String res = "{";
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) res += ", ";
            res += arr[i];
        }
        res += "}";
        return res;
    }

    public static void printSettings() {
        PlayerSettings learnerSettings = new PlayerSettings(LEARNER_PLAYER, POSSIBLE_LEARNER_BOARD_ENCODERS[0], LEARNER_MOVE_SELECTOR, LEARNER_SEARCH_METHOD, LEARNER_SEARCH_POLICY, LEARNER_SEARCH_BUDGET);
        Parameters parameters = new Parameters(POSSIBLE_LEARNER_LAYER_SIZES[0], POSSIBLE_MOVE_SELECTOR_RATES[0], POSSIBLE_MOVE_SELECTOR_RATE_DECAY_FACTORS[0], POSSIBLE_LEARNING_RATES[0], POSSIBLE_LEARNING_RATE_DECAY_FACTORS[0], POSSIBLE_DECAY_RATES[0], POSSIBLE_ETAS[0], POSSIBLE_WEIGHT_DECAYS[0], POSSIBLE_LAMBDAS[0], learnerSettings);
        PlayerSettings opponentSettings = new PlayerSettings(OPPONENT_PLAYER, OPPONENT_BOARD_ENCODER, OPPONENT_MOVE_SELECTOR, OPPONENT_SEARCH_METHOD, OPPONENT_SEARCH_POLICY, OPPONENT_SEARCH_BUDGET);
        PlayerSettings benchmarkSettings = new PlayerSettings(BENCHMARK_PLAYER, BENCHMARK_BOARD_ENCODER, BENCHMARK_MOVE_SELECTOR, BENCHMARK_SEARCH_METHOD, BENCHMARK_SEARCH_POLICY, BENCHMARK_SEARCH_BUDGET);
        System.out.println("Parameters:" + lineSeparators(1) + parameters.toString());
        System.out.println(EPOCHS + " epochs" + lineSeparators(1) + GAMES_PER_EPOCH + " training games per epoch" + lineSeparators(1) + BENCHMARK_GAMES + " benchmark games per epoch" + lineSeparators(1) + GRID_SEARCH_RERUNS + " reruns" + lineSeparators(1) + INITIAL_PIECES + " initial pieces");
        System.out.print("Learner:" + lineSeparators(1) + learnerSettings);
        System.out.print("Opponent:" + lineSeparators(1) + opponentSettings);
        System.out.print("Benchmark:" + lineSeparators(1) + benchmarkSettings);
    }
}

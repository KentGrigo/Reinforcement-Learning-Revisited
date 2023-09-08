package reinforcementlearning;

import auxiliaries.*;
import static connectfour.Settings.*;
import java.util.ArrayList;
import java.util.List;

public class Parameters implements Comparable<Parameters> {

    private int[] layerSizes;
    private double moveSelectorRate;
    private double moveSelectorRateDecayFactor;
    private double learningRate;
    private double learningRateDecayFactor;
    private double decayRate;
    private double eta;
    private double weightDecay;
    private double lambda;
    private PlayerSettings playerSettings;
    private Statistics benchmarkStatistics;
    private String networkPath;
    private Statistics networkStatistics;

    public Parameters(int[] layerSizes, double moveSelectorRate, double moveSelectorRateDecayFactor, double learningRate, double learningRateDecayFactor, double decayRate, double eta, double weightDecay, double lambda, PlayerSettings playerSettings) {
        this.layerSizes = layerSizes;
        this.moveSelectorRate = moveSelectorRate;
        this.moveSelectorRateDecayFactor = moveSelectorRateDecayFactor;
        this.learningRate = learningRate;
        this.learningRateDecayFactor = learningRateDecayFactor;
        this.decayRate = decayRate;
        this.eta = eta;
        this.weightDecay = weightDecay;
        this.lambda = lambda;
        this.playerSettings = playerSettings;
        this.benchmarkStatistics = null;
        this.networkPath = "";
        this.networkStatistics = null;
    }

    public static List<Parameters> getCombinations(int[][] possibleLayerSizes, double[] possibleMoveSelectorRates, double[] possibleMoveSelectorRateDecayFactors, double[] possibleLearningRates, double[] possibleLearningRateDecayFactors, double[] possibleDecayRates,
            double[] possibleEtas, double[] possibleWeightDecays, double[] possibleLambdas, BoardEncoderType[] possibleBoardEncoderTypes) {
        List<Parameters> combinationOfParameters = new ArrayList<>();
        Parameters parameters;
        boolean varyBoardEncoderType = possibleBoardEncoderTypes.length > 1;
        int boardEncoderIdx = 0;
        BoardEncoderType boardEncoderType = null;
        if (!varyBoardEncoderType) {
            boardEncoderType = possibleBoardEncoderTypes[0];
        }
        for (int[] layerSizes : possibleLayerSizes) {
            if (varyBoardEncoderType) {
                boardEncoderType = possibleBoardEncoderTypes[boardEncoderIdx];
                boardEncoderIdx++;
            }
            for (double moveSelectorRate : possibleMoveSelectorRates) {
                for (double moveSelectorRateDecayFactor : possibleMoveSelectorRateDecayFactors) {
                    for (double learningRate : possibleLearningRates) {
                        for (double learningRateDecayFactor : possibleLearningRateDecayFactors) {
                            for (double decayRate : possibleDecayRates) {
                                for (double eta : possibleEtas) {
                                    for (double weightDecay : possibleWeightDecays) {
                                        for (double lambda : possibleLambdas) {
                                            PlayerSettings settings = new PlayerSettings(LEARNER_PLAYER, boardEncoderType, LEARNER_MOVE_SELECTOR, LEARNER_SEARCH_METHOD, LEARNER_SEARCH_POLICY, LEARNER_SEARCH_BUDGET);
                                            parameters = new Parameters(layerSizes, moveSelectorRate, moveSelectorRateDecayFactor, learningRate, learningRateDecayFactor, decayRate, eta, weightDecay, lambda, settings);
                                            combinationOfParameters.add(parameters);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return combinationOfParameters;
    }

    public static List<Parameters> updateCombinationOfParameters(List<Parameters> combinationOfParameters, int searchIteration) {
        List<Parameters> resultingCombinationOfParameters = new ArrayList<>();
        resultingCombinationOfParameters.addAll(combinationOfParameters);
        Parameters tmpParameters;
        for (Parameters parameters : combinationOfParameters) {
            double bound = Math.pow(0.5, searchIteration + 1);
            for (int decayRateFactor = -1; decayRateFactor <= 1; decayRateFactor += 2) {
                for (int lambdaFactor = -1; lambdaFactor <= 1; lambdaFactor += 2) {
                    tmpParameters = new Parameters(parameters.getLayerSizes(), parameters.getMoveSelectorRate(), parameters.getMoveSelectorRateDecayFactor(), parameters.getLearningRate(), parameters.getLearningRateDecayFactor(), parameters.getDecayRate() + decayRateFactor * bound, parameters.getEta(), parameters.getWeightDecay(), parameters.getLambda() + lambdaFactor * bound, parameters.getPlayerSettings());
                    resultingCombinationOfParameters.add(tmpParameters);
                }
            }
        }
        return resultingCombinationOfParameters;
    }

    public PlayerSettings getPlayerSettings() {
        return playerSettings;
    }

    public void setPlayerSettings(PlayerSettings playerSettings) {
        this.playerSettings = playerSettings;
    }

    public static List<Parameters> getRandomCombinationOfParameters(List<Parameters> combinationOfParameters) {
        List<Parameters> resultingCombinationOfParameters = new ArrayList<>();
        resultingCombinationOfParameters.addAll(combinationOfParameters);
        Parameters parameters = combinationOfParameters.get(0);
        for (int i = 0; i < 20; i++) {
            Parameters tmpParameters = new Parameters(parameters.getLayerSizes(), parameters.getMoveSelectorRate(), parameters.getMoveSelectorRateDecayFactor(), RNG.nextDouble(), parameters.getLearningRateDecayFactor(), RNG.nextDouble(), parameters.getEta(), parameters.getWeightDecay(), RNG.nextDouble(), parameters.getPlayerSettings());
            resultingCombinationOfParameters.add(tmpParameters);
        }
        return resultingCombinationOfParameters;
    }

    public static List<Parameters> getRandomCombinationOfParametersForParameterDecayFactorExperiment(List<Parameters> combinationOfParameters) {
        List<Parameters> resultingCombinationOfParameters = new ArrayList<>();
        resultingCombinationOfParameters.addAll(combinationOfParameters);
        Parameters parameters = combinationOfParameters.get(0);
        for (int i = 0; i < 20; i++) {
            Parameters tmpParameters = new Parameters(parameters.getLayerSizes(), RNG.nextDouble(), parameters.getMoveSelectorRateDecayFactor(), RNG.nextDouble(), parameters.getLearningRateDecayFactor(), parameters.getDecayRate(), parameters.getEta(), parameters.getWeightDecay(), parameters.getLambda(), parameters.getPlayerSettings());
            resultingCombinationOfParameters.add(tmpParameters);
        }
        return resultingCombinationOfParameters;
    }

    public static List<Parameters> getRandomCombinationOfParametersSarsaLambda(List<Parameters> combinationOfParameters) {
        List<Parameters> resultingCombinationOfParameters = new ArrayList<>();
        resultingCombinationOfParameters.addAll(combinationOfParameters);
        Parameters parameters = combinationOfParameters.get(0);
        for (int i = 0; i < 20; i++) {
            Parameters tmpParameters = new Parameters(parameters.getLayerSizes(), parameters.getMoveSelectorRate(), parameters.getMoveSelectorRateDecayFactor(), RNG.nextDouble(), parameters.getLearningRateDecayFactor(), RNG.nextDouble(), RNG.nextDouble() * 0.25, parameters.getWeightDecay(), RNG.nextDouble(), parameters.getPlayerSettings());
            resultingCombinationOfParameters.add(tmpParameters);
        }
        return resultingCombinationOfParameters;
    }

    public int[] getLayerSizes() {
        return layerSizes;
    }

    public void setLayerSizes(int[] layerSizes) {
        this.layerSizes = layerSizes;
    }

    public double getMoveSelectorRate() {
        return moveSelectorRate;
    }

    public void setMoveSelectorRate(double newMoveSelectorRate) {
        moveSelectorRate = newMoveSelectorRate;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getDecayRate() {
        return decayRate;
    }

    public double getEta() {
        return eta;
    }

    public double getWeightDecay() {
        return weightDecay;
    }

    public double getLambda() {
        return lambda;
    }

    public Statistics getBenchmarkStatistics() {
        return benchmarkStatistics;
    }

    public void setBenchmarkStatistics(Statistics benchmarkStatistics) {
        this.benchmarkStatistics = benchmarkStatistics;
    }

    @Override
    public int compareTo(Parameters o) {
        if (benchmarkStatistics.getWinRate(benchmarkStatistics.getEpochs() - 1) < o.getBenchmarkStatistics().getWinRate(o.getBenchmarkStatistics().getEpochs() - 1))
            return 1;
        else if (benchmarkStatistics.getWinRate(benchmarkStatistics.getEpochs() - 1) > o.getBenchmarkStatistics().getWinRate(o.getBenchmarkStatistics().getEpochs() - 1))
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "\tlayerSizes=" + PrintUtil.formatArray(layerSizes) + PrintUtil.lineSeparators(1) + "\tmoveSelectorRate=" + moveSelectorRate + PrintUtil.lineSeparators(1) + "\tmoveSelectorRateDecayFactor=" + moveSelectorRateDecayFactor + PrintUtil.lineSeparators(1) + "\tlearningRate=" + learningRate + PrintUtil.lineSeparators(1) + "\tlearningRateDecayFactor=" + learningRateDecayFactor + PrintUtil.lineSeparators(1) + "\tdecayRate=" + decayRate + PrintUtil.lineSeparators(1) + "\teta=" + eta + PrintUtil.lineSeparators(1) + "\tweightDecay=" + weightDecay + PrintUtil.lineSeparators(1) + "\tlambda=" + lambda;
    }

    public String getNetworkPath() {
        return networkPath;
    }

    public void setNetworkPath(String networkPath) {
        this.networkPath = networkPath;
    }

    public Statistics getNetworkStatistics() {
        return networkStatistics;
    }

    public void setNetworkStatistics(Statistics networkStatistics) {
        this.networkStatistics = networkStatistics;
    }

    public double getMoveSelectorRateDecayFactor() {
        return moveSelectorRateDecayFactor;
    }

    public void setMoveSelectorRateDecayFactor(double moveSelectorRateDecayFactor) {
        this.moveSelectorRateDecayFactor = moveSelectorRateDecayFactor;
    }

    public double getLearningRateDecayFactor() {
        return learningRateDecayFactor;
    }

    public void setLearningRateDecayFactor(double learningRateDecayFactor) {
        this.learningRateDecayFactor = learningRateDecayFactor;
    }
}

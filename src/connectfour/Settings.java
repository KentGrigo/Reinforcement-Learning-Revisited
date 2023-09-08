package connectfour;

import auxiliaries.*;
import neuralnetworks.*;

public class Settings {

    public static NetworkHandler NETWORK_HANDLER = new DefaultNetworkHandler();
    public static final int BOARD_ROWS = 6;
    public static final int BOARD_COLUMNS = 7;

    //Layer sizes for learner, opponent and benchmark networks, respectively.
    public static int[][] POSSIBLE_LEARNER_LAYER_SIZES = {{213, 100, 1}};
    public static int[] OPPONENT_LAYER_SIZES = {213, 100, 1};
    public static int[] BENCHMARK_LAYER_SIZES = {213, 100, 1};

    //TD_LAMBDA
    public static double[] POSSIBLE_MOVE_SELECTOR_RATES = {0.3}; // 0.3
    public static double[] POSSIBLE_LEARNING_RATES = {0.3};//0.3
    public static double[] POSSIBLE_DECAY_RATES = {1}; // 1
    public static double[] POSSIBLE_ETAS = {0.1}; //not part of TD_LAMBDA
    public static double[] POSSIBLE_WEIGHT_DECAYS = {0}; //not part of TD_LAMBDA
    public static double[] POSSIBLE_LAMBDAS = {0}; // 0

    public static String NETWORK_FOLDER = "./temporary networks/";
    public static String GRID_SEARCH_FOLDER = "./temporary grid search networks/";

    public static PlayerType LEARNER_PLAYER = PlayerType.TD_LAMBDA;
    public static String LEARNER_NETWORK_PATH = NETWORK_FOLDER + "{213, 100, 1}-search-methods-100k";
    public static boolean LEARNER_IS_LEARNING = true;
    public static BoardEncoderType[] POSSIBLE_LEARNER_BOARD_ENCODERS = {BoardEncoderType.FEATURE_BOARD_ENCODER};
    public static MoveSelectorType LEARNER_MOVE_SELECTOR = MoveSelectorType.SOFTMAX;
    public static SearchMethodType LEARNER_SEARCH_METHOD = SearchMethodType.MINIMAX;
    public static PolicyType LEARNER_SEARCH_POLICY = PolicyType.RANDOM_POLICY;
    public static int LEARNER_SEARCH_BUDGET = 1;

    public static PlayerType OPPONENT_PLAYER = PlayerType.TD_LAMBDA;
    public static String OPPONENT_NETWORK_PATH = NETWORK_FOLDER + "{213, 100, 1}-search-methods-100k";
    public static boolean OPPONENT_IS_LEARNING = true;
    public static BoardEncoderType OPPONENT_BOARD_ENCODER = BoardEncoderType.FEATURE_BOARD_ENCODER;
    public static MoveSelectorType OPPONENT_MOVE_SELECTOR = MoveSelectorType.SOFTMAX;
    public static SearchMethodType OPPONENT_SEARCH_METHOD = SearchMethodType.MCTS;
    public static PolicyType OPPONENT_SEARCH_POLICY = PolicyType.RANDOM_POLICY;
    public static int OPPONENT_SEARCH_BUDGET = 230;

    public static PlayerType BENCHMARK_PLAYER = PlayerType.MINIMAX;
    public static String BENCHMARK_NETWORK_PATH = NETWORK_FOLDER + "benchmarkNetwork";
    public static BoardEncoderType BENCHMARK_BOARD_ENCODER = BoardEncoderType.FEATURE_BOARD_ENCODER;
    public static MoveSelectorType BENCHMARK_MOVE_SELECTOR = MoveSelectorType.EPSILON_GREEDY;
    public static SearchMethodType BENCHMARK_SEARCH_METHOD = SearchMethodType.MINIMAX;
    public static PolicyType BENCHMARK_SEARCH_POLICY = PolicyType.RANDOM_POLICY;
    public static int BENCHMARK_SEARCH_BUDGET = 5;

    public static boolean OVERWRITE = true;
    public static boolean SAVE_NETWORKS = true;
    //if true, learner and opponent will use the same network
    public static boolean DEBUG = false;

    public static double[] POSSIBLE_MOVE_SELECTOR_RATE_DECAY_FACTORS = {1}; // 0.8
    public static double MIN_MOVE_SELECTOR_RATE = 0; //0.005 for softmax
    public static double[] POSSIBLE_LEARNING_RATE_DECAY_FACTORS = {1}; // 0.6
    public static double ETA_DECAY_FACTOR = 1; //not part of TD_LAMBDA

    public static boolean IS_GRID_SEARCH = false;
    public static int GRID_SEARCH_RERUNS = 3;
    public static int GRID_SEARCH_ITERATIONS = 0;
    public static int GRID_SEARCH_KEEP = 20;
    public static int EPOCHS = 10;
    public static int GAMES_PER_EPOCH = 10000;
    public static int BENCHMARK_GAMES = 0;
    public static int INITIAL_PIECES = 0;

    public static int BATCH_SIZE = 1;
}

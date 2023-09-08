package auxiliaries;

import game.*;
import neuralnetworks.BoardEncoder;
import neuralnetworks.Network;
import reinforcementlearning.*;
import searchmethods.SearchMethod;

public enum PlayerType {

    HUMAN, SARSA, RANDOM, MINIMAX, MCTS, TD_LAMBDA, SARSA_LAMBDA;

    public static Player getPlayer(PlayerSettings playerSettings, Network network, Parameters parameters, boolean learning, boolean debug, GamePanel gp) {
        BoardEncoder boardEncoder = BoardEncoderType.getBoardEncoder(playerSettings.getBoardEncoderType());
        MoveSelector moveSelector = MoveSelectorType.getMoveSelector(playerSettings.getMoveSelectorType(), parameters.getMoveSelectorRate());
        SearchMethod searchMethod = SearchMethodType.getSearchMethod(playerSettings.getSearchMethodType(), PolicyType.getPolicy(playerSettings.getPolicyType(), network));
        switch (playerSettings.getPlayerType()) {
            case HUMAN:
                return new HumanPlayer(gp);
            case TD_LAMBDA:
                return new TDLambdaLearningPlayer(network, boardEncoder, moveSelector, searchMethod, parameters, playerSettings.getSearchBudget(), learning, debug);
            case SARSA:
                return new SarsaLearningPlayer(network, boardEncoder, moveSelector, searchMethod, parameters, playerSettings.getSearchBudget(), learning, debug);
            case SARSA_LAMBDA:
                return new SarsaLambdaLearningPlayer(network, boardEncoder, moveSelector, searchMethod, parameters, playerSettings.getSearchBudget(), learning, debug);
            case MINIMAX:
                return new MinimaxPlayer(playerSettings.getSearchBudget());
            case MCTS:
                return new MCTSPlayer(playerSettings.getSearchBudget());
            case RANDOM:
                return new RandomAI();
            default:
                throw new IllegalArgumentException("Player type " + playerSettings.getPlayerType() + " isn't supported.");
        }
    }
}

package auxiliaries;

import searchmethods.*;

public enum SearchMethodType {

    MINIMAX, MCTS;

    public static SearchMethod getSearchMethod(SearchMethodType type, Policy policy) {
        switch (type) {
            case MINIMAX:
                return new MinimaxSearchMethod();
            case MCTS:
                return new MCTSSearchMethod(policy);
            default:
                throw new IllegalArgumentException("No such search method: " + type);
        }
    }
}

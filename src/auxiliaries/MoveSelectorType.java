package auxiliaries;

import reinforcementlearning.*;

public enum MoveSelectorType {

    EPSILON_GREEDY, SOFTMAX, EPSILON_SOFTMAX_GREEDY;

    public static MoveSelector getMoveSelector(MoveSelectorType type, double rate) {
        switch (type) {
            case EPSILON_GREEDY:
                return new EpsilonGreedyMoveSelector(rate);
            case SOFTMAX:
                return new SoftmaxMoveSelector(rate);
            case EPSILON_SOFTMAX_GREEDY:
                return new EpsilonSoftmaxGreedyMoveSelector(rate);
            default:
                throw new IllegalArgumentException("No such move selector: " + type);
        }
    }
}

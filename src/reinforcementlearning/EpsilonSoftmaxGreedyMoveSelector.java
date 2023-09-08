package reinforcementlearning;

import auxiliaries.RNG;
import java.util.Collections;
import java.util.List;

public class EpsilonSoftmaxGreedyMoveSelector extends MoveSelector {

    private final SoftmaxMoveSelector softmaxSelector;

    public EpsilonSoftmaxGreedyMoveSelector(double rate) {
        super(rate);
        softmaxSelector = new SoftmaxMoveSelector(1);
    }

    @Override
    public Move selectMove(List<Move> moves) {
        if (RNG.nextDouble() < rate) {
            return softmaxSelector.selectMove(moves);
        }

        Move selectedMove = Collections.max(moves, Move.getIncreasingComparator());
        return selectedMove;
    }

}

package reinforcementlearning;

import auxiliaries.RNG;
import java.util.Collections;
import java.util.List;

public class EpsilonGreedyMoveSelector extends MoveSelector {

    public EpsilonGreedyMoveSelector(double epsilon) {
        super(epsilon);
    }

    @Override
    public Move selectMove(List<Move> moves) {
        if (RNG.nextDouble() < rate) {
            return moves.get(RNG.nextInt(moves.size()));
        }

        Move selectedMove = Collections.max(moves, Move.getIncreasingComparator());
        return selectedMove;
    }
}

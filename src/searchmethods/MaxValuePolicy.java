package searchmethods;

import game.Game;
import java.util.List;
import neuralnetworks.BoardEncoder;
import reinforcementlearning.Move;
import reinforcementlearning.ValueFunction;

public class MaxValuePolicy implements Policy {

    private ValueFunction vf;

    public MaxValuePolicy(ValueFunction vf) {
        this.vf = vf;
    }

    @Override
    public Move selectAction(Game game, int[][] board, List<Move> moves, int playerNumber, int turn, BoardEncoder encoder) {
        moves.stream()
                .forEach(m -> m.setValue(vf.evaluate(encoder.encodeBoard(game, board, playerNumber, turn))));
        return moves.stream().max(Move.getDecreasingComparator()).get();
    }

}

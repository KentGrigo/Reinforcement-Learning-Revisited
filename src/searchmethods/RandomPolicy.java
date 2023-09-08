package searchmethods;

import auxiliaries.RNG;
import game.Game;
import java.util.List;
import neuralnetworks.BoardEncoder;
import reinforcementlearning.Move;

public class RandomPolicy implements Policy {

    @Override
    public Move selectAction(Game game, int[][] board, List<Move> moves, int playerNumber, int turn, BoardEncoder encoder) {
        return moves.get(RNG.nextInt(moves.size()));
    }

}

package searchmethods;

import game.Game;
import java.util.List;
import neuralnetworks.BoardEncoder;
import reinforcementlearning.Move;

public interface Policy {

    public Move selectAction(Game game, int[][] board, List<Move> moves, int playerNumber, int turn, BoardEncoder encoder);
}

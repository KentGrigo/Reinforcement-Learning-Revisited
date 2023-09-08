package neuralnetworks;

import game.Game;
import org.apache.commons.math3.linear.RealVector;

public interface BoardEncoder {

    /**
     * Encode board with respect to player, using game to find player numbers.
     *
     * @param board the board to encode.
     * @param playerNumber
     * @return board encoded as a RealVector.
     */
    public RealVector encodeBoard(Game game, int[][] board, int playerNumber, int playerTurn);
}

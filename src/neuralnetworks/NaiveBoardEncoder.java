package neuralnetworks;

import game.Game;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class NaiveBoardEncoder implements BoardEncoder {

    @Override
    public RealVector encodeBoard(Game game, int[][] board, int playerNumber, int playerTurn) {
        int boardSize = board.length * board[0].length;
        RealVector v = new ArrayRealVector(boardSize + 1);
        int entry;
        if (playerTurn == -1) {
            entry = -1;
        } else if (playerTurn == 1) {
            entry = 1;
        } else {
            entry = 0;
        }
        v.setEntry(boardSize, entry);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == playerNumber) {
                    entry = 1;
                } else if (board[i][j] != 0) {
                    entry = -1;
                } else {
                    entry = 0;
                }
                v.setEntry(i * board[i].length + j, entry);
            }
        }
        return v;
    }
}

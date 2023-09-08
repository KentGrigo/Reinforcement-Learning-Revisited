package neuralnetworks;

import game.Game;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class DefaultBoardEncoder implements BoardEncoder {

    @Override
    public RealVector encodeBoard(Game game, int[][] board, int playerNumber, int playerTurn) {
        int boardSize = board.length * board[0].length;
        RealVector v = new ArrayRealVector(2 * boardSize + 2);
        int selfEntry, opponentEntry;
        if (playerTurn == -1) {
            selfEntry = 0;
            opponentEntry = 1;

        } else if (playerTurn == 1) {
            selfEntry = 1;
            opponentEntry = 0;
        } else {
            selfEntry = 0;
            opponentEntry = 0;
        }
        v.setEntry(2 * boardSize, selfEntry);
        v.setEntry(2 * boardSize + 1, opponentEntry);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == playerNumber) {
                    selfEntry = 1;
                    opponentEntry = 0;
                } else if (board[i][j] != 0) {
                    selfEntry = 0;
                    opponentEntry = 1;
                } else {
                    selfEntry = opponentEntry = 0;
                }
                v.setEntry(i * board[i].length + j, selfEntry);
                v.setEntry(boardSize + i * board[i].length + j, opponentEntry);
            }
        }
        return v;
    }
}

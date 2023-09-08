package neuralnetworks;

import game.BoardChecker;
import game.Game;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class FeatureBoardEncoder implements BoardEncoder {

    @Override
    public RealVector encodeBoard(Game game, int[][] board, int playerNumber, int playerTurn) {
        int opponentPlayerNumber = (1 - (playerNumber - 1)) + 1;
        int boardSize = board.length * board[0].length;
        RealVector v = new ArrayRealVector(5 * boardSize + 3);
        int selfEntry, opponentEntry, blankEntry;
        if (playerTurn == -1) {
            selfEntry = 0;
            opponentEntry = 1;
            blankEntry = 0;

        } else if (playerTurn == 1) {
            selfEntry = 1;
            opponentEntry = 0;
            blankEntry = 0;
        } else {
            selfEntry = 0;
            opponentEntry = 0;
            blankEntry = 1;
        }
        v.setEntry(5 * boardSize, selfEntry);
        v.setEntry(5 * boardSize + 1, opponentEntry);
        v.setEntry(5 * boardSize + 2, blankEntry);

        int selfSequenceEntry, opponentSequenceEntry;
        BoardChecker bc = game.getBoardChecker(board);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                selfSequenceEntry = opponentSequenceEntry = 0;
                if (board[i][j] == playerNumber) {
                    selfEntry = 1;
                    opponentEntry = 0;
                    blankEntry = 0;
                } else if (board[i][j] != 0) {
                    selfEntry = 0;
                    opponentEntry = 1;
                    blankEntry = 0;
                } else {
                    selfEntry = 0;
                    opponentEntry = 0;
                    blankEntry = 1;
                    if (bc.checkWinner(playerNumber, i, j)) {
                        selfSequenceEntry = 1;
                    }
                    if (bc.checkWinner(opponentPlayerNumber, i, j)) {
                        opponentSequenceEntry = 1;
                    }
                }

                v.setEntry(3 * i * board[i].length + 3 * j, selfEntry);
                v.setEntry(3 * i * board[i].length + 3 * j + 1, opponentEntry);
                v.setEntry(3 * i * board[i].length + 3 * j + 2, blankEntry);
                v.setEntry(3 * boardSize + 2 * i * board[i].length + 2 * j, selfSequenceEntry);
                v.setEntry(3 * boardSize + 2 * i * board[i].length + 2 * j + 1, opponentSequenceEntry);
            }
        }
        return v;
    }
}

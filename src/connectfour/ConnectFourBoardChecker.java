package connectfour;

import game.BoardChecker;
import java.util.*;
import reinforcementlearning.Move;

public class ConnectFourBoardChecker extends BoardChecker {

    private final int[][] board;
    private final int rows, columns;

    public ConnectFourBoardChecker(int[][] board) {
        this.board = board;
        rows = board.length;
        columns = board[0].length;
    }

    private boolean checkWinnerHelper(int player, int row, int column, int rf, int cf) {
        int count = 1;
        for (int i = 1; i < 4; i++) {
            if (getPiece(row + rf * i, column + cf * i) == player) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 4; i++) {
            if (getPiece(row - rf * i, column - cf * i) == player) {
                count++;
            } else {
                break;
            }
        }
        return count >= 4;
    }

    public boolean checkWinner(int player, int row, int column) {
        boolean hasWon = false;

        //North east diagonal
        hasWon |= checkWinnerHelper(player, row, column, 1, 1);
        //South east diagonal
        hasWon |= checkWinnerHelper(player, row, column, -1, 1);
        //East
        hasWon |= checkWinnerHelper(player, row, column, 0, 1);
        //North
        hasWon |= checkWinnerHelper(player, row, column, 1, 0);

        return hasWon;
    }

    private void markWinnerHelper(int player, int row, int column, int rf, int cf) {
        List<Move> winningPieces = new LinkedList<>();
        winningPieces.add(new Move(row, column));
        for (int i = 1; i < 4; i++) {
            if (getPiece(row + rf * i, column + cf * i) == player) {
                winningPieces.add(new Move(row + rf * i, column + cf * i));
            } else {
                break;
            }
        }
        for (int i = 1; i < 4; i++) {
            if (getPiece(row - rf * i, column - cf * i) == player) {
                winningPieces.add(new Move(row - rf * i, column - cf * i));
            } else {
                break;
            }
        }
        if (winningPieces.size() >= 4) {
            markPieces(winningPieces, player);
        }
    }

    private void markPieces(List<Move> pieces, int player) {
        for (Move move : pieces) {
            if (board[move.getRow()][move.getColumn()] == player) {
                board[move.getRow()][move.getColumn()] += 2;
            }
        }
    }

    @Override
    public void markWinner(int player, int row, int column) {
        //North east diagonal
        markWinnerHelper(player, row, column, 1, 1);
        //South east diagonal
        markWinnerHelper(player, row, column, -1, 1);
        //East
        markWinnerHelper(player, row, column, 0, 1);
        //North
        markWinnerHelper(player, row, column, 1, 0);
    }

    /**
     * @return the piece at (r,c) if it's a valid entry, -1 otherwise
     */
    private int getPiece(int r, int c) {
        if (r < rows && r >= 0 && c < columns && c >= 0) {
            return board[r][c];
        }
        return -1;
    }

    public int getFreeRow(int column) {
        if (column < columns && column >= 0) {
            for (int row = 0; row < board.length; row++) {
                if (board[row][column] == 0) {
                    return row;
                }
            }
        }
        return -1;
    }

    private List<Integer> getFreeColumns() {
        List<Integer> freeColumns = new ArrayList<>(columns);
        for (int column = 0; column < board[0].length; column++) {
            if (board[board.length - 1][column] == 0) {
                freeColumns.add(column);
            }
        }
        return freeColumns;
    }

    @Override
    public boolean isDraw() {
        return getFreeColumns().isEmpty();
    }

    @Override
    public List<Move> getPossibleMoves() {
        List<Move> possibleMoves = new LinkedList<>();
        int row;
        for (int column = 0; column < columns; column++) {
            row = getFreeRow(column);
            if (row != -1) {
                possibleMoves.add(new Move(row, column));
            }
        }
        return possibleMoves;
    }

}

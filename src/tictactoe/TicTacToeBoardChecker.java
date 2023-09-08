package tictactoe;

import game.BoardChecker;
import java.util.ArrayList;
import java.util.List;
import reinforcementlearning.Move;

class TicTacToeBoardChecker extends BoardChecker {

    private static final int WIDTH = TicTacToe.WIDTH;
    private static final int HEIGHT = TicTacToe.HEIGHT;
    private static final int BLANK = TicTacToe.BLANK;

    private final int[][] board;

    public TicTacToeBoardChecker(int[][] board) {
        this.board = board;
    }

    public boolean isWinningHorizontal(int player, int row) {
        int counter = 0;
        for (int column = 0; column < WIDTH; column++) {
            if (board[row][column] == player) {
                counter++;
            } else {
                counter = 0;
            }
        }
        return 3 <= counter;
    }

    public boolean isWinningVertical(int player, int column) {
        int counter = 0;
        for (int row = 0; row < HEIGHT; row++) {
            if (board[row][column] == player) {
                counter++;
            } else {
                counter = 0;
            }
        }
        return 3 <= counter;
    }

    public boolean isWinningDiagonalSE(int player) {
        int counter = 0;
        for (int i = 0; i < HEIGHT; i++) {
            if (board[HEIGHT - i - 1][i] == player) {
                counter++;
            } else {
                counter = 0;
            }
        }
        return 3 <= counter;
    }

    public boolean isWinningDiagonalNE(int player) {
        int counter = 0;
        for (int i = 0; i < HEIGHT; i++) {
            if (board[i][WIDTH - i - 1] == player) {
                counter++;
            } else {
                counter = 0;
            }
        }
        return 3 <= counter;
    }

    @Override
    public boolean checkWinner(int player, int row, int column) {
        boolean isWinner = false;
        for (int i = 0; i < HEIGHT; i++) {
            isWinner |= isWinningHorizontal(player, i);
            isWinner |= isWinningVertical(player, i);
        }
        return isWinner || isWinningDiagonalNE(player) || isWinningDiagonalSE(player);
    }

    @Override
    public boolean isDraw() {
        return getPossibleMoves().isEmpty();
    }

    @Override
    public List<Move> getPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++) {
                if (board[row][column] == BLANK)
                    moves.add(new Move(row, column));
            }
        }
        return moves;
    }

    @Override
    public void markWinner(int player, int row, int column) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

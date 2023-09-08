package tictactoe;

import game.*;
import java.util.ArrayList;
import java.util.List;
import reinforcementlearning.Move;

public class TicTacToe implements Game {

    static final int WIDTH = 3;
    static final int HEIGHT = 3;
    static final int BLANK = 0;

    private final int[][] board = new int[WIDTH][HEIGHT];
    private int activePlayer = 0;
    private final Player[] players;
    private final List<GameListener> gameListeners;
    private final BoardChecker boardChecker;

    public TicTacToe() {
        boardChecker = new TicTacToeBoardChecker(board);
        gameListeners = new ArrayList<>();
        players = new Player[2];
    }

    public void initializeBoard() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++) {
                board[row][column] = BLANK;
            }
        }
    }

    public boolean makeMove(int row, int column) {
        if (board[row][column] != BLANK) {
            return false;
        }

        board[row][column] = activePlayer + 1;
        notifyGameListeners();

        boolean isWinner = boardChecker.checkWinner(activePlayer + 1, row, column);
        if (boardChecker.isDraw()) {
            return true;
        } else if (!isWinner) {
            activePlayer = 1 - activePlayer;
        }
        return isWinner;
    }

    @Override
    public int getPiece(int row, int column) {
        return board[row][column];
    }

    @Override
    public int getPlayerNumber(Player p) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == p) return i + 1;
        }
        throw new IllegalArgumentException("There exists no player number for player " + p);
    }

    @Override
    public int getActivePlayer() {
        return activePlayer + 1;
    }

    @Override
    public int[][] getBoard() {
        int[][] boardCopy = new int[HEIGHT][WIDTH];
        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++) {
                boardCopy[row][column] = board[row][column];
            }
        }
        return boardCopy;
    }

    @Override
    public List<Move> getPossibleMoves() {
        return boardChecker.getPossibleMoves();
    }

    @Override
    public int getRows() {
        return HEIGHT;
    }

    @Override
    public int getColumns() {
        return WIDTH;
    }

    @Override
    public void addGameListener(GameListener gl) {
        gameListeners.add(gl);
    }

    public void removeGameListener(GameListener gl) {
        gameListeners.remove(gl);
    }

    private void notifyGameListeners() {
        gameListeners.stream().forEach((gl) -> {
            gl.boardChanged();
        });
    }

    @Override
    public BoardChecker getBoardChecker(int[][] board) {
        return new TicTacToeBoardChecker(board);
    }

    @Override
    public int startGame(Player p1, Player p2) {
        players[0] = p1;
        players[1] = p2;
        activePlayer = 0;
        initializeBoard();
        Move move;
        int row;
        int column;
        while (true) {
            move = players[activePlayer].makeMove(this); // change
            row = move.getRow();
            column = move.getColumn();
            if (makeMove(row, column)) {
                break;
            }
        }

        if (boardChecker.checkWinner(activePlayer + 1, -1, -1)) {
            players[activePlayer].win(this);
            players[1 - activePlayer].lose(this);
            return activePlayer + 1;
        } else {
            players[activePlayer].draw(this);
            players[1 - activePlayer].draw(this);
            return 0;
        }
    }

    @Override
    public int startGame(Player p1, Player p2, int initialPieces) {
        System.out.println("Initial pieces are not supported yet. It was set to: " + initialPieces);
        return startGame(p1, p2);
    }

    @Override
    public void printBoardTikz() {
        throw new UnsupportedOperationException("No tikz for TicTacToe boards."); //To change body of generated methods, choose Tools | Templates.
    }
}

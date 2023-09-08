package connectfour;

import auxiliaries.RNG;
import game.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import reinforcementlearning.Move;

public class ConnectFourGame implements Game {

    //0 = empty, 1 = first player, 2 = second player, anything else is invalid
    private final int rows, columns;
    private int placed;
    private final int[][] board;
    private Player[] players;
    private final ConnectFourBoardChecker boardChecker;
    private int activePlayer;
    private boolean winnerFound;
    private final List<GameListener> gameListeners;

    public ConnectFourGame(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        board = new int[rows][columns];
        boardChecker = new ConnectFourBoardChecker(board);
        players = new Player[2];
        winnerFound = false;
        activePlayer = 0;

        gameListeners = new ArrayList<>();
    }

    private void initBoard(int initialPieces) {
        if (initialPieces >= rows * columns) {
            throw new IllegalArgumentException("Cannot initialize full board");
        }
        reset();
        boolean moveMade;
        while (placed < initialPieces) {
            List<Move> moves = boardChecker.getPossibleMoves();
            moveMade = false;
            while (!moveMade) {
                Move move = moves.get(RNG.nextInt(moves.size()));
                board[move.getRow()][move.getColumn()] = activePlayer + 1;
                if (boardChecker.checkWinner(activePlayer + 1, move.getRow(), move.getColumn())) {
                    board[move.getRow()][move.getColumn()] = 0;
                    moves.remove(move);
                } else {
                    moveMade = true;
                    placed++;
                    activePlayer = 1 - activePlayer;
                }
            }
        }
    }

    private int playGame(Player p1, Player p2) {
        players[0] = p1;
        players[1] = p2;
        int row, column;
        int winner;
        while (true) {
            Move move = players[activePlayer].makeMove(this);
            column = move.getColumn();
            row = boardChecker.getFreeRow(column);
            if (row != -1) {
                board[row][column] = activePlayer + 1;
                placed++;
                winnerFound = boardChecker.checkWinner(activePlayer + 1, row, column);
                notifyGameListeners();
                if (winnerFound) {
                    //System.out.println("Player " + (activePlayer+1) + " has won!");
                    players[activePlayer].win(this);
                    players[1 - activePlayer].lose(this);
                    winner = activePlayer + 1;
                    break;
                } else if (placed == rows * columns) {
                    //System.out.println("Draw!");
                    players[0].draw(this);
                    players[1].draw(this);
                    winner = 0;
                    break;
                } else {
                    activePlayer = 1 - activePlayer;
                }
            }
        }
        if (players[0] instanceof HumanPlayer || players[1] instanceof HumanPlayer) {
            if (winner != 0) {
                boardChecker.markWinner(winner, row, column);
                notifyGameListeners();
                System.out.println("Player " + winner + " won the game!");
            } else {
                System.out.println("The game ended in a draw!");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectFourGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return winner;
    }

    @Override
    public int getPlayerNumber(Player p) {
        if (players[0] == p) {
            return 1;
        } else if (players[1] == p) {
            return 2;
        }
        throw new IllegalArgumentException("Player " + p + " is not in this game.");
    }

    @Override
    public int startGame(Player p1, Player p2) {
        reset();
        notifyGameListeners();
        return playGame(p1, p2);
    }

    @Override
    public int startGame(Player p1, Player p2, int initialPieces) {
        initBoard(initialPieces);
        notifyGameListeners();
        return playGame(p1, p2);
    }

    @Override
    public void addGameListener(GameListener gl) {
        gameListeners.add(gl);
    }

    public void removeGameListener(GameListener gl) {
        gameListeners.remove(gl);
    }

    private void reset() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = 0;
            }
        }
        placed = 0;
        winnerFound = false;
        activePlayer = 0;
    }

    public int getActivePlayer() {
        return activePlayer + 1;
    }

    /**
     * @return the piece at (r,c) if it's a valid entry, -1 otherwise
     */
    @Override
    public int getPiece(int r, int c) {
        if (r < rows && r >= 0 && c < columns && c >= 0) {
            return board[r][c];
        }
        return -1;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    private void notifyGameListeners() {
        gameListeners.stream().forEach((gl) -> {
            gl.boardChanged();
        });
    }

    @Override
    public int[][] getBoard() {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
        }
        return newBoard;
    }

    public int getFreeRow(int column) {
        return boardChecker.getFreeRow(column);
    }

    @Override
    public List<Move> getPossibleMoves() {
        return boardChecker.getPossibleMoves();
    }

    @Override
    public BoardChecker getBoardChecker(int[][] board) {
        return new ConnectFourBoardChecker(board);
    }

    @Override
    public void printBoardTikz() {
        String res = "\\begin{tikzpicture} " + System.lineSeparator() + "\\board";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    res += System.lineSeparator() + "\\Xtikz{" + i + "}{" + j + "}";
                } else if (board[i][j] == 2) {
                    res += System.lineSeparator() + "\\Otikz{" + i + "}{" + j + "}";
                }
            }
        }
        res += System.lineSeparator() + "\\end{tikzpicture}";
        System.out.println(res);
    }
}

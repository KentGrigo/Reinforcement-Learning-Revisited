package game;

import java.util.List;
import reinforcementlearning.Move;

public interface Game {

    /**
     * Returns a list of possible moves to be made in this game
     *
     * @return the list of possible moves
     */
    public List<Move> getPossibleMoves();

    /**
     * Returns a COPY of the board currently in this game, meaning changes made
     * to the returned board should not affect the game.
     *
     * @return the copy of the board
     */
    public int[][] getBoard();

    /**
     * Returns the player number for p, should be 1 or 2 in accordance to the
     * numbers inserted in the board for the corresponding player.
     *
     * @param p player to get number for
     * @return number of p
     */
    public int getPlayerNumber(Player p);

    /**
     * @return currently active player, either 1 or 2.
     */
    public int getActivePlayer();

    /**
     * @return the number of rows in this game
     */
    public int getRows();

    /**
     * @return the number of columns in this game
     */
    public int getColumns();

    /**
     * Adds gl to the list of GameListeners who should be alerted whenever a
     * move is made.
     *
     * @param gl
     */
    public void addGameListener(GameListener gl);

    /**
     * Returns the piece at place (row, column)
     *
     * @return
     */
    public int getPiece(int row, int column);

    /**
     * Returns a BoardChecker for the game type of this game for the board
     * given.
     *
     * @param board to create a BoardChecker for.
     * @return
     */
    public BoardChecker getBoardChecker(int[][] board);

    /**
     * Start a game between two players
     *
     * @param p1 first player
     * @param p2 second player
     * @return winner of the game (0, 1, or 2)
     */
    public int startGame(Player p1, Player p2);

    /**
     * Start a game between two players
     *
     * @param p1 first player
     * @param p2 second player
     * @param initialPieces number of randomly placed initial pieces
     * @return winner of the game (0, 1, or 2)
     */
    public int startGame(Player p1, Player p2, int initialPieces);

    /**
     * Print a representation of the game board in our tikz format.
     */
    public void printBoardTikz();

    /**
     * Returns the other player's number.
     *
     * @param playerNumber must be 1 or 2
     * @return
     */
    public static int getOtherPlayer(int playerNumber) {
        return 3 - playerNumber;
    }
}

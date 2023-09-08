package game;

import java.util.List;
import reinforcementlearning.Move;

public abstract class BoardChecker {

    public abstract boolean checkWinner(int player, int row, int column);

    public abstract void markWinner(int player, int row, int column);

    public abstract boolean isDraw();

    public abstract List<Move> getPossibleMoves();

    public boolean isTerminal(int player, Move move) {
        return isDraw() || checkWinner(player, move.getRow(), move.getColumn());
    }
}

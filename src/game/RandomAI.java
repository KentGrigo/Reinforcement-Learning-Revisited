package game;

import auxiliaries.RNG;
import java.util.List;
import reinforcementlearning.Move;

public class RandomAI extends BasePlayer {

    @Override
    public Move makeMove(Game game) {
        List<Move> possibleMoves = game.getPossibleMoves();
        return possibleMoves.get(RNG.nextInt(possibleMoves.size()));
    }
}

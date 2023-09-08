package game;

import searchmethods.MinimaxSearchMethod;
import reinforcementlearning.Move;

public class MinimaxPlayer extends BasePlayer {

    private final int searchDepth;
    private final MinimaxSearchMethod minimaxSearch;

    public MinimaxPlayer(int searchDepth) {
        this.searchDepth = searchDepth;
        this.minimaxSearch = new MinimaxSearchMethod();

    }

    @Override
    public Move makeMove(Game game) {
        return minimaxSearch.searchForMove(game, this, searchDepth);
    }
}

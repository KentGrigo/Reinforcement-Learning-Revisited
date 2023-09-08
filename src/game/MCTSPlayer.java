package game;

import reinforcementlearning.Move;
import searchmethods.MCTSSearchMethod;
import searchmethods.RandomPolicy;

public class MCTSPlayer extends BasePlayer {

    private final int budget;
    private final MCTSSearchMethod searchMethod;

    public MCTSPlayer(int budget) {
        this.budget = budget;
        this.searchMethod = new MCTSSearchMethod(new RandomPolicy());
    }

    @Override
    public Move makeMove(Game game) {
        return searchMethod.searchForMove(game, this, budget);
    }

}

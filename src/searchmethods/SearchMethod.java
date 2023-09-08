package searchmethods;

import game.*;
import java.util.List;
import reinforcementlearning.Move;
import reinforcementlearning.NeuralNetworkPlayer;

public interface SearchMethod {

    public List<Move> searchForMoves(Game game, NeuralNetworkPlayer player, int searchBudget);

    public Move searchForMove(Game game, Player player, int searchBudget);
}

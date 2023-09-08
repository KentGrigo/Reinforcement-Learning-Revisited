package game;

import auxiliaries.RNG;
import auxiliaries.Statistics;
import static connectfour.Settings.*;
import reinforcementlearning.NeuralNetworkPlayer;

public class GameHandler {

    public static Statistics train(Game game, Player p1, Player p2, int numberOfGames, String network1Path, String network2Path) {
        Statistics statistics = playGames(game, p1, p2, numberOfGames, false);
        if (p1 instanceof NeuralNetworkPlayer) {
            NETWORK_HANDLER.saveNetwork((NeuralNetworkPlayer) p1, network1Path);
        }
        if (p2 instanceof NeuralNetworkPlayer) {
            NETWORK_HANDLER.saveNetwork((NeuralNetworkPlayer) p2, network2Path);
        }
        return statistics;
    }

    public static Statistics benchmark(Game game, Player p1, Player p2, int numberOfGames) {
        Statistics statistics;
        if (p1 instanceof NeuralNetworkPlayer) {
            boolean isLearning;
            double rate;
            NeuralNetworkPlayer nnp = (NeuralNetworkPlayer) p1;
            isLearning = nnp.isLearning();
            nnp.setLearning(false);
            rate = nnp.getMoveSelectorRate();
            nnp.setMoveSelectorRate(0);

            statistics = playGames(game, p1, p2, numberOfGames, true);

            nnp.setLearning(isLearning);
            nnp.setMoveSelectorRate(rate);
        } else {
            statistics = playGames(game, p1, p2, numberOfGames, true);
        }
        return statistics;
    }

    public static Statistics playGames(Game game, Player p1, Player p2, int numberOfGames, boolean benchmark) {
        return playGames(game, p1, p2, numberOfGames, benchmark, INITIAL_PIECES);
    }

    public static Statistics playGames(Game game, Player p1, Player p2, int numberOfGames, boolean benchmark, int initialPieces) {
        Player[] players = {p1, p2};
        int startingPlayer = 0;
        int[] wins = new int[2];
        int draws = 0;
        int winner;
        for (int i = 0; i < numberOfGames; i++) {
            if (benchmark) winner = game.startGame(p1, p2);
            else {
                if (initialPieces <= 0) winner = game.startGame(players[startingPlayer], players[1 - startingPlayer]);
                else winner = game.startGame(players[startingPlayer], players[1 - startingPlayer], RNG.nextInt(initialPieces + 1));
            }
            if (winner == 1) wins[startingPlayer]++;
            else if (winner == 2) wins[1 - startingPlayer]++;
            else draws++;
            if (!benchmark) {
                startingPlayer = 1 - startingPlayer;
            }
        }
        return new Statistics(wins[0], wins[1], draws, numberOfGames);
    }

}

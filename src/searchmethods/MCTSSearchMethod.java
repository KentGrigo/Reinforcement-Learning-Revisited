package searchmethods;

import auxiliaries.RNG;
import game.*;
import java.util.*;
import java.util.function.BiFunction;
import neuralnetworks.BoardEncoder;
import reinforcementlearning.*;
import trees.*;

public class MCTSSearchMethod implements SearchMethod {

    //To satisfy the Hoeffding inequality
    private final double cp = 1 / Math.sqrt(2);
    //how many simulations to get accuracy equivalent to NN evaluation.
    private final int neuralNetworkAccuracy = 30;
    private final Policy policy;

    public MCTSSearchMethod(Policy policy) {
        this.policy = policy;
    }

    @Override
    public List<Move> searchForMoves(Game game, NeuralNetworkPlayer player, int searchBudget) {
        int[][] board = game.getBoard();
        BoardChecker boardChecker = game.getBoardChecker(board);
        int playerNumber = Game.getOtherPlayer(game.getPlayerNumber(player));
        RootNode rootNode = new RootNode(board, boardChecker, playerNumber, false);
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < searchBudget) {
            Node n = treePolicy(rootNode, expand(game, player));
            int[][] state = rootNode.getState();
            double delta = defaultPolicy(n, state, rootNode.getBoardChecker(), game, player.getBoardEncoder());
            backupNegamax(n, delta, state);
        }

        List<Move> moves = new LinkedList<>();
        for (Node n : rootNode.getChildren()) {
            Move m = n.getMove();
            m.setValue(computeNodeValue(n, 0));
            moves.add(m);
        }
        return moves;
    }

    @Override
    public Move searchForMove(Game game, Player player, int searchBudget) {
        int[][] board = game.getBoard();
        BoardChecker boardChecker = game.getBoardChecker(board);
        int playerNumber = Game.getOtherPlayer(game.getPlayerNumber(player));
        RootNode rootNode = new RootNode(board, boardChecker, playerNumber, false);

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < searchBudget) {
            Node n = treePolicy(rootNode, this::expand);
            int[][] state = rootNode.getState();
            double delta = defaultPolicy(n, state, rootNode.getBoardChecker(), game, null);
            backupNegamax(n, delta, state);
        }

        Node best = bestChild(rootNode, 0, board);
        return best.getMove();
    }

    private void backupNegamax(Node n, double delta, int[][] state) {
        while (n != null) {
            n.Q = ((n.Q * n.N) + delta) / (n.N + 1);
            n.N++;
            delta = -delta;
            Move move = n.getMove();
            if (move != null) {
                state[move.getRow()][move.getColumn()] = 0;
            }
            n = n.getParent();
        }
    }

    private Node treePolicy(RootNode root, BiFunction<Node, int[][], Node> expandFunction) {
        int[][] state = root.getState();
        Node n = root;
        while (!n.isTerminal()) {
            if (!n.getUntriedMoves().isEmpty()) {
                return expandFunction.apply(n, state);
            } else {
                n = bestChild(n, cp, state);
            }
        }
        return n;
    }

    public Node bestChild(Node n, double c, int[][] state) {
        List<Node> children = n.getChildren();
        double bestValue = Double.NEGATIVE_INFINITY;
        Node bestChild = children.get(0);
        for (Node m : children) {
            double value = computeNodeValue(m, c);
            if (value > bestValue) {
                bestValue = value;
                bestChild = m;
            }
        }
        Move move = bestChild.getMove();
        state[move.getRow()][move.getColumn()] = bestChild.getMovingPlayerNumber();
        return bestChild;
    }

    private double computeNodeValue(Node n, double c) {
        Node parent = n.getParent();
        double value;
        if (n.N == 0) {
            value = Double.POSITIVE_INFINITY;
        } else {
            value = n.Q + c * Math.sqrt(2 * Math.log(parent.N) / n.N);
        }
        return value;
    }

    public BiFunction<Node, int[][], Node> expand(Game game, NeuralNetworkPlayer p) {
        final ValueFunction vf = p.getValueFunction();
        final BoardEncoder encoder = p.getBoardEncoder();
        return (Node n, int[][] state) -> expandHelper(n, state, (Node node, Move move, BoardChecker boardChecker, int playerNumber, boolean isTerminal) -> {
            double initialQ = vf.evaluate(encoder.encodeBoard(game, state, playerNumber, -1)) * 2 - 1;
            return new Node(node, move, boardChecker, playerNumber, initialQ, neuralNetworkAccuracy, isTerminal);
        });
    }

    public Node expand(Node n, int[][] state) {
        return expandHelper(n, state, (Node node, Move move, BoardChecker boardChecker, int playerNumber, boolean isTerminal) -> {
            //initial values of Q and N are both 0
            return new Node(node, move, boardChecker, playerNumber, 0, 0, isTerminal);
        });
    }

    public Node expandHelper(Node n, int[][] state, NodeCreator creator) {
        List<Move> untriedMoves = n.getUntriedMoves();
        Move move = untriedMoves.get(RNG.nextInt(untriedMoves.size()));
        untriedMoves.remove(move);

        int otherPlayerNumber = Game.getOtherPlayer(n.getMovingPlayerNumber());
        state[move.getRow()][move.getColumn()] = otherPlayerNumber;

        BoardChecker bc = n.getBoardChecker();
        boolean isTerminal = bc.isTerminal(otherPlayerNumber, move);

        Node newNode = creator.createNode(n, move, bc, otherPlayerNumber, isTerminal);
        n.addChild(newNode);
        return newNode;
    }

    private double defaultPolicy(Node n, int[][] state, BoardChecker boardChecker, Game game, BoardEncoder encoder) {
        int lastMovingPlayer = n.getMovingPlayerNumber();
        Move move = n.getMove();
        int activePlayer = lastMovingPlayer;
        List<Move> moves = new LinkedList<>();
        double reward = 0;
        while (move == null || !boardChecker.isTerminal(activePlayer, move)) {
            activePlayer = Game.getOtherPlayer(activePlayer);
            List<Move> possibleMoves = boardChecker.getPossibleMoves();
            move = policy.selectAction(game, state, possibleMoves, activePlayer, 1, encoder);
            moves.add(move);
            state[move.getRow()][move.getColumn()] = activePlayer;
        }
        if (boardChecker.checkWinner(activePlayer, move.getRow(), move.getColumn())) {
            if (activePlayer == lastMovingPlayer) {
                reward = 1;
            } else {
                reward = -1;
            }
        } else if (boardChecker.isDraw()) {
            reward = 0;
        }
        for (Move m : moves) {
            state[m.getRow()][m.getColumn()] = 0;
        }
        return reward;
    }
}

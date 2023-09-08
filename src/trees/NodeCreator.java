package trees;

import game.BoardChecker;
import reinforcementlearning.Move;

public interface NodeCreator {

    public Node createNode(Node node, Move move, BoardChecker boardChecker, int playerNumber, boolean isTerminal);
}

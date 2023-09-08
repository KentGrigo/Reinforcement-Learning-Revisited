package trees;

import game.BoardChecker;
import java.util.LinkedList;
import java.util.List;
import reinforcementlearning.Move;

public class Node {

    protected Node parent;
    private final Move move;
    public double Q;
    public int N;
    protected int movingPlayerNumber;
    protected boolean isTerminal;

    protected List<Move> untriedMoves;
    protected BoardChecker boardChecker;
    protected List<Node> children;

    public Node(Node parent, Move move, BoardChecker boardChecker, int movingPlayerNumber, double initialQ, int initialN, boolean isTerminal) {
        this.parent = parent;
        this.move = move;
        this.Q = initialQ;
        this.N = initialN;
        this.movingPlayerNumber = movingPlayerNumber;
        this.isTerminal = isTerminal;

        this.untriedMoves = boardChecker.getPossibleMoves();
        this.boardChecker = boardChecker;
        this.children = new LinkedList<>();
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public List<Move> getUntriedMoves() {
        return untriedMoves;
    }

    public BoardChecker getBoardChecker() {
        return boardChecker;
    }

    public int getMovingPlayerNumber() {
        return movingPlayerNumber;
    }

    public void addChild(Node n) {
        children.add(n);
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public Move getMove() {
        return move;
    }
}

package trees;

import game.BoardChecker;

public class RootNode extends Node {

    private final int[][] state;

    public RootNode(int[][] state, BoardChecker boardChecker, int movingPlayerNumber, boolean isTerminal) {
        super(null, null, boardChecker, movingPlayerNumber, 0, 0, isTerminal);
        this.state = state;
    }

    public int[][] getState() {
        return state;
    }

}

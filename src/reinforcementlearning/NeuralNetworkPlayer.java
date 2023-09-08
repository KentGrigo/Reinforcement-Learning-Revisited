package reinforcementlearning;

import game.*;
import java.util.*;
import neuralnetworks.BoardEncoder;
import searchmethods.SearchMethod;

public abstract class NeuralNetworkPlayer implements Player {

    protected ValueFunction valueFunction;
    protected BoardEncoder boardEncoder;
    protected MoveSelector moveSelector;
    protected SearchMethod searchMethod;

    protected double learningRate, eta;

    protected int searchBudget;
    protected boolean learning;
    protected boolean debug;

    protected int[][] lastState, currentState;
    protected int lastStateTurn, currentStateTurn;
    protected double lastStateValue, currentStateValue;

    public NeuralNetworkPlayer(ValueFunction valueFunction, BoardEncoder boardEncoder,
            MoveSelector moveSelector, SearchMethod searchMethod, Parameters parameters, int searchBudget, boolean learning, boolean debug) {
        this.valueFunction = valueFunction;
        this.boardEncoder = boardEncoder;
        this.moveSelector = moveSelector;
        this.searchMethod = searchMethod;

        this.learningRate = parameters.getLearningRate();
        this.eta = parameters.getEta();

        this.searchBudget = searchBudget;
        this.learning = learning;
        this.debug = debug;
        this.lastState = null;
        this.lastStateTurn = 0;
        this.lastStateValue = 0;
        this.currentState = null;
        this.currentStateTurn = 0;
        this.currentStateValue = 0;
    }

    @Override
    public Move makeMove(Game game) {
        int[][] board = game.getBoard();
        int activePlayer = game.getActivePlayer();
        List<Move> moves = searchMethod.searchForMoves(game, this, searchBudget);
        if (debug) {
            for (Move m : moves) {
                System.out.println("(" + (m.getColumn() + 1) + ", value: " + m.getValue() + ")");
            }
            System.out.println("********************");
        }

        Move selectedMove = moveSelector.selectMove(moves);

        if (learning) {
            board[selectedMove.getRow()][selectedMove.getColumn()] = activePlayer;
            currentState = board;
            currentStateValue = selectedMove.getValue();
            currentStateTurn = -1;
            changeLastStateValue(game, 0);
        }
        return selectedMove;
    }

    private void updateCurrentStateAndValue(Game game, int turn) {
        currentState = game.getBoard();
        currentStateValue = 0;
        currentStateTurn = turn;
    }

    private void gameEnded(Game game, double reward, int turn) {
        if (learning) {
            updateCurrentStateAndValue(game, turn);
            changeLastStateValue(game, reward);
        }
        reset();
    }

    @Override
    public void win(Game game) {
        gameEnded(game, getWinReward(), -1);
    }

    @Override
    public void lose(Game game) {
        gameEnded(game, getLoseReward(), 1);
    }

    @Override
    public void draw(Game game) {
        gameEnded(game, getDrawReward(), 0);
    }

    private void reset() {
        lastState = null;
    }

    public BoardEncoder getBoardEncoder() {
        return boardEncoder;
    }

    public void setMoveSelectorRate(double newMoveSelectorRate) {
        moveSelector.setRate(newMoveSelectorRate);
    }

    public double getMoveSelectorRate() {
        return moveSelector.getRate();
    }

    public double getWinReward() {
        return 1;
    }

    public double getLoseReward() {
        return 0;
    }

    public double getDrawReward() {
        return 0;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setValueFunction(ValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    public ValueFunction getValueFunction() {
        return valueFunction;
    }

    public void setLearning(boolean learning) {
        this.learning = learning;
    }

    public boolean isLearning() {
        return learning;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public double getEta() {
        return eta;
    }

    public MoveSelector getMoveSelector() {
        return moveSelector;
    }

    public void setMoveSelector(MoveSelector moveSelector) {
        this.moveSelector = moveSelector;
    }

    protected abstract void changeLastStateValue(Game game, double reward);

    public void setSearchBudget(int searchBudget) {
        this.searchBudget = searchBudget;
    }

    public SearchMethod getSearchMethod() {
        return searchMethod;
    }

}

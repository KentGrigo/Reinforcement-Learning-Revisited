package reinforcementlearning;

import game.Game;
import neuralnetworks.BoardEncoder;
import searchmethods.SearchMethod;

public class SarsaLearningPlayer extends NeuralNetworkPlayer {

    private final double learningRate, decayRate, weightDecay;

    public SarsaLearningPlayer(ValueFunction valueFunction, BoardEncoder boardEncoder, MoveSelector moveSelector, SearchMethod searchMethod, Parameters parameters, int searchDepth, boolean learning, boolean debug) {
        super(valueFunction, boardEncoder, moveSelector, searchMethod, parameters, searchDepth, learning, debug);
        this.learningRate = parameters.getLearningRate();
        this.decayRate = parameters.getDecayRate();
        this.weightDecay = parameters.getWeightDecay();
        this.debug = debug;
    }

    /**
     * Changes the value of the last state towards the value of the current
     * state of 'game'.
     */
    @Override
    protected void changeLastStateValue(Game game, double reward) {
        if (lastState != null) {
//            double currentStateValue = valueFunction.evaluate(boardEncoder.encodeBoard(currentState, this, game, turn));
            lastStateValue = lastStateValue + learningRate * (reward + decayRate * currentStateValue - lastStateValue);
            valueFunction.changeStateValue(boardEncoder.encodeBoard(game, lastState, game.getPlayerNumber(this), lastStateTurn), lastStateValue, eta, weightDecay);
        }
        lastState = currentState;
        lastStateValue = currentStateValue;
        lastStateTurn = currentStateTurn;
    }

    /**
     * @return the weightDecay
     */
    public double getWeightDecay() {
        return weightDecay;
    }
}

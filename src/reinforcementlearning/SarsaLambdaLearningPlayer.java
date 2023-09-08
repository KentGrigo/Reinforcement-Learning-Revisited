package reinforcementlearning;

import auxiliaries.Pair;
import game.Game;
import java.util.Iterator;
import java.util.LinkedList;
import neuralnetworks.BoardEncoder;
import org.apache.commons.math3.linear.RealVector;
import searchmethods.SearchMethod;

public class SarsaLambdaLearningPlayer extends NeuralNetworkPlayer {

    private static final double MIN_ELIGIBILITY_TRACE = 0.001;

    private double decayRate, weightDecay, lambda;
    private LinkedList<Pair<RealVector, Double>> eligibilityTraces;

    public SarsaLambdaLearningPlayer(ValueFunction valueFunction, BoardEncoder boardEncoder, MoveSelector moveSelector, SearchMethod searchMethod, Parameters parameters, int searchDepth, boolean learning, boolean debug) {
        super(valueFunction, boardEncoder, moveSelector, searchMethod, parameters, searchDepth, learning, debug);
        this.learningRate = parameters.getLearningRate();
        this.decayRate = parameters.getDecayRate();
        this.weightDecay = parameters.getWeightDecay();
        this.lambda = parameters.getLambda();
        this.debug = debug;

        this.eligibilityTraces = new LinkedList<>();
    }

    /**
     * Changes the value of the last state towards the value of the current
     * state of 'game'.
     */
    @Override
    protected void changeLastStateValue(Game game, double reward) {
        if (lastState != null) {
//            BoardVisualizer.createBoardVisualizer(lastState, "Last State", 600, 0);
//            BoardVisualizer.createBoardVisualizer(currentState, "Current State", 1200, 0);

            RealVector state = boardEncoder.encodeBoard(game, lastState, game.getPlayerNumber(this), lastStateTurn);
            eligibilityTraces.addFirst(new Pair<>(state, 1.0));
            double delta = reward + decayRate * currentStateValue - lastStateValue;
//            double currentStateValue = valueFunction.evaluate(boardEncoder.encodeBoard(currentState, this, game, turn));
            Pair<RealVector, Double> et;
            for (Iterator<Pair<RealVector, Double>> it = eligibilityTraces.iterator(); it.hasNext();) {
                et = it.next();
                double value = valueFunction.evaluate(et.getFirst());
                valueFunction.changeStateValue(et.getFirst(), value + learningRate * delta * et.getSecond(), eta, weightDecay);
                et.setSecond(et.getSecond() * decayRate * lambda);
                if (et.getSecond() <= MIN_ELIGIBILITY_TRACE) {
                    it.remove();
                }
            }
        }
        lastState = currentState;
        lastStateValue = currentStateValue;
        lastStateTurn = currentStateTurn;
    }

    private void resetEligibilityTraces() {
        eligibilityTraces.clear();
    }

    @Override
    public void win(Game game) {
        super.win(game);
        resetEligibilityTraces();
    }

    @Override
    public void lose(Game game) {
        super.lose(game);
        resetEligibilityTraces();
    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        resetEligibilityTraces();
    }

    /**
     * @return the weightDecay
     */
    public double getWeightDecay() {
        return weightDecay;
    }
}

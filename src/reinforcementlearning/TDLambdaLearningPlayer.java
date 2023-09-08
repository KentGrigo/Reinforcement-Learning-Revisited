package reinforcementlearning;

import auxiliaries.PrintUtil;
import game.Game;
import neuralnetworks.BoardEncoder;
import org.apache.commons.math3.linear.*;
import searchmethods.SearchMethod;

public class TDLambdaLearningPlayer extends NeuralNetworkPlayer {

    private final int[] layerSizes;
    private final double decayRate, lambda;
    private final RealVector[] biasEligibilityTraces;
    private final RealMatrix[] weightEligibilityTraces;

    public TDLambdaLearningPlayer(ValueFunction valueFunction, BoardEncoder boardEncoder, MoveSelector moveSelector, SearchMethod searchMethod, Parameters parameters, int searchDepth, boolean learning, boolean debug) {
        super(valueFunction, boardEncoder, moveSelector, searchMethod, parameters, searchDepth, learning, debug);
        this.layerSizes = parameters.getLayerSizes();
        this.decayRate = parameters.getDecayRate();
        this.lambda = parameters.getLambda();
        biasEligibilityTraces = new RealVector[layerSizes.length];
        weightEligibilityTraces = new RealMatrix[layerSizes.length];

        for (int i = 1; i < layerSizes.length; i++) {
            biasEligibilityTraces[i] = new ArrayRealVector(layerSizes[i]);
            weightEligibilityTraces[i] = MatrixUtils.createRealMatrix(layerSizes[i], layerSizes[i - 1]);
        }
    }

    @Override
    protected void changeLastStateValue(Game game, double reward) {
        if (lastState != null) {
            RealVector state = boardEncoder.encodeBoard(game, lastState, game.getPlayerNumber(this), lastStateTurn);
            double delta = (reward + decayRate * currentStateValue - lastStateValue);
            valueFunction.changeStateValue(state, lastStateValue, currentStateValue, delta, learningRate, decayRate, lambda, biasEligibilityTraces, weightEligibilityTraces);
        }
        lastState = currentState;
        lastStateValue = currentStateValue;
        lastStateTurn = currentStateTurn;
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

    public void resetEligibilityTraces() {
        for (int i = 1; i < weightEligibilityTraces.length; i++) {
            weightEligibilityTraces[i] = weightEligibilityTraces[i].scalarMultiply(0);
            biasEligibilityTraces[i].mapMultiplyToSelf(0);
        }
    }

    @Override
    public String toString() {
        return PrintUtil.formatArray(layerSizes);
    }

}

package reinforcementlearning;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public interface ValueFunction {

    public double evaluate(RealVector state);

    public void changeStateValue(RealVector state, double newValue, double eta, double weightDecay);

    public void changeStateValue(RealVector state, double vs, double vsPrime, double tdError, double learningRate, double gamma, double lambda, RealVector[] biasEligibilityTraces, RealMatrix[] weightEligibilityTraces);
}

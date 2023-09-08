package neuralnetworks;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.RealVector;

public class QuadraticCostFunction implements CostFunction {

    @Override
    public double evaluate(RealVector desiredOutputs, RealVector outputActivations, RealVector weightedInputs) {
        double v = desiredOutputs.subtract(outputActivations).getNorm();
        return 0.5 * v * v;
    }

    @Override
    public RealVector gradient(RealVector desiredOutputs, RealVector outputActivations, RealVector weightedInputs) {
        return weightedInputs.map(new UnivariateFunction() {

            @Override
            public double value(double x) {
                return sigmoidPrime(x);
            }
        }).ebeMultiply(outputActivations.subtract(desiredOutputs));
    }

    private double sigmoid(double z) {
        return 1.0 / (2.0 + Math.expm1(-z));
    }

    private double sigmoidPrime(double z) {
        return sigmoid(z) * (1 - sigmoid(z));
    }

}

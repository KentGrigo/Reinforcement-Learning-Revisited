package neuralnetworks;

import org.apache.commons.math3.linear.RealVector;

public class CrossEntropyCostFunction implements CostFunction {

    @Override
    public double evaluate(RealVector desiredOutputs, RealVector outputActivations, RealVector weightedInputs) {
        double res = 0;
        for (int i = 0; i < desiredOutputs.getDimension(); i++) {
            double y = desiredOutputs.getEntry(i);
            double a = outputActivations.getEntry(i);
            double temp = -y * Math.log1p(a - 1) - (1 - y) * Math.log1p(-a);
            if (temp == Double.NaN) {
                temp = 0;
            }
            res += temp;
        }
        return res;
    }

    @Override
    public RealVector gradient(RealVector desiredOutputs, RealVector outputActivations, RealVector weightedInputs) {
        return outputActivations.subtract(desiredOutputs);
    }

}

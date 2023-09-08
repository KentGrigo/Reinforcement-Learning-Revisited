package neuralnetworks;

import java.io.Serializable;
import org.apache.commons.math3.linear.RealVector;

public interface CostFunction extends Serializable {

    public double evaluate(RealVector desiredOutputs, RealVector outputActivations, RealVector weightedInputs);

    public RealVector gradient(RealVector desiredOutputs, RealVector outputActivations, RealVector weightedInputs);
}

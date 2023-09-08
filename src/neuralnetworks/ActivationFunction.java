package neuralnetworks;

import java.io.Serializable;
import org.apache.commons.math3.analysis.UnivariateFunction;

public abstract class ActivationFunction implements UnivariateFunction, Serializable {

    public abstract double apply(double z);

    public abstract double applyPrime(double z);

    @Override
    public double value(double x) {
        return applyPrime(x);
    }

}

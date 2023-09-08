package neuralnetworks;

import org.apache.commons.math3.util.FastMath;

public class TanhActivationFunction extends ActivationFunction {

    @Override
    public double apply(double z) {
        return FastMath.tanh(z);
    }

    @Override
    public double applyPrime(double z) {
        return FastMath.pow(1 / FastMath.cosh(z), 2);
    }

}

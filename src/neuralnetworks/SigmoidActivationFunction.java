package neuralnetworks;

import org.apache.commons.math3.util.FastMath;

public class SigmoidActivationFunction extends ActivationFunction {

    @Override
    public double apply(double z) {
        return 1.0 / (2.0 + FastMath.expm1(-z));
    }

    @Override
    public double applyPrime(double z) {
        return apply(z) * (1 - apply(z));
    }

    public static double exp(double val) {
        final long tmp = (long) (1512775 * val + (1072693248 - 60801));
        return Double.longBitsToDouble(tmp << 32);
    }
}

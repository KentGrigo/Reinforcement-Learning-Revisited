package neuralnetworks;

import org.apache.commons.math3.util.FastMath;

public class RectifiedLinearActivationFunction extends ActivationFunction {

    @Override
    public double apply(double z) {
        return FastMath.max(0, z);
    }

    @Override
    public double applyPrime(double z) {
        if (z > 0) {
            return 1;
        }
        return 0;
    }

}

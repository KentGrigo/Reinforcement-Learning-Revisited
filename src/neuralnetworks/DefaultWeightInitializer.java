package neuralnetworks;

import auxiliaries.RNG;

public class DefaultWeightInitializer implements WeightInitializer {

    @Override
    public double[] initWeights(int numWeights) {
        double[] weights = new double[numWeights];
        double div = Math.sqrt(numWeights);
        for (int i = 0; i < numWeights; i++) {
            weights[i] = RNG.nextGaussian() / div;
        }
        return weights;
    }

    @Override
    public double initBias() {
        return RNG.nextGaussian();
    }

}

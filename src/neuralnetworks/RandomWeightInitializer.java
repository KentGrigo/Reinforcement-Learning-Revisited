package neuralnetworks;

import auxiliaries.RNG;

public class RandomWeightInitializer implements WeightInitializer {

    @Override
    public double[] initWeights(int numWeights) {
        double[] weights = new double[numWeights];
        for (int i = 0; i < numWeights; i++) {
            weights[i] = RNG.nextGaussian();
        }
        return weights;
    }

    @Override
    public double initBias() {
        return RNG.nextGaussian();
    }

}

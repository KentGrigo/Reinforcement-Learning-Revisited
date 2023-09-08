package neuralnetworks;

public interface WeightInitializer {

    public double[] initWeights(int numWeights);

    public double initBias();
}

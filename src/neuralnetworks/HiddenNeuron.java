package neuralnetworks;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class HiddenNeuron implements Neuron {

    private RealVector weights;
    private double bias;

    public HiddenNeuron(int numWeights, WeightInitializer init) {
        this.weights = new ArrayRealVector(init.initWeights(numWeights));
        bias = init.initBias();
    }

    @Override
    public double feed(RealVector input) {
        return weights.dotProduct(input) + bias;
    }

    @Override
    public double getWeight(int i) {
        return weights.getEntry(i);
    }

    @Override
    public RealVector getWeights() {
        return weights;
    }

    @Override
    public void setWeights(RealVector newWeights) {
        weights = newWeights;
    }

    @Override
    public double getBias() {
        return bias;
    }

    @Override
    public void setBias(double newBias) {
        bias = newBias;
    }

}

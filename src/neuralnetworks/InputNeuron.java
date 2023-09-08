package neuralnetworks;

import org.apache.commons.math3.linear.RealVector;

public class InputNeuron implements Neuron {

    @Override
    public double feed(RealVector input) {
        throw new UnsupportedOperationException("Input neurons do not accept feeds.");
    }

    @Override
    public double getWeight(int i) {
        throw new UnsupportedOperationException("Input neurons do not have weights.");
    }

    @Override
    public RealVector getWeights() {
        throw new UnsupportedOperationException("Input neurons do not have weights.");
    }

    @Override
    public void setWeights(RealVector newWeights) {
        throw new UnsupportedOperationException("Input neurons do not have weights.");
    }

    @Override
    public double getBias() {
        throw new UnsupportedOperationException("Input neurons do not have a bias.");
    }

    @Override
    public void setBias(double newBias) {
        throw new UnsupportedOperationException("Input neurons do not have a bias.");
    }

}

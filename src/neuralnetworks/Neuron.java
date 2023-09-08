package neuralnetworks;

import java.io.Serializable;
import org.apache.commons.math3.linear.RealVector;

public interface Neuron extends Serializable {

    public double feed(RealVector input);

    public double getWeight(int i);

    public RealVector getWeights();

    public void setWeights(RealVector newWeights);

    public double getBias();

    public void setBias(double newBias);
}

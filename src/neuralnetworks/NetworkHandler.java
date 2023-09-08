package neuralnetworks;

import reinforcementlearning.NeuralNetworkPlayer;

public interface NetworkHandler {

    public Network loadNetwork(String network, int[] layerSizes);

    public void setupNetwork(int[] layerSizes, String networkPath);

    public void saveNetwork(NeuralNetworkPlayer p, String networkPath);
}

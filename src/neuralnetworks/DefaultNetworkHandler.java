package neuralnetworks;

import static connectfour.Settings.*;
import reinforcementlearning.NeuralNetworkPlayer;

public class DefaultNetworkHandler implements NetworkHandler {

    @Override
    public Network loadNetwork(String network, int[] layerSizes) {
        if (network.equals("")) {
            return new Network(layerSizes, new SigmoidActivationFunction(), new CrossEntropyCostFunction(), new DefaultWeightInitializer(), BATCH_SIZE);
        } else {
            return (Network) ObjectLoader.loadObject(network);
        }
    }

    @Override
    public void setupNetwork(int[] layerSizes, String networkPath) {
        if (OVERWRITE) {
            Network network;
            if (!networkPath.equals("")) {
                network = new Network(layerSizes, new SigmoidActivationFunction(), new CrossEntropyCostFunction(), new DefaultWeightInitializer(), BATCH_SIZE);
                ObjectLoader.saveObject(network, networkPath);
            }
        }
    }

    @Override
    public void saveNetwork(NeuralNetworkPlayer p, String networkPath) {
        if (!networkPath.equals("")) {
            if (SAVE_NETWORKS) {
                ObjectLoader.saveObject(p.getValueFunction(), networkPath);
            }
        }
    }

}

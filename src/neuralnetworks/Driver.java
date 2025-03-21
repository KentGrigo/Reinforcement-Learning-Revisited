package neuralnetworks;

import java.util.ArrayList;
import java.util.List;
import auxiliaries.Pair;
import org.apache.commons.math3.linear.RealVector;

public class Driver {

    public static void main(String[] args) {
        int[] sizes = {784, 30, 10};
        Network network = new Network(sizes, new SigmoidActivationFunction(), new CrossEntropyCostFunction(), new DefaultWeightInitializer(), 10);

        List<Pair<RealVector, RealVector>> trainingData = loadData("trainingImages", "trainingLabels");
        List<Pair<RealVector, RealVector>> testData = loadData("testImages", "testLabels");
//        Collections.shuffle(trainingData);
//        trainingData = trainingData.subList(0, 5000);

        network.stochasticGradientDescent(1.5, 4, 30, trainingData, testData, 0.8);
//        ObjectLoader.saveObject(network, "variable_learning_rate_network_0_9");
    }

    private static List<Pair<RealVector, RealVector>> loadData(String imageFile, String labelFile) {
        RealVector[] imageData = (RealVector[]) ObjectLoader.loadObject(imageFile);
        RealVector[] labelData = (RealVector[]) ObjectLoader.loadObject(labelFile);

        List<Pair<RealVector, RealVector>> data = new ArrayList<>();
        for (int i = 0; i < imageData.length; i++) {
            data.add(new Pair<>(imageData[i], labelData[i]));
        }
        return data;
    }
}

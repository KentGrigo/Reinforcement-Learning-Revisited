package neuralnetworks;

import java.io.Serializable;
import java.util.*;
import javafx.util.Pair;
import org.apache.commons.math3.linear.*;
import reinforcementlearning.ValueFunction;

public class Network implements ValueFunction, Serializable {

    public final Neuron[][] neurons;
    private final ActivationFunction activationFunction;
    private final CostFunction costFunction;
    private final List<Pair<RealVector, RealVector>> savedExamples;
    private final int batchSize;

    public Network(int[] layerSizes, ActivationFunction activationFunction, CostFunction costFunction, WeightInitializer weightInitializer, int batchSize) {
        this.costFunction = costFunction;
        neurons = new Neuron[layerSizes.length][];

        this.activationFunction = activationFunction;
        for (int i = 1; i < layerSizes.length; i++) {
            neurons[i] = new Neuron[layerSizes[i]];
            for (int j = 0; j < neurons[i].length; j++) {
                neurons[i][j] = new HiddenNeuron(layerSizes[i - 1], weightInitializer);
            }
        }
        neurons[0] = new Neuron[layerSizes[0]];
        for (int i = 0; i < layerSizes[0]; i++) {
            neurons[0][i] = new InputNeuron();
        }
        savedExamples = new ArrayList<>();
        this.batchSize = batchSize;
    }

    public Network(int[] layerSizes, ActivationFunction activationFunction, CostFunction costFunction, WeightInitializer weightInitializer) {
        this(layerSizes, activationFunction, costFunction, weightInitializer, 1);
    }

    public Pair<RealVector[], RealVector[]> feedForward(RealVector input) {
        RealVector[] weightedInputs = new RealVector[neurons.length];
        RealVector[] activations = new RealVector[neurons.length];
        weightedInputs[0] = input;
        activations[0] = input;
        RealVector weightedInput;
        RealVector activation;
        Neuron n;
        for (int i = 1; i < neurons.length; i++) {
            weightedInput = new ArrayRealVector(new double[neurons[i].length]);
            activation = new ArrayRealVector(new double[neurons[i].length]);
            for (int j = 0; j < neurons[i].length; j++) {
                n = neurons[i][j];
                double z = n.feed(activations[i - 1]);
                weightedInput.setEntry(j, z);
                activation.setEntry(j, activationFunction.apply(z));
            }
            weightedInputs[i] = weightedInput;
            activations[i] = activation;
        }
        return new Pair<>(weightedInputs, activations);
    }

    public RealVector[] backpropagate(RealVector[] weightedInputs, RealVector[] activations, RealVector desiredOutput) {
        int amountOfLayers = weightedInputs.length;
//        RealVector activationPrime = weightedInputs[amountOfLayers - 1].map(activationFunction);
        RealVector[] deltas = new RealVector[amountOfLayers];
//        deltas[amountOfLayers - 1] = activationPrime.ebeMultiply(costFunction.gradient(desiredOutput, activations[amountOfLayers - 1], weightedInputs[amountOfLayers-1]));
        deltas[amountOfLayers - 1] = costFunction.gradient(desiredOutput, activations[amountOfLayers - 1], weightedInputs[amountOfLayers - 1]);
        RealVector delta;
        RealVector activationPrime;
        for (int i = amountOfLayers - 2; i >= 0; i--) {
            delta = new ArrayRealVector(neurons[i].length);
            activationPrime = weightedInputs[i].map(activationFunction);
            for (int j = 0; j < neurons[i].length; j++) {
                RealVector weights = new ArrayRealVector(neurons[i + 1].length);
                for (int k = 0; k < neurons[i + 1].length; k++) {
                    weights.setEntry(k, neurons[i + 1][k].getWeight(j));
                }
                delta.setEntry(j, deltas[i + 1].dotProduct(weights) * activationPrime.getEntry(j));
            }
            deltas[i] = delta;
        }
        return deltas;
    }

    public RealVector[] backpropagateNoCost(RealVector[] weightedInputs, RealVector[] activations) {
        int amountOfLayers = weightedInputs.length;
        RealVector[] deltas = new RealVector[amountOfLayers];
        deltas[amountOfLayers - 1] = weightedInputs[amountOfLayers - 1].map(activationFunction);
        RealVector delta;
        RealVector activationPrime;
        for (int i = amountOfLayers - 2; i >= 0; i--) {
            delta = new ArrayRealVector(neurons[i].length);
            activationPrime = weightedInputs[i].map(activationFunction);
            for (int j = 0; j < neurons[i].length; j++) {
                RealVector weights = new ArrayRealVector(neurons[i + 1].length);
                for (int k = 0; k < neurons[i + 1].length; k++) {
                    weights.setEntry(k, neurons[i + 1][k].getWeight(j));
                }
                delta.setEntry(j, deltas[i + 1].dotProduct(weights) * activationPrime.getEntry(j));
            }
            deltas[i] = delta;
        }
        return deltas;
    }

    public void gradientDescent(double eta, double lambda, Pair<RealVector, RealVector> example) {
        List<Pair<RealVector, RealVector>> examples = new LinkedList<>();
        examples.add(example);
        gradientDescent(eta, lambda, examples, 1);
    }

    public void gradientDescent(double eta, double lambda, List<Pair<RealVector, RealVector>> examples, int n) {
        int amountOfLayers = neurons.length;
        int m = examples.size();
        RealVector[] bias = new RealVector[amountOfLayers];
        RealMatrix[] weights = new RealMatrix[amountOfLayers];
        for (int i = 1; i < amountOfLayers; i++) {
            bias[i] = new ArrayRealVector(neurons[i].length);
            weights[i] = MatrixUtils.createRealMatrix(neurons[i].length, neurons[i - 1].length);
        }
        for (Pair<RealVector, RealVector> example : examples) {
            Pair<RealVector[], RealVector[]> temp = feedForward(example.getKey());
            RealVector[] weightedInputs = temp.getKey();
            RealVector[] activations = temp.getValue();
            RealVector[] deltas = backpropagate(weightedInputs, activations, example.getValue());
            for (int i = 1; i < amountOfLayers; i++) {
                bias[i] = bias[i].add(deltas[i]);
                weights[i] = weights[i].add(deltas[i].outerProduct(activations[i - 1]));
            }
        }
        double factor = eta / m;
        RealMatrix currentWeights;
        RealVector currentBias;
        for (int i = 1; i < amountOfLayers; i++) {
            currentWeights = MatrixUtils.createRealMatrix(neurons[i].length, neurons[i - 1].length);
            weights[i] = weights[i].scalarMultiply(factor);
            bias[i].mapMultiplyToSelf(factor);
            currentBias = new ArrayRealVector(neurons[i].length);
            for (int j = 0; j < neurons[i].length; j++) {
                currentWeights.setRowVector(j, neurons[i][j].getWeights());
                currentBias.setEntry(j, neurons[i][j].getBias());
            }
            //L2-regularization
            currentWeights = currentWeights.scalarMultiply((1 - lambda * eta / n));
            currentWeights = currentWeights.subtract(weights[i]);
            currentBias = currentBias.subtract(bias[i]);
            for (int j = 0; j < neurons[i].length; j++) {
                neurons[i][j].setWeights(currentWeights.getRowVector(j));
                neurons[i][j].setBias(currentBias.getEntry(j));
            }
        }
    }

    public void onlineEligibityTraceGD(RealVector input, double vs, double vsPrime, double tdError, double alpha, double gamma, double lambda, RealVector[] biasEligibilityTraces, RealMatrix[] weightEligibilityTraces) {
        int amountOfLayers = neurons.length;
        RealVector[] biasGradient = new RealVector[amountOfLayers];
        RealMatrix[] weightGradients = new RealMatrix[amountOfLayers];
        for (int i = 1; i < amountOfLayers; i++) {
            biasGradient[i] = new ArrayRealVector(neurons[i].length);
            weightGradients[i] = MatrixUtils.createRealMatrix(neurons[i].length, neurons[i - 1].length);
        }
        Pair<RealVector[], RealVector[]> temp = feedForward(input);
        RealVector[] weightedInputs = temp.getKey();
        RealVector[] activations = temp.getValue();
        RealVector[] deltas = backpropagateNoCost(weightedInputs, activations);
        for (int i = 1; i < amountOfLayers; i++) {
            biasGradient[i] = biasGradient[i].add(deltas[i]);
            weightGradients[i] = weightGradients[i].add(deltas[i].outerProduct(activations[i - 1]));
            biasEligibilityTraces[i].mapMultiplyToSelf(lambda * gamma);
            biasEligibilityTraces[i] = biasEligibilityTraces[i].add(biasGradient[i]);
            weightEligibilityTraces[i] = weightEligibilityTraces[i].scalarMultiply(lambda * gamma);
            weightEligibilityTraces[i] = weightEligibilityTraces[i].add(weightGradients[i]);
        }

        RealMatrix currentWeights;
        RealVector currentBias;
        for (int i = 1; i < amountOfLayers; i++) {
            currentWeights = MatrixUtils.createRealMatrix(neurons[i].length, neurons[i - 1].length);
            currentBias = new ArrayRealVector(neurons[i].length);
            for (int j = 0; j < neurons[i].length; j++) {
                currentWeights.setRowVector(j, neurons[i][j].getWeights());
                currentBias.setEntry(j, neurons[i][j].getBias());
            }
            currentWeights = currentWeights.add(weightEligibilityTraces[i].scalarMultiply(alpha * tdError));
            currentBias = currentBias.add(biasEligibilityTraces[i].mapMultiply(alpha * tdError));
            for (int j = 0; j < neurons[i].length; j++) {
                neurons[i][j].setWeights(currentWeights.getRowVector(j));
                neurons[i][j].setBias(currentBias.getEntry(j));
            }
        }
    }

    public void stochasticGradientDescent(double eta, double lambda, int epochs, List<Pair<RealVector, RealVector>> examples, List<Pair<RealVector, RealVector>> testData, double etaFactor) {
        int n = examples.size();
        for (int k = 1; k <= epochs; k++) {
            Collections.shuffle(examples);
            List<Pair<RealVector, RealVector>> batch = new ArrayList<>();
            for (int i = 0; i < n / batchSize; i++) {
                batch.clear();
                for (int j = 0; j < batchSize; j++) {
                    batch.add(examples.get(i * batchSize + j));
                }
                gradientDescent(eta, lambda, batch, n);
            }
            if (testData != null) {
                int correct = test(testData);
                System.out.println("Epoch " + k + "/" + epochs + ": " + correct + "/" + testData.size());
            }
            eta *= etaFactor;
        }
    }

    public int test(List<Pair<RealVector, RealVector>> examples) {
        int correctAnswers = 0;
        RealVector output;
        for (Pair<RealVector, RealVector> example : examples) {
            output = feedForward(example.getKey()).getValue()[neurons.length - 1];
            if (example.getValue().getMaxIndex() == output.getMaxIndex()) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    @Override
    public double evaluate(RealVector state) {
        Pair<RealVector[], RealVector[]> output = feedForward(state);
        return output.getValue()[neurons.length - 1].getEntry(0);
    }

    @Override
    public void changeStateValue(RealVector state, double vs, double vsPrime, double tdError, double learningRate, double gamma, double lambda, RealVector[] biasEligibilityTraces, RealMatrix[] weightEligibilityTraces) {
        onlineEligibityTraceGD(state, vs, vsPrime, tdError, learningRate, gamma, lambda, biasEligibilityTraces, weightEligibilityTraces);
    }

    @Override
    public void changeStateValue(RealVector state, double newValue, double eta, double weightDecay) {
        RealVector newValueVector = new ArrayRealVector(1);
        newValueVector.setEntry(0, newValue);
        savedExamples.add(new Pair<>(state, newValueVector));
        if (savedExamples.size() >= batchSize) {
            gradientDescent(eta, weightDecay, savedExamples, savedExamples.size());
            savedExamples.clear();
        }
    }
}

package tetris.ai;

import java.util.Random;

/**
 * A small neural network with one hidden layer. Every node adds a fixed bias to its weighted inputs and squashes
 * the result with tanh. The weights live in the network's {@link DNA}.
 */
final class Perceptron {
    private static final double BIAS = 1.0;

    private final DNA dna;

    Perceptron(DNA dna) {
        this.dna = dna;
    }

    DNA getDna() {
        return dna;
    }

    /** Feeds a column of inputs through the network and returns the value of its first output node. */
    double evaluate(Matrix inputs) {
        Matrix hidden = activate(dna.getHiddenWeights().multiply(inputs));
        Matrix output = activate(dna.getOutputWeights().multiply(hidden));
        return output.get(0, 0);
    }

    private static Matrix activate(Matrix weightedSums) {
        for (int row = 0; row < weightedSums.getRows(); row++) {
            for (int column = 0; column < weightedSums.getColumns(); column++) {
                weightedSums.set(row, column, Math.tanh(weightedSums.get(row, column) + BIAS));
            }
        }
        return weightedSums;
    }

    Perceptron crossover(Perceptron other, Random random) {
        return new Perceptron(dna.crossover(other.dna, random));
    }

    void mutate(Random random) {
        dna.mutate(random);
    }
}

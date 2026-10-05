package tetris.ai;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * The weights of a network, together with the genetic operations that breed and vary them.
 *
 * <p>Each row of a weight matrix holds the incoming weights of one node, so breeding and mutation work a whole
 * node at a time.
 */
final class DNA {
    private static final double MUTATION_RATE = 0.15;

    /** Out of 100 nodes in a child, how many come from the parent that {@link #crossover} is called on. */
    private static final int PERCENT_FROM_FIRST_PARENT = 90;

    /** One row per hidden node, one column per input. */
    private final Matrix hiddenWeights;

    /** One row per output node, one column per hidden node. */
    private final Matrix outputWeights;

    private DNA(Matrix hiddenWeights, Matrix outputWeights) {
        this.hiddenWeights = hiddenWeights;
        this.outputWeights = outputWeights;
    }

    /** Creates random weights for a network with the given number of nodes in each layer. */
    static DNA random(int inputNodes, int hiddenNodes, int outputNodes, Random random) {
        DNA dna = new DNA(new Matrix(hiddenNodes, inputNodes), new Matrix(outputNodes, hiddenNodes));
        dna.hiddenWeights.randomize(random);
        dna.outputWeights.randomize(random);
        return dna;
    }

    Matrix getHiddenWeights() {
        return hiddenWeights;
    }

    Matrix getOutputWeights() {
        return outputWeights;
    }

    /**
     * Breeds a child that takes each node's weights from either this parent or the other one. The same choice of
     * parent per row is used for both layers.
     */
    DNA crossover(DNA other, Random random) {
        boolean[] fromThis = new boolean[hiddenWeights.getRows()];
        for (int row = 0; row < fromThis.length; row++) {
            fromThis[row] = random.nextInt(100) < PERCENT_FROM_FIRST_PARENT;
        }
        return new DNA(
                mixRows(hiddenWeights, other.hiddenWeights, fromThis),
                mixRows(outputWeights, other.outputWeights, fromThis));
    }

    private static Matrix mixRows(Matrix first, Matrix second, boolean[] fromFirst) {
        Matrix child = new Matrix(first.getRows(), first.getColumns());
        for (int row = 0; row < child.getRows(); row++) {
            Matrix parent = fromFirst[row] ? first : second;
            for (int column = 0; column < child.getColumns(); column++) {
                child.set(row, column, parent.get(row, column));
            }
        }
        return child;
    }

    /** Randomly picks nodes and replaces all of a picked node's weights with a single new random value. */
    void mutate(Random random) {
        mutateRows(hiddenWeights, random);
        mutateRows(outputWeights, random);
    }

    private static void mutateRows(Matrix weights, Random random) {
        for (int row = 0; row < weights.getRows(); row++) {
            double replacement = random.nextGaussian();
            boolean mutated = random.nextInt(100) / 100.0 <= MUTATION_RATE;
            if (mutated) {
                for (int column = 0; column < weights.getColumns(); column++) {
                    weights.set(row, column, replacement);
                }
            }
        }
    }

    /** Saves the weights as text: the hidden weights one row per line, a gap, then the output weights. */
    void write(Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writeRows(hiddenWeights, writer);
            writer.write("\n\n");
            writeRows(outputWeights, writer);
        }
    }

    private static void writeRows(Matrix weights, BufferedWriter writer) throws IOException {
        for (int row = 0; row < weights.getRows(); row++) {
            for (int column = 0; column < weights.getColumns(); column++) {
                writer.write(weights.get(row, column) + " ");
            }
            writer.write("\n");
        }
    }

    /** Loads weights saved by {@link #write} for a network with the given number of nodes in each layer. */
    static DNA read(Path file, int inputNodes, int hiddenNodes, int outputNodes) throws IOException {
        try (Scanner scanner = new Scanner(file)) {
            scanner.useLocale(Locale.ROOT);
            Matrix hiddenWeights = readRows(scanner, hiddenNodes, inputNodes);
            Matrix outputWeights = readRows(scanner, outputNodes, hiddenNodes);
            return new DNA(hiddenWeights, outputWeights);
        }
    }

    private static Matrix readRows(Scanner scanner, int rows, int columns) {
        Matrix weights = new Matrix(rows, columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                weights.set(row, column, scanner.nextDouble());
            }
        }
        return weights;
    }
}

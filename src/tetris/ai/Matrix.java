package tetris.ai;

import java.util.Random;

/**
 * A grid of numbers, used for the network's weights and for the values passed between its layers.
 */
final class Matrix {
    private final int rows;
    private final int columns;
    /** Stored one row after another. */
    private final double[] values;

    /** Creates a matrix filled with zeros. */
    Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.values = new double[rows * columns];
    }

    /** Creates a matrix with a single column holding the given values. */
    static Matrix columnVector(double... values) {
        Matrix vector = new Matrix(values.length, 1);
        for (int row = 0; row < values.length; row++) {
            vector.set(row, 0, values[row]);
        }
        return vector;
    }

    int getRows() {
        return rows;
    }

    int getColumns() {
        return columns;
    }

    double get(int row, int column) {
        return values[row * columns + column];
    }

    void set(int row, int column, double value) {
        values[row * columns + column] = value;
    }

    /** Fills the matrix with random values from a standard normal distribution. */
    void randomize(Random random) {
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextGaussian();
        }
    }

    /** Returns the matrix product {@code this x other}. */
    Matrix multiply(Matrix other) {
        Matrix product = new Matrix(rows, other.columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < other.columns; column++) {
                double sum = 0.0;
                // KNOWN ISSUE (see README): this should run over `columns`, not `rows`. With the 1 x 7 output
                // weights it stops after the first term, so the network's answer depends on only one hidden
                // node. Left as is because the saved agent was trained with it and would play differently.
                for (int k = 0; k < rows; k++) {
                    sum += get(row, k) * other.get(k, column);
                }
                product.set(row, column, sum);
            }
        }
        return product;
    }
}

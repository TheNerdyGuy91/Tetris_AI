package tetris.game;

/**
 * A piece in play: one of the {@link TetrominoType} shapes in its current orientation.
 */
public final class Tetromino {
    private final TetrominoType type;
    private int[][] cells;

    public Tetromino(TetrominoType type) {
        this.type = type;
        this.cells = type.createCells();
    }

    public int getHeight() {
        return cells.length;
    }

    public int getWidth() {
        return cells[0].length;
    }

    /** Whether the piece fills the given cell of its own bounding box. */
    public boolean isFilledAt(int row, int col) {
        return cells[row][col] != 0;
    }

    public int getId() {
        return type.getId();
    }

    public int getDistinctRotations() {
        return type.getDistinctRotations();
    }

    /** Turns the piece a quarter turn clockwise. */
    public void rotate() {
        int height = getHeight();
        int width = getWidth();
        int[][] rotated = new int[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                rotated[col][height - 1 - row] = cells[row][col];
            }
        }
        cells = rotated;
    }
}

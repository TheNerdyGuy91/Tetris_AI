package tetris.game;

/**
 * The seven tetromino shapes, each drawn as rows of text where {@code #} is a filled cell.
 */
public enum TetrominoType {
    I(2, "#",
         "#",
         "#",
         "#"),

    J(4, ".#",
         ".#",
         "##"),

    L(4, "#.",
         "#.",
         "##"),

    O(1, "##",
         "##"),

    S(2, ".##",
         "##."),

    T(4, ".#.",
         "###"),

    Z(2, "##.",
         ".##");

    private final int distinctRotations;
    private final String[] rows;

    TetrominoType(int distinctRotations, String... rows) {
        this.distinctRotations = distinctRotations;
        this.rows = rows;
    }

    /** The value stored in every board cell this shape fills: 1 to 7, leaving 0 for an empty cell. */
    public int getId() {
        return ordinal() + 1;
    }

    /** How many quarter turns it takes before the shape looks the same as when it started. */
    public int getDistinctRotations() {
        return distinctRotations;
    }

    /** A fresh copy of the shape in its starting orientation, as a grid of 0 (empty) and {@link #getId()}. */
    int[][] createCells() {
        int[][] cells = new int[rows.length][rows[0].length()];
        for (int row = 0; row < rows.length; row++) {
            for (int col = 0; col < rows[row].length(); col++) {
                if (rows[row].charAt(col) == '#') {
                    cells[row][col] = getId();
                }
            }
        }
        return cells;
    }
}

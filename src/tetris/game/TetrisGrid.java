package tetris.game;

/**
 * The playing field. Row 0 is the top. Each cell is either empty or holds the id of the tetromino that filled it.
 *
 * <p>Positions passed to the piece methods are the column ({@code x}) and row ({@code y}) of the top-left corner
 * of the piece's bounding box.
 */
public final class TetrisGrid {
    public static final int HEIGHT = 24;
    public static final int WIDTH = 10;

    private static final int EMPTY = 0;

    private final int[][] cells = new int[HEIGHT][WIDTH];

    /** The id of the tetromino filling this cell, or 0 if it is empty. */
    public int getCellAt(int row, int col) {
        return cells[row][col];
    }

    public boolean isEmptyAt(int row, int col) {
        return cells[row][col] == EMPTY;
    }

    /** Whether the piece at this position would stick out of the board or overlap a filled cell. */
    public boolean collides(Tetromino piece, int x, int y) {
        for (int row = 0; row < piece.getHeight(); row++) {
            for (int col = 0; col < piece.getWidth(); col++) {
                if (!piece.isFilledAt(row, col)) {
                    continue;
                }
                int boardRow = y + row;
                int boardCol = x + col;
                boolean outOfBounds = boardRow < 0 || boardRow >= HEIGHT || boardCol < 0 || boardCol >= WIDTH;
                if (outOfBounds || !isEmptyAt(boardRow, boardCol)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Writes the piece onto the board. Cells that are already filled keep what they had. */
    public void place(Tetromino piece, int x, int y) {
        for (int row = 0; row < piece.getHeight(); row++) {
            for (int col = 0; col < piece.getWidth(); col++) {
                if (piece.isFilledAt(row, col) && isEmptyAt(y + row, x + col)) {
                    cells[y + row][x + col] = piece.getId();
                }
            }
        }
    }

    /** Empties every cell the piece covers at this position. */
    public void remove(Tetromino piece, int x, int y) {
        for (int row = 0; row < piece.getHeight(); row++) {
            for (int col = 0; col < piece.getWidth(); col++) {
                if (piece.isFilledAt(row, col)) {
                    cells[y + row][x + col] = EMPTY;
                }
            }
        }
    }

    /**
     * Removes every full line, dropping the rows above it down by one.
     *
     * @return how many lines were removed
     */
    public int clearFullLines() {
        int cleared = 0;
        int row = HEIGHT - 1;
        while (row > 0) {
            if (isFull(row)) {
                dropRowsAbove(row);
                cleared++;
            } else {
                row--;
            }
        }
        return cleared;
    }

    private boolean isFull(int row) {
        for (int col = 0; col < WIDTH; col++) {
            if (isEmptyAt(row, col)) {
                return false;
            }
        }
        return true;
    }

    // KNOWN ISSUE (see README): the top row is copied down but never emptied, and is never checked for being
    // full. Left as is so that games play out exactly as they did before the refactor.
    private void dropRowsAbove(int fullRow) {
        for (int row = fullRow; row > 0; row--) {
            cells[row] = cells[row - 1].clone();
        }
    }
}

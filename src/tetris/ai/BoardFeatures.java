package tetris.ai;

import tetris.game.TetrisGrid;

/**
 * Describes a board with the handful of numbers the network looks at when it judges a move.
 */
final class BoardFeatures {
    /** How many numbers {@link #of} produces, which is also the number of inputs the network needs. */
    static final int COUNT = 7;

    private BoardFeatures() {
    }

    /** Measures the board and returns its features as a column, each scaled by a hand-picked factor. */
    static Matrix of(TetrisGrid grid) {
        int[] heights = columnHeights(grid);
        return Matrix.columnVector(
                totalHeight(heights) / 100000.0,
                holes(grid, heights),
                bumpiness(heights) / 1000.0,
                emptyColumns(heights) / 2000.0,
                completedLines(grid) * 1000.0,
                rowTransitions(grid) / 10.0,
                columnTransitions(grid) / 10.0);
    }

    /** The height of each column, measured from the floor up to its highest filled cell. */
    private static int[] columnHeights(TetrisGrid grid) {
        int[] heights = new int[TetrisGrid.WIDTH];
        for (int col = 0; col < TetrisGrid.WIDTH; col++) {
            int row = 0;
            while (row < TetrisGrid.HEIGHT && grid.isEmptyAt(row, col)) {
                row++;
            }
            heights[col] = TetrisGrid.HEIGHT - row;
        }
        return heights;
    }

    private static int totalHeight(int[] heights) {
        int total = 0;
        for (int height : heights) {
            total += height;
        }
        return total;
    }

    /** Empty cells that have a filled cell somewhere above them in the same column. */
    private static int holes(TetrisGrid grid, int[] heights) {
        int holes = 0;
        for (int col = 0; col < TetrisGrid.WIDTH; col++) {
            for (int row = TetrisGrid.HEIGHT - heights[col]; row < TetrisGrid.HEIGHT; row++) {
                if (grid.isEmptyAt(row, col)) {
                    holes++;
                }
            }
        }
        return holes;
    }

    /** How uneven the surface is: the height differences between neighbouring columns, added up. */
    private static int bumpiness(int[] heights) {
        int bumpiness = 0;
        for (int col = 1; col < heights.length; col++) {
            bumpiness += Math.abs(heights[col - 1] - heights[col]);
        }
        return bumpiness;
    }

    private static int emptyColumns(int[] heights) {
        int empty = 0;
        for (int height : heights) {
            if (height == 0) {
                empty++;
            }
        }
        return empty;
    }

    // KNOWN ISSUE (see README): `complete` is never set back to true, so after the first unfinished row, which is
    // normally the top one, nothing more is counted and this returns 0. Left as is because the saved agent was
    // trained with it and would play differently.
    private static int completedLines(TetrisGrid grid) {
        boolean complete = true;
        int lines = 0;
        for (int row = 0; row < TetrisGrid.HEIGHT; row++) {
            for (int col = 0; col < TetrisGrid.WIDTH; col++) {
                if (grid.isEmptyAt(row, col)) {
                    complete = false;
                    break;
                }
            }
            if (complete) {
                lines++;
            }
        }
        return lines;
    }

    /** Places along the rows where a filled cell sits next to an empty one. */
    private static int rowTransitions(TetrisGrid grid) {
        int transitions = 0;
        for (int row = 0; row < TetrisGrid.HEIGHT; row++) {
            for (int col = 0; col < TetrisGrid.WIDTH - 1; col++) {
                if (grid.isEmptyAt(row, col) != grid.isEmptyAt(row, col + 1)) {
                    transitions++;
                }
            }
        }
        return transitions;
    }

    /** Places down the columns where a filled cell sits directly above or below an empty one. */
    private static int columnTransitions(TetrisGrid grid) {
        int transitions = 0;
        for (int row = 0; row < TetrisGrid.HEIGHT - 1; row++) {
            for (int col = 0; col < TetrisGrid.WIDTH; col++) {
                if (grid.isEmptyAt(row, col) != grid.isEmptyAt(row + 1, col)) {
                    transitions++;
                }
            }
        }
        return transitions;
    }
}

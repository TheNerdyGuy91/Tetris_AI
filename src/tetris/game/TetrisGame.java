package tetris.game;

import java.util.Arrays;
import java.util.Random;

/**
 * One game of Tetris: the board, the piece in play, the queue of upcoming pieces and the score.
 *
 * <p>The game does not move pieces on its own. A player calls {@link #spawnNextPiece()}, tries positions for the
 * piece with {@link #collides}, {@link #placePiece} and {@link #removePiece}, and finishes the turn by leaving
 * the piece placed and calling {@link #clearLines()}.
 */
public final class TetrisGame {
    /** The column where the left edge of every new piece starts, at row 0. */
    public static final int SPAWN_X = TetrisGrid.WIDTH / 2;

    /** Points for clearing 0, 1, 2, 3 or 4 lines with one piece. */
    private static final long[] POINTS_FOR_LINES = {0, 100, 300, 500, 800};

    private static final TetrominoType[] TYPES = TetrominoType.values();

    private final TetrisGrid grid = new TetrisGrid();
    private final Random random;

    private final boolean[] drawnFromBag = new boolean[TYPES.length];
    private int leftInBag = TYPES.length;
    private TetrominoType nextType;
    private Tetromino piece;

    /** How many times a single piece cleared exactly this many lines. */
    private final int[] clearsByLineCount = new int[POINTS_FOR_LINES.length];
    private int piecesDrawn;
    private long score;

    public TetrisGame(Random random) {
        this.random = random;
        this.nextType = drawFromBag();
    }

    /**
     * Brings the next piece into play at the spawn position.
     *
     * @return false if there is no room for it, which means the game is over
     */
    public boolean spawnNextPiece() {
        piece = new Tetromino(nextType);
        nextType = drawFromBag();
        return !collides(SPAWN_X, 0);
    }

    /** Picks a random shape that has not come up yet in the current bag, refilling the bag when it runs out. */
    private TetrominoType drawFromBag() {
        if (leftInBag == 0) {
            Arrays.fill(drawnFromBag, false);
            // KNOWN ISSUE (see README): a refilled bag is one piece short, so after the first seven pieces one
            // shape is skipped in every round. Left as is so that games play out exactly as before the refactor.
            leftInBag = TYPES.length - 1;
        }
        int index = random.nextInt(TYPES.length);
        while (drawnFromBag[index]) {
            index = random.nextInt(TYPES.length);
        }
        drawnFromBag[index] = true;
        leftInBag--;
        piecesDrawn++;
        return TYPES[index];
    }

    public TetrisGrid getGrid() {
        return grid;
    }

    public Tetromino getPiece() {
        return piece;
    }

    public boolean collides(int x, int y) {
        return grid.collides(piece, x, y);
    }

    public void placePiece(int x, int y) {
        grid.place(piece, x, y);
    }

    public void removePiece(int x, int y) {
        grid.remove(piece, x, y);
    }

    public void rotatePiece() {
        piece.rotate();
    }

    /** Clears any full lines and adds the points for them. */
    public void clearLines() {
        int lines = grid.clearFullLines();
        clearsByLineCount[lines]++;
        score += POINTS_FOR_LINES[lines];
    }

    public long getScore() {
        return score;
    }

    /**
     * How well this game went, for ranking agents against each other: one point per piece drawn, plus a bonus for
     * each clear that doubles with every extra line cleared at once (2, 4, 8 or 16).
     */
    public long getFitness() {
        long lineBonus = 0;
        for (int lines = 1; lines < clearsByLineCount.length; lines++) {
            lineBonus += (1L << lines) * clearsByLineCount[lines];
        }
        return lineBonus + piecesDrawn;
    }
}

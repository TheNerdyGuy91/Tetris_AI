package tetris.ai;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import tetris.game.BoardView;
import tetris.game.TetrisGame;
import tetris.game.Tetromino;

/**
 * A Tetris player. For every piece it tries each rotation in each column, lets its network score the board that
 * would result, and plays the move with the highest score.
 */
public final class Agent {
    private static final int INPUT_NODES = BoardFeatures.COUNT;
    private static final int HIDDEN_NODES = 7;
    private static final int OUTPUT_NODES = 1;

    private static final int[] SWEEP_DIRECTIONS = {1, -1};

    private final Perceptron brain;

    /** The game this agent played most recently, kept so its results can be read afterwards. */
    private TetrisGame game;

    private Agent(Perceptron brain) {
        this.brain = brain;
    }

    /** Creates an agent with random weights. */
    public static Agent random(Random random) {
        return new Agent(new Perceptron(DNA.random(INPUT_NODES, HIDDEN_NODES, OUTPUT_NODES, random)));
    }

    /** Creates an agent from weights written by {@link #save}. */
    public static Agent load(Path weightsFile) throws IOException {
        return new Agent(new Perceptron(DNA.read(weightsFile, INPUT_NODES, HIDDEN_NODES, OUTPUT_NODES)));
    }

    public void save(Path weightsFile) throws IOException {
        brain.getDna().write(weightsFile);
    }

    /** Breeds a child whose weights are a mix of this agent's and the other's. */
    public Agent crossover(Agent other, Random random) {
        return new Agent(brain.crossover(other.brain, random));
    }

    public void mutate(Random random) {
        brain.mutate(random);
    }

    /** Plays the game until no more pieces fit, showing every step on the view. */
    public void play(TetrisGame game, BoardView view) {
        this.game = game;
        while (game.spawnNextPiece()) {
            makeMove(findBestMove(), view);
        }
    }

    /** The fitness of the last game this agent played. */
    public long getFitness() {
        return game.getFitness();
    }

    /** The score of the last game this agent played. */
    public long getScore() {
        return game.getScore();
    }

    private Move findBestMove() {
        Tetromino piece = game.getPiece();
        Move best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int rotations = 0; rotations < piece.getDistinctRotations(); rotations++) {
            // Sweep from the spawn column out to the right, then out to the left, as far as the piece can go.
            for (int direction : SWEEP_DIRECTIONS) {
                for (int x = TetrisGame.SPAWN_X; !game.collides(x, 0); x += direction) {
                    double score = scoreDropAt(x);
                    if (score > bestScore) {
                        bestScore = score;
                        best = new Move(rotations, x);
                    }
                }
            }
            game.rotatePiece();
        }
        // The piece has now turned through all of its distinct rotations, so it looks as it did at the start.
        return best;
    }

    /** Scores the board as it would look with the piece dropped straight down column x. */
    private double scoreDropAt(int x) {
        int y = 0;
        while (!game.collides(x, y + 1)) {
            y++;
        }
        game.placePiece(x, y);
        double score = brain.evaluate(BoardFeatures.of(game.getGrid()));
        game.removePiece(x, y);
        return score;
    }

    /** Rotates the piece, slides it across to its column and drops it, one visible step at a time. */
    private void makeMove(Move move, BoardView view) {
        int x = TetrisGame.SPAWN_X;
        int y = 0;

        for (int turn = 0; turn < move.rotations; turn++) {
            showPieceAt(x, y, view);
            game.rotatePiece();
        }
        while (x != move.x) {
            showPieceAt(x, y, view);
            x += move.x > x ? 1 : -1;
        }
        while (!game.collides(x, y + 1)) {
            showPieceAt(x, y, view);
            y++;
        }

        game.placePiece(x, y);
        view.show(game.getGrid());
        game.clearLines();
    }

    /** Draws the piece at a position it is only passing through. */
    private void showPieceAt(int x, int y, BoardView view) {
        game.placePiece(x, y);
        view.show(game.getGrid());
        game.removePiece(x, y);
    }
}

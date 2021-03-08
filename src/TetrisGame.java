import Tetriminos.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class TetrisGame {
    private Tetromino curPiece, nextPiece;
    private int piecesRemain;
    private final int numPieces = 7;
    private boolean piecesHasSpawned[] = new boolean[numPieces];
    private static HashMap<Integer, Long> scorePoints;
    private static HashMap<Integer, Color> gridColor;
    private TetrisGrid grid;
    private Random rand;
    private TetrisGUI board;
    private int []linesScored;
    private int piecesSpawn, linesCompleted; // for fitness
    private long totalScore, totalLinesCompletes, totalSumHeight, totalHoles;

    public TetrisGame() {
        rand = new Random();
        int n = rand.nextInt(numPieces);
        piecesRemain = numPieces - 1;
        grid = new TetrisGrid();
        resetspawns();
        nextPiece = TertriminoSpawn.spawnTetrimino(n);
        piecesHasSpawned[n] = true;
        board = new TetrisGUI();
        piecesSpawn = 1;
        linesScored = new int[]{0, 0, 0, 0, 0};
        totalHoles = 0;
        totalSumHeight = 1;
    }

    private void resetspawns() {
        for (int p = 0; p < numPieces; p++) {
            piecesHasSpawned[p] = false;
        }
    }

    public boolean spawnNextBlock() throws CloneNotSupportedException {
        Random rand = new Random();
        if (piecesRemain == 0) {
            resetspawns();
            piecesRemain = numPieces - 1;
        }
        curPiece = (Tetromino) nextPiece.clone();
        int piece = rand.nextInt(7);
        while (piecesHasSpawned[piece]) {
            piece = rand.nextInt(7);
        }
        piecesHasSpawned[piece] = true;
        nextPiece = TertriminoSpawn.spawnTetrimino(piece);
        piecesRemain--;
        piecesSpawn++;
        return !grid.checkCollision(grid.getWidth() / 2, 0, curPiece.getTetriminoPiece(), false);
    }

    public boolean MergedAt(int posX, int posY) {
        return grid.mergeTetriminoAt(posX, posY, curPiece.getTetriminoPiece());
    }

    public void removeBlockAt(int posX, int posY) {
        grid.removeTetriminoAt(posX, posY, curPiece.getTetriminoPiece());
    }

    public void displayboard() {
        grid.displayBoard();
    }

    public Matrix getHeuristic() {
        return grid.getHeuristic();
    }

    public void rotatecurrentBlock() {
        curPiece.rotatePiece();
    }

    public Tetromino getCurPiece() {
        return curPiece;
    }

    public TetrisGrid getGrid() {
        return grid;
    }

    public boolean checkCollision(int posX, int posY, boolean isXpos) {
        return grid.checkCollision(posX, posY, curPiece.getTetriminoPiece(), isXpos);
    }

    public void scoreIt() {

        int value = grid.removeLines();
        linesScored[value] += 1;

        totalScore += scorePoints.get(value);
    }

    public long calculateFitness()
    {

        return (totalLinesCompletes + getLinesMultiplier() + piecesSpawn - getTotalHoles()) /getSumHeight();
    }

    public static void setupScoring() {
        scorePoints = new HashMap<Integer, Long>();
        scorePoints.put(0, 0L);
        scorePoints.put(1, 100L);
        scorePoints.put(2, 300L);
        scorePoints.put(3, 500L);
        scorePoints.put(4, 800L);

    }

    public long getTotalScore() {
        return totalScore;
    }

    public static void setupColorMap() {
        gridColor = new HashMap<Integer, Color>();
        gridColor.put(0, Color.black);
        gridColor.put(1, Color.CYAN);
        gridColor.put(2, Color.orange);
        gridColor.put(3, Color.MAGENTA);
        gridColor.put(4, Color.PINK);
        gridColor.put(5, Color.GREEN);
        gridColor.put(6, Color.BLUE);
        gridColor.put(7, Color.RED);
    }

    public static Color getColor(int num) {
        return gridColor.get(num);
    }

    public void updateBoard() {
        board.updateMap(grid);
    }
    public int getLinesMultiplier()
    {
        int sum = 0;
        for (int i = 1; i < 5; i++)
        {
            sum = sum + (int)Math.pow(2.0, (double)i) * linesScored[i];
        }
        return sum;
    }
    public void aggregateHoles()
    {
        grid.setColumnHeights();
       totalHoles += grid.getHolesHeuristic();
    }
    public void aggregateHeight()
    {
        totalSumHeight += grid.aggregateHeightHeuristic();
    }
    public long getSumHeight()
    {
        return totalSumHeight;
    }
    public long getTotalLinesCompletes()
    {
        return totalLinesCompletes;
    }

    public long getTotalHoles() {
        return totalHoles;
    }

    public void exitGame()
    {
        board.end();
    }
}
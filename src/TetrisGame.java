import Tetriminos.*;

import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class TetrisGame {
    private Tetromino curPiece, nextPiece;
    private int piecesRemain;
    private final int numPieces = 7;
    private boolean piecesHasSpawned[] = new boolean[numPieces];
    private static HashMap<Integer, BigInteger> scorePoints;
    private static  HashMap<Integer, Color>gridColor;
    private TetrisGrid grid;
    private Random rand;
    private BigInteger totalScore = BigInteger.valueOf(0);

    public TetrisGame() {
        rand = new Random();
        int n = rand.nextInt(numPieces);
        piecesRemain = numPieces - 1;
        grid = new TetrisGrid();
        resetspawns();
        nextPiece = TertriminoSpawn.spawnTetrimino(n);
        piecesHasSpawned[n] = true;

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
        return !grid.checkCollision(grid.getWidth() / 2, 0, curPiece.getTetriminoPiece(), false);
    }

    public void MergedAt(int posX, int posY)
    {
        grid.mergeTetriminoAt(posX, posY, curPiece.getTetriminoPiece());
    }
    public void removeBlockAt(int posX, int posY)
    {
        grid.removeTetriminoAt(posX, posY, curPiece.getTetriminoPiece());
    }
    public void displayboard()
    {
        grid.displayBoard();
    }
    public Matrix getHeuristic()
    {
        return grid.getHeuristic();
    }
    public void rotatecurrentBlock()
    {
        curPiece.rotatePiece();
    }

    public Tetromino getCurPiece() {
        return curPiece;
    }

    public TetrisGrid getGrid() {
        return grid;
    }
    public boolean checkCollision(int posX, int posY, boolean isXpos)
    {
        return grid.checkCollision(posX, posY, curPiece.getTetriminoPiece(), isXpos);
    }
    public void scoreIt()
    {
       totalScore = totalScore.add(scorePoints.get(grid.removeLines()));
    }
    public static void setupScoring()
    {
        scorePoints = new HashMap<Integer, BigInteger>();
        scorePoints.put(0, BigInteger.valueOf(0));
        scorePoints.put(1, BigInteger.valueOf(400));
        scorePoints.put(2, BigInteger.valueOf(1200));
        scorePoints.put(3, BigInteger.valueOf(1800));

    }

    public BigInteger getTotalScore() {
        return totalScore;
    }
    public static void setupColorMap()
    {
        gridColor = new HashMap<Integer, Color>();
        gridColor.put(0, Color.black);
        gridColor.put(1, Color.CYAN);
        gridColor.put(2, Color.orange);
        gridColor.put(3, Color.MAGENTA);
        gridColor.put(4, Color.YELLOW);
        gridColor.put(5, Color.GREEN);
        gridColor.put(6, Color.BLUE);
        gridColor.put(7, Color.RED);
    }
    public static Color getColor(int num)
    {
        return gridColor.get(num);
    }
}
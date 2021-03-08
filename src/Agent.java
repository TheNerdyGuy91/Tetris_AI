import Tetriminos.*;
import java.util.concurrent.TimeUnit;
public class Agent implements Cloneable
{
    Perceptron brain;
    int fitness;
    TetrisGrid boardCopy;
    double bestHeurScore, currHeurScore;
    Movement movement; // xmovement, rotation
    TetrisGame gameplay;
    int milliseconds;
    boolean stillPlaying;
    public Agent()
    {
        brain = new Perceptron(7, 7, 1);
        fitness = 0;
        boardCopy = new TetrisGrid();
        movement = new Movement();
        bestHeurScore = Double.NEGATIVE_INFINITY;
        milliseconds = 0;
        stillPlaying = true;
    }
    public Agent crossover(Agent parentB)
    {
        Agent child = new Agent();
        Perceptron childBrain = brain.crossOver(parentB.getBrain());
        child.setBrain(childBrain);
        return child;
    }

    public void setBrain(Perceptron brain) {
        this.brain = brain;
    }
    public Perceptron getBrain()
    {
        return brain;
    }
    public void display()
    {
        brain.display();
    }
    public void randomize()
    {
        brain.randomize();
    }
    public void mutate()
    {
        brain.mutate();
    }
    public long getFitness()
    {
        return gameplay.calculateFitness();
    }
    public boolean spawnBlock() throws CloneNotSupportedException {
        return gameplay.spawnNextBlock();
    }
    public void getBestMove() throws CloneNotSupportedException, InterruptedException {
        int i = 1;
        int posX = 0;
        int posY = 0;
            Tetromino tBlock = gameplay.getCurPiece();

            for (int rotation = 0; rotation < tBlock.getRotations(); rotation++)
            {
                calculateMove(rotation, tBlock.getTetriminoPiece(), true);
                calculateMove(rotation, tBlock.getTetriminoPiece(), false);
                tBlock.rotatePiece();
            }
    }
    private void calculateMove(int rotation, int[][] currBlock, boolean toRight) throws CloneNotSupportedException, InterruptedException {
        int direction = (toRight ? 1 : -1), posY = 0, x_i = boardCopy.getWidth() / 2;
        int x_f = x_i;

        while (!boardCopy.checkCollision(x_f, posY, currBlock, true))
        {
            boardCopy = (TetrisGrid) gameplay.getGrid().clone();
            while (!boardCopy.checkCollision(x_f, posY + 1, currBlock, false))
            {
                posY++;

            }
            boardCopy.mergeTetriminoAt(x_f, posY, currBlock);
            TetrisGrid heursmap = (TetrisGrid) boardCopy.clone();
            currHeurScore = brain.feedForward(heursmap.getHeuristic());
            if (currHeurScore > bestHeurScore)
            {
                bestHeurScore = currHeurScore;
                movement.setMovements(x_f - x_i, rotation);
            }
            boardCopy.removeTetriminoAt(x_f, posY, currBlock);
            x_f += direction;
            posY = 0;
        }

    }
    public void makeMove() throws InterruptedException, CloneNotSupportedException {
        int xDir = (movement.getxMovement() > 0 ? 1 : -1);
        int x = 0, y = 0, posX = TetrisGrid.getWidth() / 2, rotation = 0, posY = 0;
        while(rotation < movement.getRotation())
        {
            shiftTetrimino(posX, posY, milliseconds);
            gameplay.rotatecurrentBlock();
            rotation++;
        }
        while (x != movement.getxMovement())
        {
            shiftTetrimino(posX, posY, milliseconds);
            x += xDir;
            posX += xDir;
        }
        while (!gameplay.checkCollision(posX, posY + 1, false))
        {
            posY = y;
            shiftTetrimino(posX, posY, milliseconds);
            y++;
        }
    stillPlaying = gameplay.MergedAt(posX, posY);
        gameplay.updateBoard();
        TimeUnit.MILLISECONDS.sleep(milliseconds);
       gameplay.scoreIt();
        bestHeurScore = Double.NEGATIVE_INFINITY; // reseting for next block

    }
    private void shiftTetrimino(int posX, int posY, int millisecond) throws InterruptedException {

            gameplay.MergedAt(posX, posY);
            gameplay.updateBoard();
            TimeUnit.MILLISECONDS.sleep(millisecond);
            gameplay.removeBlockAt(posX, posY);
    }
    public void play() throws InterruptedException, CloneNotSupportedException {
        gameplay = new TetrisGame();

        while (spawnBlock() && stillPlaying)
        {
            getBestMove();
            makeMove();
        }
        TimeUnit.MILLISECONDS.sleep(milliseconds * 10);
        gameplay.exitGame();
    }
    public long getTotalScore()
    {
        return gameplay.getTotalScore();
    }
    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }
    public void setLayer(Matrix inputLayer, Matrix hiddenLayer)
    {
        brain.setLayer(inputLayer, hiddenLayer);
    }
    public Matrix getInputLayer()
    {
        return brain.getInputLayer();
    }
    public Matrix getHiddenLayer()
    {
        return brain.getHiddenLayer();
    }
    public long getTotalCompletedLines()
    {
        return gameplay.getTotalLinesCompletes();
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
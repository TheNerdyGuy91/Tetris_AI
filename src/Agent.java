import Tetriminos.*;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Agent
{
    Perceptron brain;
    int fitness, completedLines;
    TetrisGrid boardCopy;
    double bestHeurScore, currHeurScore;
    Movement movement; // xmovement, rotation
    TetrisGame gameplay;
    public Agent()
    {
        gameplay = new TetrisGame();
        brain = new Perceptron(5, 5, 1);
        brain.randomize();
        fitness = 0;
        boardCopy = new TetrisGrid();
        movement = new Movement();
        bestHeurScore = Double.NEGATIVE_INFINITY;
        completedLines = 0;
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
    public BigInteger getFitness()
    {
        return gameplay.getTotalScore();
    }
    public boolean spawnBlock() throws CloneNotSupportedException {
        return gameplay.spawnNextBlock();
    }
    public void getBestMove() throws CloneNotSupportedException {
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
    private void calculateMove(int rotation, int[][] currBlock, boolean toRight)
    {
        int direction = (toRight ? 1 : -1), posY = 0, x_i = boardCopy.getWidth() / 2;
        int x_f = x_i;

        while (!boardCopy.checkCollision(x_f + direction, posY, currBlock, true))
        {
            boardCopy = gameplay.getGrid();
            x_f += direction;
            while (!boardCopy.checkCollision(x_f, posY + 1, currBlock, false))
            {
                posY++;

            }
            boardCopy.mergeTetriminoAt(x_f, posY, currBlock);
            currHeurScore = brain.feedForward(boardCopy.getHeuristic());
            if (currHeurScore > bestHeurScore)
            {
                bestHeurScore = currHeurScore;
                movement.setMovements(x_f - x_i, rotation);
            }
            boardCopy.removeTetriminoAt(x_f, posY, currBlock);
            posY = 0;
        }

    }
    public void makeMove() throws InterruptedException
    {
        int xDir = (movement.getxMovement() > 0 ? 1 : -1);
        int x = 0, y = 0, posX = TetrisGrid.getWidth() / 2, rotation = 0, posY = 0;
        while(rotation < movement.getRotation())
        {
            shiftTetrimino(posX, posY, 0);
            gameplay.rotatecurrentBlock();
            rotation++;
        }
        while (x != movement.getxMovement())
        {
            shiftTetrimino(posX, posY, 0);
            x += xDir;
            posX += xDir;
        }
        while (!gameplay.checkCollision(posX, posY + 1, false))
        {
            posY = y;
            shiftTetrimino(posX, posY, 0);
            y++;
        }
        gameplay.MergedAt(posX, posY);
       gameplay.displayboard();
       gameplay.scoreIt();
        System.out.println();
         TimeUnit.SECONDS.sleep(0);
        bestHeurScore = Double.NEGATIVE_INFINITY; // reseting for next block

    }
    public void shiftTetrimino(int posX, int posY, int milliseconds) throws InterruptedException {
        gameplay.MergedAt(posX, posY);
        gameplay.displayboard();
        System.out.println();
        TimeUnit.MILLISECONDS.sleep(milliseconds);
        gameplay.removeBlockAt(posX, posY);
    }
    public void play() throws InterruptedException, CloneNotSupportedException {
        while (spawnBlock())
        {
            getBestMove();
            makeMove();
        }
    }

    public int getCompletedLines() {
        return completedLines;
    }
}
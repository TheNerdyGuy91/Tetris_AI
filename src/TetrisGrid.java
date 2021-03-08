import Tetriminos.Tetromino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TetrisGrid implements Cloneable
{

    private static int height = 24, width = 10;
    private int [][]board;
   private int []columnHeights;
   private int totaCompletedLines;
  private Matrix inputHeuristic;
    TetrisGrid()
    {
        board = new int [height][width];
        columnHeights = new int[width];
        totaCompletedLines = 0;
        clearBoard();
        inputHeuristic = new Matrix();
        inputHeuristic.setMatrix( 7, 1);
    }
    public void clearBoard()
    {

        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                board[h][w] = 0;
                columnHeights[w] = 0;
            }
        }
    }
    public void setColumnHeights()
    {
        int r = 0, c = 0, toBlock = 0;
        while (c <  width)
        {
            while ( r < height && board[r][c] == 0)
            {
                toBlock++;
                r++;
            }
            columnHeights[c] = height - toBlock;
            toBlock = r = 0;
            c++;
        }
    }

    public double aggregateHeightHeuristic()
    {
        int sum = 0;
        for (int w = 0; w < width; w++)
        {
            sum += columnHeights[w];
        }
        return sum / 100000.0;
    }
    public double getCompletedLinesHeuristic()
    {
        boolean isLineComplete = true;
        totaCompletedLines = 0;
        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                if (board[h][w] == 0)
                {
                    isLineComplete = false;
                    break;
                }
            }
            totaCompletedLines += (isLineComplete ? 1 : 0);
        }
        return totaCompletedLines * 1000.0;
    }
    public double getBumpinessHeurstic()
    {
        int bumpiness = 0;
        for (int w = 1; w < width; w++)
        {
            bumpiness += Math.abs(columnHeights[w - 1] - columnHeights[w]);
        }
        return bumpiness / 1000.0;
    }
    public double getHolesHeuristic()
    {
        double holes = 0;
        for (int w = 0; w < width; w++)
        {
            for (int h = height - 1; h >= height - columnHeights[w]; h--)
            {
                holes += (board[h][w] == 0 ? 1 : 0);
            }
        }
        return holes;
    }
    public double getDeepWellsHeuristic()
    {
        int wells = 0;
        for (int w = 0; w < width; w++)
        {
            wells += (columnHeights[w] == 0 ? 1 : 0);
        }
        return wells / 2000.0;
    }
    public double rowTransitionHeuristic()
    {
        int sum = 0;
        for (int r = 0; r < TetrisGrid.getHeight(); r++)
        {
            for (int c = 0; c < TetrisGrid.getWidth() - 1; c++)
            {
                sum += (board[r][c] > 0 && board[r][c + 1] == 0 || board[r][c] == 0
                        && board[r][c + 1] > 0 ? 1 : 0);
            }
        }
        return sum / 10.0;
    }
    public double colTransitionHeuristic()
    {
        int sum = 0;
        for (int r = 0; r < TetrisGrid.getHeight() - 1; r++)
        {
            for (int c = 0; c < TetrisGrid.getWidth(); c++)
            {
                sum += (board[r][c] > 0 && board[r + 1][c] == 0 || board[r][c] == 0
                        && board[r + 1][c] > 0 ? 1 : 0);
            }
        }
        return sum / 10.0;
    }
    public boolean checkCollision(int posX, int posY, int [][]currentPiece, boolean ischeckX)
    {
        boolean outOfBounds;
        for (int y = 0; y < currentPiece.length; y++)
        {
            for (int x = 0; x < currentPiece[0].length; x++)
            {
               outOfBounds = (ischeckX ? (x + posX < 0 || x + posX >= width) :
                             (posY + y < 0 || posY + y >= height));
                if (outOfBounds && currentPiece[y][x] > 0) //out of bounds
                {
                    return true;
                }

                if (currentPiece[y][x] > 0 && board[y + posY][x + posX] > 0) // pieces collided
                {
                    return true;
                }
            }
        }

        return false;
    }
    public boolean mergeTetriminoAt(int posX, int posY, int [][] currPiece)
    {
        for (int y = 0; y < currPiece.length; y++)
        {
            for (int x = 0; x < currPiece[0].length; x++)
            {
                if (x + posX >= width)
                {
                    return false;
                }
                board[y + posY][posX + x] = (board[y + posY][posX + x] == 0 ? currPiece[y][x] :
                        board[y + posY][posX + x]);
            }
        }
        return true;
    }
    public void removeTetriminoAt(int posX, int posY, int [][] currPiece)
    {
        for (int y = 0; y < currPiece.length; y++)
        {
            for (int x = 0; x < currPiece[0].length; x++)
            {
                board[y + posY][x + posX] = (currPiece[y][x] > 0 ? 0 : board[y + posY][x + posX]);
            }
        }
    }
    public Matrix getHeuristic()
    {
        setColumnHeights(); // to update

        inputHeuristic.setValueAt(aggregateHeightHeuristic() , 0, 0);
        inputHeuristic.setValueAt(getHolesHeuristic() , 1, 0);
        inputHeuristic.setValueAt(getBumpinessHeurstic(), 2, 0);
        inputHeuristic.setValueAt(getDeepWellsHeuristic(), 3, 0);
        inputHeuristic.setValueAt(getCompletedLinesHeuristic(), 4, 0);
        inputHeuristic.setValueAt(rowTransitionHeuristic(), 5, 0);
        inputHeuristic.setValueAt(colTransitionHeuristic(), 6, 0);
        return inputHeuristic;
    }
    public void displayBoard()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                System.out.print(board[y][x]);
            }
            System.out.println();
        }
    }
    public int removeLines() // needs update
    {
        boolean moreLines = true;
        int c = 0, rowUpdate, completedlines = 0;
        while (moreLines)
        {
            moreLines = false;
        for(int r = height - 1; r > 0; r--) {
            rowUpdate = r;
            c = 0;
            while (c < width && board[r][c] != 0) {
                c++;
            }

            if (c == width)
            {
                completedlines++;
                while (rowUpdate > 0) {
                    board[rowUpdate] = board[rowUpdate - 1].clone(); // pass by value not reference
                    rowUpdate--;
                    moreLines = true;
                }
            }
        }
        }
        return completedlines;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
    public void randomizeBoard()
    {
        Random rand = new Random();
       int randRow = 14;
       int randColumn = 1;

        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                board[h][w] = (h >= randRow  && w >= randColumn ? rand.nextInt(5 ) + 1 : 0);
            }
        }
    }
    // for grid display
    public int getCellAt(int row, int col)
    {
        return board[row][col];
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
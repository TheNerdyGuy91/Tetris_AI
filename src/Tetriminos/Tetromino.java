package Tetriminos;/*
A class that will be used as an abstract class
 */

import java.awt.*;
public class Tetromino implements Cloneable
{
    protected int rotations = 4;
    protected int rowSize;
    protected int columnSize;
   protected int tetriminoPiece[][];
    public int[][] getTetriminoPiece() {
        return tetriminoPiece;
    }

    public int getRotations()
    {
        return rotations;
    }
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public int getRowSize() {
        return rowSize;
    }
    public int getColumnSize() {
        return columnSize;
    }
    public void rotatePiece()
    {
        int rotatedRow = columnSize, rotatedColumn = rowSize;
        int[][] rotatedPiece = new int[columnSize][rowSize];
        for (int c = 0; c < rotatedColumn; c++)
        {
            for (int r = 0; r < rotatedRow; r++)
            {
                rotatedPiece[r][rotatedColumn - 1 - c] = tetriminoPiece[c][r];
            }
        }
        tetriminoPiece = rotatedPiece;
        columnSize = rotatedColumn;
        rowSize = rotatedRow;

    }
    public void display()
    {
        for (int i = 0; i < rowSize; i++)
        {
            for (int j = 0; j < columnSize; j++)
            {
                System.out.print(tetriminoPiece[i][j]);
            }
            System.out.println();
        }
    }
}
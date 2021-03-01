package Tetriminos;

import java.awt.*;

public class TetrominoS extends Tetromino
{
    public TetrominoS()
    {

        rowSize = 2;
        columnSize = 3;
        rotations = 2;
        tetriminoPiece = new int[][]
                {{0, 5, 5},
                {5, 5, 0}};
    }
}
package Tetriminos;

import java.awt.*;

public class TetrominoZ extends Tetromino
{
    public TetrominoZ()
    {
        rowSize = 2;
        columnSize = 3;
        rotations = 2;
        tetriminoPiece = new int[][]
                {{7, 7, 0},
                 {0, 7, 7}};
    }
}
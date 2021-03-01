package Tetriminos;

import java.awt.*;

public class TetrominoO extends Tetromino
{
    public TetrominoO()
    {
        rowSize = 2;
        columnSize = 2;
        rotations = 1;

        tetriminoPiece = new int[][]
                {{4, 4},
                 {4, 4}};

    }
}
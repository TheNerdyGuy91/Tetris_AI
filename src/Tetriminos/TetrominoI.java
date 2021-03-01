package Tetriminos;

import java.awt.*;

public class TetrominoI extends Tetromino
{
    public TetrominoI()
    {
        rotations = 2;
        rowSize = 4;
        columnSize = 1;

        tetriminoPiece = new int[][]
                {{1},
                 {1},
                 {1},
                 {1}};

    }
}
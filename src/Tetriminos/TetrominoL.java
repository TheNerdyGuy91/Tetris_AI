package Tetriminos;

import java.awt.*;

public class TetrominoL extends Tetromino
{
    public TetrominoL()
    {
        rowSize = 3;
        columnSize = 2;
        tetriminoPiece = new int[][]
                {{3, 0},
                 {3, 0},
                 {3, 3}};

    }
}
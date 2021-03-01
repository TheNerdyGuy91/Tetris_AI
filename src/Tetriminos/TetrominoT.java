package Tetriminos;

import java.awt.*;

public class TetrominoT extends Tetromino
{
    public TetrominoT()
    {
        rowSize = 2;
        columnSize = 3;
        tetriminoPiece = new int[][]
                {{0, 6, 0},
                {6, 6, 6}};
    }
}
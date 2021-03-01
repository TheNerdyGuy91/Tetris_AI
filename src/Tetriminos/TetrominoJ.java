package Tetriminos;

import java.awt.*;

public class TetrominoJ extends Tetromino
{
    public TetrominoJ()
    {
        rowSize = 3;
        columnSize = 2;
        tetriminoPiece = new int[][]
                {{0, 2},
                 {0, 2},
                 {2, 2}};

    }
}
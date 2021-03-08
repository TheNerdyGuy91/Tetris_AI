import Tetriminos.*;

import java.util.HashMap;

public class TertriminoSpawn
{
    private Tetromino blockPiece;
    private static HashMap <Integer, Tetromino> spawnedBlock;
    public static Tetromino spawnTetrimino(int n)
    {
        return spawnedBlock.get(n);
    }
    public static void setupTetriminos() // hash map
    {
        spawnedBlock = new HashMap<Integer, Tetromino>();
        spawnedBlock.put(0, new TetrominoI());
        spawnedBlock.put(1, new TetrominoJ());
        spawnedBlock.put(2, new TetrominoL());
        spawnedBlock.put(3, new TetrominoO());
        spawnedBlock.put(4, new TetrominoS());
        spawnedBlock.put(5, new TetrominoT());
        spawnedBlock.put(6, new TetrominoZ());
    }
}
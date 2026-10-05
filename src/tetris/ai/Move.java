package tetris.ai;

/**
 * Where to put a piece: how many quarter turns to give it and which column to drop it down.
 */
final class Move {
    final int rotations;
    final int x;

    Move(int rotations, int x) {
        this.rotations = rotations;
        this.x = x;
    }
}

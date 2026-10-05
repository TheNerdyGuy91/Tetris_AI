package tetris.game;

/**
 * Something that can display the board, such as a window. The game logic never depends on a real screen, so a
 * game can also be played with a view that does nothing.
 */
public interface BoardView {
    /** Displays the board as it is right now, returning once it has been on screen long enough to be seen. */
    void show(TetrisGrid grid);
}

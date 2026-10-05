package tetris.ui;

import javax.swing.JFrame;

import tetris.game.BoardView;
import tetris.game.TetrisGrid;

/**
 * A window that shows a game being played. Closing it ends the program.
 */
public final class GameWindow implements BoardView {
    private final JFrame frame = new JFrame("Tetris AI");
    private final BoardPanel boardPanel = new BoardPanel();
    private final int frameDelayMs;

    /**
     * @param frameDelayMs how long each step of the game stays on screen; 0 plays as fast as possible
     */
    public GameWindow(int frameDelayMs) {
        this.frameDelayMs = frameDelayMs;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(boardPanel);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void show(TetrisGrid grid) {
        boardPanel.setGrid(grid);
        if (frameDelayMs > 0) {
            try {
                Thread.sleep(frameDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void close() {
        frame.dispose();
    }
}

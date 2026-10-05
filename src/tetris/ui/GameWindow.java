package tetris.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import tetris.game.BoardView;

/**
 * A window that shows one or more games being played. Closing it ends the program.
 */
public final class GameWindow {
    private static final int LARGE_CELL_SIZE = 30;
    private static final int SMALL_CELL_SIZE = 5;
    private static final int BOARDS_PER_ROW = 20;
    private static final int GAP = 6;
    private static final int REPAINTS_PER_SECOND = 30;
    private static final Color BACKGROUND = new Color(30, 30, 30);

    private final JFrame frame = new JFrame("Tetris AI");
    private final List<BoardView> boards = new ArrayList<>();
    private final Timer repaintTimer;

    /**
     * Opens a window with one large board.
     *
     * @param frameDelayMs how long each step of the game stays on screen; 0 plays as fast as possible
     */
    public static GameWindow forOneGame(int frameDelayMs) {
        return new GameWindow(1, LARGE_CELL_SIZE, true, frameDelayMs);
    }

    /**
     * Opens a window with a grid of small boards, one for each game.
     *
     * @param frameDelayMs how long each step of each game stays on screen; 0 plays as fast as possible
     */
    public static GameWindow forManyGames(int games, int frameDelayMs) {
        return new GameWindow(games, SMALL_CELL_SIZE, false, frameDelayMs);
    }

    private GameWindow(int games, int cellSize, boolean gridLines, int frameDelayMs) {
        JPanel content = new JPanel(new GridLayout(0, Math.min(games, BOARDS_PER_ROW), GAP, GAP));
        content.setBackground(BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        for (int i = 0; i < games; i++) {
            BoardPanel board = new BoardPanel(cellSize, gridLines, frameDelayMs);
            boards.add(board);
            content.add(board);
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);

        // The games run on their own threads and never ask for a repaint. The window redraws itself on a timer.
        repaintTimer = new Timer(1000 / REPAINTS_PER_SECOND, event -> content.repaint());
        repaintTimer.start();
    }

    /** The boards in this window, in reading order. Give each game one of them to be shown on. */
    public List<BoardView> getBoards() {
        return Collections.unmodifiableList(boards);
    }

    public void setTitle(String title) {
        SwingUtilities.invokeLater(() -> frame.setTitle(title));
    }

    public void close() {
        repaintTimer.stop();
        frame.dispose();
    }
}

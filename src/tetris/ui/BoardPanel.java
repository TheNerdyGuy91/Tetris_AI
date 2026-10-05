package tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import tetris.game.BoardView;
import tetris.game.TetrisGrid;

/**
 * Draws one board as a grid of coloured squares.
 *
 * <p>A game runs on its own thread and keeps changing its board, including while it tries out moves. So the panel
 * never draws the live board: {@link #show} takes a copy, and painting works from that copy.
 */
final class BoardPanel extends JPanel implements BoardView {
    /** The colour of each cell value: black for empty, then one colour per tetromino id. */
    private static final Color[] CELL_COLORS = {
        Color.BLACK, Color.CYAN, Color.ORANGE, Color.MAGENTA, Color.PINK, Color.GREEN, Color.BLUE, Color.RED
    };

    private final int cellSize;
    private final boolean gridLines;
    private final int frameDelayMs;

    /** The board as it was when it was last shown. Lock it before reading or writing. */
    private final TetrisGrid snapshot = new TetrisGrid();

    /**
     * @param cellSize     width and height of one cell, in pixels
     * @param gridLines    whether to draw a line between every cell, or only an outline around the board
     * @param frameDelayMs how long {@link #show} waits before returning; 0 does not wait
     */
    BoardPanel(int cellSize, boolean gridLines, int frameDelayMs) {
        this.cellSize = cellSize;
        this.gridLines = gridLines;
        this.frameDelayMs = frameDelayMs;
        setBackground(CELL_COLORS[0]);
        // One extra pixel so the lines along the right and bottom edges are visible.
        setPreferredSize(new Dimension(TetrisGrid.WIDTH * cellSize + 1, TetrisGrid.HEIGHT * cellSize + 1));
    }

    @Override
    public void show(TetrisGrid grid) {
        synchronized (snapshot) {
            snapshot.copyFrom(grid);
        }
        if (frameDelayMs > 0) {
            try {
                Thread.sleep(frameDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int boardWidth = TetrisGrid.WIDTH * cellSize;
        int boardHeight = TetrisGrid.HEIGHT * cellSize;

        synchronized (snapshot) {
            for (int row = 0; row < TetrisGrid.HEIGHT; row++) {
                for (int col = 0; col < TetrisGrid.WIDTH; col++) {
                    if (!snapshot.isEmptyAt(row, col)) {
                        g.setColor(CELL_COLORS[snapshot.getCellAt(row, col)]);
                        g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                    }
                }
            }
        }

        if (gridLines) {
            g.setColor(Color.WHITE);
            for (int row = 0; row <= TetrisGrid.HEIGHT; row++) {
                g.drawLine(0, row * cellSize, boardWidth, row * cellSize);
            }
            for (int col = 0; col <= TetrisGrid.WIDTH; col++) {
                g.drawLine(col * cellSize, 0, col * cellSize, boardHeight);
            }
        } else {
            g.setColor(Color.DARK_GRAY);
            g.drawRect(0, 0, boardWidth, boardHeight);
        }
    }
}

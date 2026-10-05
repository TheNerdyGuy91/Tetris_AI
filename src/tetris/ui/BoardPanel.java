package tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import tetris.game.TetrisGrid;

/**
 * Draws a board as a grid of coloured squares.
 */
final class BoardPanel extends JPanel {
    private static final int CELL_SIZE = 30;

    /** The colour of each cell value: black for empty, then one colour per tetromino id. */
    private static final Color[] CELL_COLORS = {
        Color.BLACK, Color.CYAN, Color.ORANGE, Color.MAGENTA, Color.PINK, Color.GREEN, Color.BLUE, Color.RED
    };

    private volatile TetrisGrid grid = new TetrisGrid();

    BoardPanel() {
        // One extra pixel so the last grid line on the right and bottom edges is visible.
        setPreferredSize(new Dimension(TetrisGrid.WIDTH * CELL_SIZE + 1, TetrisGrid.HEIGHT * CELL_SIZE + 1));
    }

    void setGrid(TetrisGrid grid) {
        this.grid = grid;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TetrisGrid shown = grid;
        int boardWidth = TetrisGrid.WIDTH * CELL_SIZE;
        int boardHeight = TetrisGrid.HEIGHT * CELL_SIZE;

        for (int row = 0; row < TetrisGrid.HEIGHT; row++) {
            for (int col = 0; col < TetrisGrid.WIDTH; col++) {
                g.setColor(CELL_COLORS[shown.getCellAt(row, col)]);
                g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        g.setColor(Color.WHITE);
        for (int row = 0; row <= TetrisGrid.HEIGHT; row++) {
            g.drawLine(0, row * CELL_SIZE, boardWidth, row * CELL_SIZE);
        }
        for (int col = 0; col <= TetrisGrid.WIDTH; col++) {
            g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, boardHeight);
        }
    }
}

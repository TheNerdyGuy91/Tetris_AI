import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class BoardGUI extends JPanel
{
    private TetrisGrid displaygrid = new TetrisGrid();
    private int CellSize = 40;
    private Color [][]color = new Color[][] {{Color.RED, Color.WHITE}, {Color.RED, Color.WHITE}, {Color.RED, Color.WHITE}, {Color.RED, Color.RED}};
    BoardGUI()
    {
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int row = 0; row < TetrisGrid.getWidth(); row++)
        {
            for (int col = 0; col < TetrisGrid.getHeight(); col++)
            {
                g.setColor(TetrisGame.getColor(displaygrid.getCellAt(col, row)));
                g.fillRect(row * CellSize, col * CellSize,  CellSize,  CellSize);
            }
        }
        g.setColor(Color.WHITE);
        for (int rowLine = 0; rowLine < TetrisGrid.getHeight() + 1; rowLine++)
        {
            g.drawLine(0, CellSize * rowLine, CellSize * TetrisGrid.getWidth(), CellSize * rowLine);
        }

        for (int colLine =  0; colLine < TetrisGrid.getWidth() + 1; colLine++)
        {
            g.drawLine( CellSize * colLine, 0, CellSize * colLine, CellSize * TetrisGrid.getHeight() );
        }


    }
    public void updateMap(TetrisGrid newMap)
    {
        displaygrid = newMap;
        repaint();
    }
}
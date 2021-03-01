import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class BoardGUI extends JPanel
{
    private TetrisGrid displaygrid;
    private int CellSize = 32;
    BoardGUI()
    {
        displaygrid = new TetrisGrid();
        displaygrid.clearBoard();
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        //g.setColor(Color.DARK_GRAY);
        g.setColor(Color.WHITE);
        //g.drawRect(0, 0, getWidth() / 10 , TetrisGrid.getHeight() / 10);
        for (int rowLine = 0; rowLine < TetrisGrid.getHeight() + 1; rowLine++)
        {
            g.drawLine(0, CellSize * rowLine, CellSize * TetrisGrid.getWidth(), CellSize * rowLine);
        }
        for (int colLine =  0; colLine < TetrisGrid.getWidth() + 1; colLine++)
        {
            g.drawLine( CellSize * colLine, 0, CellSize * colLine, CellSize * TetrisGrid.getHeight() );
        }

    }

}

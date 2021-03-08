import javax.swing.*;

public class TetrisGUI
{
    private JFrame displayGUI;
    private BoardGUI boardDisplay;
    private int [][]shapeColor;
    public static final int Width = 500, Height = 1200;
    public TetrisGUI()
    {
        shapeColor = new int[TetrisGrid.getHeight()][TetrisGrid.getWidth()];
        displayGUI = new JFrame("Agent");
        displayGUI.setSize(Width, Height);
        displayGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayGUI.setResizable(false);
        boardDisplay = new BoardGUI();
        displayGUI.add(boardDisplay);

        displayGUI.setVisible(true);

    }
    public void updateMap(TetrisGrid grid)
    {
        boardDisplay.updateMap(grid);
    }
    public void end()
    {
        displayGUI.dispose();
    }
}
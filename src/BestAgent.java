import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BestAgent
{
    public static void getLayer(Matrix layer, Scanner sequence)
    {
        for (int row = 0; row < layer.getRow(); row++) {
            for (int col = 0; col < layer.getColumn(); col++)
            {
                layer.setValueAt(sequence.nextDouble(), row, col);
            }
        }
    }


    public static void main(String[] args) throws CloneNotSupportedException, InterruptedException, IOException {
        //  copy the file path and paste it.
        File DNASequence = new File("/Users/benjamin/IdeaProjects/Tetris_AI/src/bestAgent.txt");
        TetrisGame.setupColorMap();
        TertriminoSpawn.setupTetriminos();
        TetrisGame.setupScoring();
        Scanner DNAImprint = new Scanner(DNASequence);
        Matrix input = new Matrix(), hidden = new Matrix();
        input.setMatrix(7, 7);
        hidden.setMatrix(1, 7);
        Agent bestAgent = new Agent();
        getLayer(input, DNAImprint);
        getLayer(hidden, DNAImprint);

        bestAgent.setLayer(input, hidden);
        bestAgent.setMilliseconds(100);
        bestAgent.play();
        System.out.println( "Total Score: " + bestAgent.getTotalScore());




    }
}

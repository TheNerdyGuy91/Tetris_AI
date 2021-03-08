import Tetriminos.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AgentTraining
{
    public static void main(String[] args) throws InterruptedException, CloneNotSupportedException, IOException {
        // important for initial setups for the AI to play
        TetrisGame.setupColorMap();
        TertriminoSpawn.setupTetriminos();
        TetrisGame.setupScoring();
        Agent bestAgent;
        Population p = new Population(100);
        int generation = 0, maxGeneration = 100;
        while (generation < maxGeneration) {
            System.out.println("Generation: " + generation);
            p.compete();
            p.nextGeneration();
            generation++;
        }
        bestAgent = p.getBestCompetitor();
        bestAgent.setMilliseconds(0);
        bestAgent.play();
        System.out.println(bestAgent.getFitness());
        System.out.println(bestAgent.getTotalScore());
        Matrix inputLayer = bestAgent.getInputLayer();
        Matrix hiddenLayer = bestAgent.getHiddenLayer();
        FileWriter DNASequence = new FileWriter("src/bestAgent.txt");
        for (int inputRow = 0; inputRow < inputLayer.getRow(); inputRow++) {
            for (int inputCol = 0; inputCol < inputLayer.getColumn(); inputCol++)
            {
                DNASequence.write(String.valueOf(inputLayer.getDataAt(inputRow, inputCol)) + " ");
            }
            DNASequence.write("\n");
        }
        DNASequence.write("\n\n");
        for (int hiddenRow = 0; hiddenRow < hiddenLayer.getRow(); hiddenRow++)
        {
            for (int hiddenCol = 0; hiddenCol < hiddenLayer.getColumn(); hiddenCol++)
            {
                DNASequence.write(String.valueOf(hiddenLayer.getDataAt(hiddenRow, hiddenCol)) + " ");
            }
            DNASequence.write("\n");
        }
        DNASequence.close();

    }
}
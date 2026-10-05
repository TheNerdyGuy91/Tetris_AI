package tetris;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import tetris.ai.Agent;
import tetris.ai.Population;
import tetris.game.TetrisGame;
import tetris.ui.GameWindow;

/**
 * Trains a population of agents and saves the weights of the best one, ready for {@link BestAgent} to play.
 *
 * <p>Run it from the project folder so that the weights file ends up in the right place.
 */
public final class AgentTraining {
    /** Where the best agent's weights are saved, relative to the folder the program is run from. */
    static final Path WEIGHTS_FILE = Paths.get("data", "bestAgent.txt");

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 100;

    private AgentTraining() {
    }

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        GameWindow window = new GameWindow(0);
        Population population = new Population(POPULATION_SIZE, random);

        for (int generation = 0; generation < GENERATIONS; generation++) {
            System.out.println("Generation: " + generation);
            population.compete(window);
            population.nextGeneration();
        }

        Agent best = population.getBest();
        best.play(new TetrisGame(random), window);
        window.close();

        System.out.println("Best agent fitness: " + best.getFitness());
        System.out.println("Best agent score: " + best.getScore());
        best.save(WEIGHTS_FILE);
    }
}

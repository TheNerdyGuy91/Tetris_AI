package tetris;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import tetris.ai.Agent;
import tetris.ai.Population;
import tetris.ui.GameWindow;

/**
 * Trains a population of agents and saves the weights of the best one, ready for {@link BestAgent} to play.
 *
 * <p>The best agent so far is saved after every generation, so training can be stopped at any time by closing the
 * window. Run it from the project folder so that the weights file ends up in the right place.
 */
public final class AgentTraining {
    /** Where the best agent's weights are saved, relative to the folder the program is run from. */
    static final Path WEIGHTS_FILE = Paths.get("data", "bestAgent.txt");

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 100;

    /**
     * How long each step of each game stays on screen. 0 trains at full speed. A small value such as 2 slows the
     * games down enough to watch, but a generation then takes far longer.
     */
    private static final int FRAME_DELAY_MS = 0;

    private AgentTraining() {
    }

    public static void main(String[] args) throws IOException {
        GameWindow window = GameWindow.forManyGames(POPULATION_SIZE, FRAME_DELAY_MS);
        Population population = new Population(POPULATION_SIZE, new Random());

        for (int generation = 0; generation < GENERATIONS; generation++) {
            System.out.println("Generation: " + generation);
            window.setTitle("Tetris AI - generation " + generation);
            population.compete(window.getBoards());
            population.nextGeneration();
            population.getBest().save(WEIGHTS_FILE);
        }
        window.close();

        Agent best = population.getBest();
        System.out.println("Best agent fitness: " + best.getFitness());
        System.out.println("Best agent score: " + best.getScore());
    }
}

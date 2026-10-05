package tetris.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import tetris.game.BoardView;
import tetris.game.TetrisGame;

/**
 * A group of agents that improves by a genetic algorithm. Each generation every agent plays one game, and the next
 * generation is bred from the two fittest agents seen so far.
 */
public final class Population {
    private final Random random;
    private List<Agent> agents = new ArrayList<>();

    /** The two fittest agents from any generation so far. */
    private Agent best;
    private Agent secondBest;

    /** Creates a population of agents with random weights. */
    public Population(int size, Random random) {
        this.random = random;
        for (int i = 0; i < size; i++) {
            agents.add(Agent.random(random));
        }
    }

    /** Has every agent play one game, showing the games on the view. */
    public void compete(BoardView view) {
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            agent.play(new TetrisGame(random), view);
            System.out.println("Fitness: " + i + " " + agent.getFitness());
        }
    }

    /** Replaces the agents with a new generation. Call after {@link #compete}. */
    public void nextGeneration() {
        agents.sort(Comparator.comparingLong(Agent::getFitness).reversed());
        Agent first = agents.get(0);
        Agent second = agents.get(1);
        System.out.println("Best Score: " + first.getFitness());
        System.out.println("2nd Best Score: " + second.getFitness());

        if (best == null || best.getFitness() < first.getFitness()) {
            if (best != null) {
                secondBest = best;
            }
            best = first;
        }
        if (secondBest == null || secondBest.getFitness() < second.getFitness()) {
            secondBest = second;
        }

        int size = agents.size();
        agents = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Agent child = best.crossover(secondBest, random);
            child.mutate(random);
            agents.add(child);
        }
    }

    /** The fittest agent from any generation so far. */
    public Agent getBest() {
        return best;
    }
}

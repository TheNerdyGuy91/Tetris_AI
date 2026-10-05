package tetris.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tetris.game.BoardView;
import tetris.game.TetrisGame;

/**
 * A group of agents that improves by a genetic algorithm. Each generation every agent plays one game, all at the
 * same time, and the next generation is bred from the two fittest agents seen so far.
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

    /**
     * Has every agent play one game, all at the same time.
     *
     * @param views where to show the games: one view for each agent, in order
     */
    public void compete(List<BoardView> views) {
        List<Callable<Void>> games = new ArrayList<>();
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            BoardView view = views.get(i);
            // Each game gets its own random generator, seeded here in order, so the pieces an agent is dealt do
            // not depend on how the threads happen to be scheduled.
            TetrisGame game = new TetrisGame(new Random(random.nextLong()));
            games.add(() -> {
                agent.play(game, view);
                return null;
            });
        }

        // One thread per agent, so that all the games advance together instead of queueing for a free thread.
        ExecutorService threads = Executors.newFixedThreadPool(agents.size());
        try {
            for (Future<Void> finishedGame : threads.invokeAll(games)) {
                finishedGame.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while the agents were playing", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("An agent's game failed", e.getCause());
        } finally {
            threads.shutdown();
        }

        for (int i = 0; i < agents.size(); i++) {
            System.out.println("Fitness: " + i + " " + agents.get(i).getFitness());
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

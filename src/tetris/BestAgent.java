package tetris;

import java.io.IOException;
import java.util.Random;

import tetris.ai.Agent;
import tetris.game.TetrisGame;
import tetris.ui.GameWindow;

/**
 * Plays one game with the agent saved by {@link AgentTraining}, slowly enough to watch.
 *
 * <p>Run it from the project folder so that the weights file can be found.
 */
public final class BestAgent {
    private static final int MOVE_DELAY_MS = 100;

    private BestAgent() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Agent agent = Agent.load(AgentTraining.WEIGHTS_FILE);
        GameWindow window = new GameWindow(MOVE_DELAY_MS);

        agent.play(new TetrisGame(new Random()), window);

        // Leave the final board up for a moment before the window goes away.
        Thread.sleep(MOVE_DELAY_MS * 10);
        window.close();
        System.out.println("Total Score: " + agent.getScore());
    }
}

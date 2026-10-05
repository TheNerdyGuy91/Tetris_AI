# Tetris AI

A Tetris-playing agent written in Java. A small neural network decides where every piece goes, and a genetic
algorithm trains the network by breeding the best players of each generation.

## How it works

**Choosing a move.** For each new piece the agent tries every rotation in every column, drops the piece there,
and describes the resulting board with seven numbers:

| Feature | What it measures |
| --- | --- |
| Total height | The heights of all columns added together |
| Holes | Empty cells with a filled cell somewhere above them |
| Bumpiness | Height differences between neighbouring columns |
| Empty columns | Columns with nothing in them |
| Completed lines | Full rows on the board |
| Row transitions | Places along a row where a filled cell meets an empty one |
| Column transitions | The same, going down a column |

Those numbers go into a network with 7 inputs, 7 hidden nodes and 1 output. The move whose board gets the highest
output is the one the agent plays.

**Training.** A population of 100 agents starts with random weights. Every generation each agent plays one game and
earns a fitness: one point per piece, plus a bonus for clearing lines that grows with the number cleared at once.
The next generation is bred from the two fittest agents seen so far. Each child takes most of its nodes from the
fitter parent and a few from the other, and then some of its nodes are mutated.

## Running it

The project needs Java 8 or newer and has no other dependencies.

**In IntelliJ:** open the project folder and run one of the two classes in `src/tetris`:

- `BestAgent` plays one game with the saved agent, slowly enough to watch.
- `AgentTraining` trains a new population for 100 generations, then saves the best agent over the old one.

**From a terminal**, in the project folder:

```sh
javac -d out $(find src -name "*.java")
java -cp out tetris.BestAgent        # or tetris.AgentTraining
```

Both programs read and write `data/bestAgent.txt` relative to the folder they are run from, so run them from the
project folder. Closing the game window ends the program.

The population size and the number of generations are constants at the top of `AgentTraining`. The playback speed
is a constant at the top of `BestAgent`.

## Project layout

```
src/tetris/
    AgentTraining.java   trains a population and saves the best agent
    BestAgent.java       plays a game with the saved agent
    game/                the rules: board, pieces, scoring. No AI and no graphics.
    ai/                  the agent, its network, and the genetic algorithm
    ui/                  the window that shows a game
data/
    bestAgent.txt        weights of the trained agent
```

## Known issues

These are bugs in the logic, each marked with `KNOWN ISSUE` in the code. They have been left alone on purpose:
fixing any of them changes how the saved agent plays, so they should be fixed together and the agent retrained.

- **The network ignores most of its hidden layer.** `Matrix.multiply` stops one loop too early for the output
  layer, so the output depends on the first hidden node only.
- **The completed-lines feature is almost always 0.** `BoardFeatures.completedLines` stops counting at the first
  unfinished row, which is normally the top one.
- **Pieces are dealt unevenly.** After the first seven pieces, `TetrisGame.drawFromBag` skips one shape in every
  round.
- **The top row is never cleared.** When a line is removed, `TetrisGrid` copies the top row down without emptying
  it. This only shows when the stack reaches the very top.

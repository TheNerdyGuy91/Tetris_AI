import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Population
{
    ArrayList<Agent> competitors;
    int numAgents;
    int bestIndex;
    private BigInteger bestFitness;
    private Agent firstCompetitor, secondCompetitor, bestCompetitor;
    private Random rand;


    Population(int nAgents)
    {
        bestCompetitor = secondCompetitor = null;
        numAgents = nAgents;
        competitors = new ArrayList<Agent>();
        bestIndex  = 0;
        bestFitness = BigInteger.ZERO;
        rand = new Random();
        for (int a = 0; a < numAgents; a++)
        {
            competitors.add(new Agent());
            competitors.get(a).randomize();
         }
    }
    public void nextGeneration() throws InterruptedException, CloneNotSupportedException {
       rankThem();
       System.out.println("Best Score: " + competitors.get(0).getFitness());
        System.out.println("2nd Best Score: " + competitors.get(1).getFitness());
        if (bestCompetitor == null || bestCompetitor.getFitness() < competitors.get(0).getFitness())
        {
            if (bestCompetitor != null)
            {
                secondCompetitor = bestCompetitor;
            }
            bestCompetitor = (Agent) competitors.get(0).clone();
        }
        if (secondCompetitor == null || (secondCompetitor.getFitness() < competitors.get(1).getFitness())) {
            secondCompetitor = (Agent) competitors.get(1).clone();
        }
        createNextGeneration();

    }
    public void compete() throws CloneNotSupportedException, InterruptedException {
        for (int agent = 0; agent < numAgents; agent++)
        {
            competitors.get(agent).play();
            System.out.println("Fitness: " + agent + " " + competitors.get(agent).getFitness());
        }
    }
    private void createNextGeneration() throws CloneNotSupportedException {

        competitors = new ArrayList<Agent>();
        for (int agent = 0; agent < numAgents; agent++)
        {
            competitors.add(bestCompetitor.crossover(secondCompetitor));
            competitors.get(agent).mutate();

        }
    }
    public void rankThem() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        Collections.sort(competitors, new Comparator<Agent>()
        {
            @Override
            public int compare(Agent o1, Agent o2)
            {
                return (int)(o2.getFitness() - o1.getFitness());
            }
        });
    }
    public void display()
    {
        for (int i = 0; i < competitors.size(); i++)
        {
            System.out.println("Agent " + i);
            competitors.get(i).display();
        }
    }

    public Agent getFirstCompetitor() {
        return firstCompetitor;
    }

    public Agent getBestCompetitor() {
        return bestCompetitor;
    }
}
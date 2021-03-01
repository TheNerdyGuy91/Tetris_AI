import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Population
{
    ArrayList<Agent> agents; // as a temp to test
    Population(int nAgents)
    {
        agents = new ArrayList<Agent>();
        for (int a = 0; a < nAgents; a++)
        {
            agents.add(new Agent());
            agents.get(a).randomize();
        }
    }
    public void nextGeneration()
    {
        Collections.sort(agents, new Comparator<Agent>()
        {
            @Override
            public int compare(Agent o1, Agent o2)
            {
                return o1.getFitness().compareTo(o2.getFitness());
            }
        });
        for (int i = 0; i < agents.size(); i++)
        {
            System.out.println(agents.get(i).getFitness());
        }

    }
    public void display()
    {
        for (int i = 0; i < agents.size(); i++)
        {
            System.out.println("Agent " + i);
            agents.get(i).display();
        }
    }
}
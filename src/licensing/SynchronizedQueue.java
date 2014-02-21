package licensing;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.LinkedList;
import java.util.Iterator;

public class SynchronizedQueue extends ConcurrentLinkedQueue<Customer>{
    private LinkedList<AbstractAgent> agents;

    public SynchronizedQueue() {
        super();
        agents = new LinkedList<AbstractAgent>();
    }

    public void registerAgent(AbstractAgent agent) {
        agents.push(agent);
    }

    public void deregisterAgent(AbstractAgent agent) {
        agents.remove(agent);
    }

    // The queue will guess the waiting time for the next customer
    public double peekTime() {
        // Naive algorithm that estimates the waiting time by doing the agent count over the sum of the wait times
        // for each of the agents (to get the customers / second rate) multiplied by the number of people in the queue

        int waitSum = 0;

        Iterator<AbstractAgent> agentIterator = agents.iterator();

        while (agentIterator.hasNext()) {
            waitSum += agentIterator.next().getAverageWait();
        }

        return size() * (agents.size() / waitSum);
    }


}


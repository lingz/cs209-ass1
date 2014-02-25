package licensing;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Queue;

// A synchronized queue using a linkedList, but with the synchronized keyword to make operations threadsafe
// Is a blocking queue, such that if the list is empty, it will hang the calling thread until it is no longer empty.
public class SynchronizedQueue<E> extends LinkedList<E> implements Queue<E>{
    private LinkedList<AbstractAgent> agents;

    public SynchronizedQueue() {
        super();
        agents = new LinkedList<AbstractAgent>();
    }

    public synchronized void registerAgent(AbstractAgent agent) {
        agents.push(agent);
    }

    public synchronized void deregisterAgent(AbstractAgent agent) {
        agents.remove(agent);
    }

    // This uses notifyAll, to alert waiting threads
    public synchronized boolean add(E object) {
        /*if (this.size() == 0) {
            notifyAll();
        }*/
        return super.add(object);
    }

    public synchronized E poll() {
        /*while (this.size() == 0) {
            try {
                wait();
            } catch (InterruptedException exception) {
                return null;
            }
        }*/
        if (this.size() == 0) return null;
        return super.poll();
    }


    // The queue will guess the waiting time for the next customer
    public double peekTime() 
    {
        // Naive algorithm that estimates the waiting time by doing the agent count over the sum of the wait times
        // for each of the agents (to get the customers / second rate) multiplied by the number of people in the queue

        int waitSum = 0;

        Iterator<AbstractAgent> agentIterator = agents.iterator();

        while (agentIterator.hasNext()) {
            waitSum += agentIterator.next().getAverageWait();
        }

        if (waitSum == 0) return 0;
        return size() * (agents.size() / waitSum);
    }


}


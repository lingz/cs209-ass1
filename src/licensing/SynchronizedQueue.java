import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.LinkedList;

public class SynchronizedQueue extends ConcurrentLinkedQueue<Customer>{
    private LinkedList<AbstractAgent> agents;

    public SynchronizedQueue() {
        super();
        agents = new LinkedList<AbstractAgent>();
    }

    public SynchronizedQueue(Customer[] customers) {
        super(customers);
        agents = new LinkedList<AbstractAgent>();
    }

    public registerAgent(AbstractAgent agent) {
        agents.push(agent);
    }

    public deregisterAgent(AbstractAgent agent) {
        agents.remove(agent);
    }

}
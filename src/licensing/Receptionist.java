package licensing;
import java.util.LinkedList;
import java.util.Vector;

public class Receptionist extends AbstractAgent{
    SynchronizedQueue<Customer> customerQueue;
    SynchronizedQueue<Customer> licensingQueue;
    SynchronizedQueue<Customer> eyeTestingQueue;
    SynchronizedQueue<Customer> translatingQueue;
    SynchronizedQueue<UAEDriversLicense> successQueue;
    SynchronizedQueue<Customer> failureQueue;


    // Options for strategy are 'RANDOM' for random placement, 'PEEK' for picking the queue with the lowest time by
    // peaking at the queues, and 'FEWEST' for picking the queue with the least number of people in it.
    private static String STRATEGY;

    int numCustomers;

	Receptionist(String strategy, SynchronizedQueue<Customer> customerQueue,SynchronizedQueue<Customer> licensingQueue, SynchronizedQueue<Customer> eyeTestingQueue, SynchronizedQueue<Customer> translatingQueue, SynchronizedQueue<UAEDriversLicense> successQueue, SynchronizedQueue<Customer> failureQueue, int numCustomers)
	{
        this.STRATEGY=strategy;
		this.customerQueue=customerQueue;
		this.licensingQueue=licensingQueue;
		this.eyeTestingQueue=eyeTestingQueue; 
		this.translatingQueue=translatingQueue; 
		this.successQueue=successQueue; 
		this.failureQueue=failureQueue;
		this.numCustomers=numCustomers;
	}
    // Check if a customer has all the correct documents (but not if those documents are correct).
    // It has a 40% failure rate of checking, to simulate people reporting bad answers.
    private boolean checkCustomer(Customer customer) {
        return ((customer.driversLicense != null && customer.emiratesId != null && customer.passport != null) ||
                (((int) (Math.random()*5)) > 3) );
    }

    // Strategy pattern for placing the customer
    private void placeCustomer(Customer customer) {
        SynchronizedQueue<Customer> targetQueue;
        if (STRATEGY == "RANDOM") {
            targetQueue = randomQueue();
        } else if (STRATEGY == "PEEK") {
            targetQueue = peekQueue();
        } else if (STRATEGY == "FEWEST") {
            targetQueue = fewestQueue();
        } else {
            // Default
            targetQueue = randomQueue();
        }
        targetQueue.push(customer);
    }

    private SynchronizedQueue<Customer> randomQueue() {
        if (Math.random() < 0.5) {
            return eyeTestingQueue;
        } else {
            return translatingQueue;
        }
    }

    private SynchronizedQueue<Customer> peekQueue() {
        if (eyeTestingQueue.peekTime() < translatingQueue.peekTime())  {
            return eyeTestingQueue;
        } else {
            return translatingQueue;
        }
    }

    private SynchronizedQueue<Customer> fewestQueue() {
        if (eyeTestingQueue.size() < translatingQueue.size()) {
            return eyeTestingQueue;
        } else {
            return translatingQueue;
        }

    }

    public void run() {
        while (!customerQueue.isEmpty()) {
            placeCustomer(customerQueue.poll());
            try {
                Thread.sleep((long) (Math.random()*20 + 10));
            } catch (InterruptedException exception)  {
            }

        }
    };
}

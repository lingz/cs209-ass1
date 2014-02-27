package licensing;

public class Receptionist implements Runnable {
    private SynchronizedQueue<Customer> customerQueue;
    private SynchronizedQueue<Customer> licensingQueue;
    private SynchronizedQueue<Customer> eyeTestingQueue;
    private SynchronizedQueue<Customer> translatingQueue;
    private SynchronizedQueue<UAEDriversLicense> successQueue;
    private SynchronizedQueue<Customer> failureQueue;
    private int numCustomers;

    // Options for strategy are 'RANDOM' for random placement, 'PEEK' for picking the queue with the lowest time by
    // peaking at the queues, and 'FEWEST' for picking the queue with the least number of people in it.
    private static String STRATEGY;

	Receptionist(String strategy,
            SynchronizedQueue<Customer> customerQueue,
            SynchronizedQueue<Customer> licensingQueue,
            SynchronizedQueue<Customer> eyeTestingQueue,
            SynchronizedQueue<Customer> translatingQueue,
            SynchronizedQueue<UAEDriversLicense> successQueue,
            SynchronizedQueue<Customer> failureQueue,
            int numCustomers) {
        this.STRATEGY = strategy;
		this.customerQueue = customerQueue;
		this.licensingQueue = licensingQueue;
		this.eyeTestingQueue = eyeTestingQueue;
		this.translatingQueue = translatingQueue;
		this.successQueue = successQueue;
		this.failureQueue = failureQueue;
		this.numCustomers = numCustomers;
	}

    // Check if a customer has all the correct documents (but not if those documents are correct).
    // It has a 40% failure rate of checking, to simulate people reporting bad answers.
    private boolean hasDocuments(Customer customer) {
        if ((customer.driversLicense != null &&
                customer.emiratesId != null &&
                customer.passport != null) ||
                (((int) (Math.random()*5)) > 3)) { // possible to pass even without documents
            return true;
        } else {
            return false;
        }
    }

    // Strategy pattern for placing the customer
    private void placeCustomer(Customer customer) { // doesn't care about eye tests or translations
        if (!hasDocuments(customer)) {
            failureQueue.add(customer);
            System.out.println("FAILURE AT RECEPTIONIST FOR: " + customer);
            System.out.println("("+successQueue.size()+" successes, "+
                    failureQueue.size()+" failures, "+
                    numCustomers+" in total)");
            return;
        } else {
            SynchronizedQueue<Customer> targetQueue;
            
            if (STRATEGY.equals("RANDOM")) {
                targetQueue = randomQueue();
            } else if (STRATEGY.equals("PEEK")) {
                targetQueue = peekQueue();
            } else if (STRATEGY.equals("FEWEST")) {
                targetQueue = fewestQueue();
            } else { // default
                targetQueue = randomQueue();
            }

            System.out.println("\tAllowed to pass (by receptionist): "+customer);
            targetQueue.push(customer);
        }
    }

    private SynchronizedQueue<Customer> randomQueue() {
        double random = Math.random();
        if (random < 0.33) {
            return eyeTestingQueue;
        } else if (random < 0.66) {
            return translatingQueue;
        } else {
            return licensingQueue;
        }
    }

    private SynchronizedQueue<Customer> peekQueue() {
        if (eyeTestingQueue.peekTime() <= translatingQueue.peekTime() &&
                eyeTestingQueue.peekTime() <= licensingQueue.peekTime())  {
            return eyeTestingQueue;
        } else if (translatingQueue.peekTime() <= eyeTestingQueue.peekTime() &&
                translatingQueue.peekTime() <= licensingQueue.peekTime())  {
            return translatingQueue;
        } else {
            return licensingQueue;
        }
    }

    private SynchronizedQueue<Customer> fewestQueue() {
        if (eyeTestingQueue.size() <= translatingQueue.size() &&
                eyeTestingQueue.size() <= licensingQueue.size()) {
            return eyeTestingQueue;
        } else if (translatingQueue.size() <= eyeTestingQueue.size() &&
                translatingQueue.size() <= licensingQueue.size()) {
            return translatingQueue;
        } else {
            return licensingQueue;
        }
    }

    public void run() {
        while ((failureQueue.size() + successQueue.size()) != numCustomers) {
            Customer customer = customerQueue.poll();

            if (customer != null) {
                System.out.println("\tCustomer processed by receptionist: " + customer);

                try {
                    // takes between 5 and 10 seconds
                    Thread.sleep((long) (Math.random() * 5000 + 5000));
                } catch (InterruptedException ex) {
                    // ignore
                }

                placeCustomer(customer);
            } else {
                try {
                    // try again in 10 seconds to avoid busy-waiting
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
        System.out.println("\tRecptionist terminated.");
    }
}

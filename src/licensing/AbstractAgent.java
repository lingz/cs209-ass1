package licensing;

public abstract class AbstractAgent implements Runnable {
    protected int minWait;
    protected int maxWait;

	AbstractAgent() {
        
	}
	
	public abstract void run(); 

	public Customer pull(SynchronizedQueue<Customer> queue) {
		Customer customer = queue.remove();
		return customer;
	}
	
	public void push(SynchronizedQueue<Customer> queue, Customer customer) {
		queue.add(customer);
	}

    public int getAverageWait() {
        return (minWait + maxWait) / 2;
    }

    protected void process() {
        try {
            Thread.sleep(minWait + (int) (Math.random() * (maxWait-minWait)));
        } catch (InterruptedException ex) {
            // ignore
        }
    }
}

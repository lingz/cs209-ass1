package licensing;

public abstract class AbstractAgent implements Runnable {
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
        return (getMinWait() + getMaxWait()) / 2;
    }

    protected void process() {
        try {
            Thread.sleep(getMinWait() + (int) (Math.random() * (getMaxWait()-getMinWait())));
        } catch (InterruptedException ex) {
            // ignore
        }
    }

    abstract int getMinWait();

    abstract int getMaxWait();
}

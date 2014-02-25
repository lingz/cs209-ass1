package licensing;

public abstract class AbstractAgent implements Runnable {

    protected int minWait;
    protected int maxWait;

    public int getAverageWait() {
        return (minWait + maxWait) / 2;
    }

	AbstractAgent() 
	{
	//	t = new Thread(this, "Start Thread");
	//	t.start();
	}
	
	public abstract void run(); 

	public Customer pull(SynchronizedQueue<Customer> queue) 
	{
		Customer customer = queue.remove();
		return customer;
	}
	
	public void push(SynchronizedQueue<Customer> queue, Customer customer)
	{
		queue.add(customer);
	}

    protected void process() {
        try {
            Thread.sleep(minWait + (int) (Math.random() * (maxWait-minWait)));
        } catch (InterruptedException ex) {
        }
    }
}

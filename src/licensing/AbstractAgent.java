package licensing;

public abstract class AbstractAgent implements Runnable {

    private int minWait;
    private int maxWait;

    public int getAverageWait() {
        return (minWait + maxWait) / 2;
    }

	AbstractAgent() 
	{
	//	t = new Thread(this, "Start Thread");
	//	t.start();
	}
	
	public abstract void run(); 

	public Customer pull(SynchronizedQueue queue) 
	{
		Customer customer = queue.remove();
		return customer;
	}
	
	public void push(SynchronizedQueue queue, Customer customer)
	{
		queue.add(customer);
	}
}

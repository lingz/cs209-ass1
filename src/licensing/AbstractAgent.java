
public abstract class AbstractAgent implements Runnable {

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

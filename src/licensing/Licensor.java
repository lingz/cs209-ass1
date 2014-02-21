import java.util.*;

class Licensor extends AbstractAgent {
	private SynchronizedQueue licenseQueue; 
	private SynchronizedQueue eyeTestQueue;
	private SynchronizedQueue translatorQueue;
	private SynchronizedQueue printQueue;
	private SynchronizedQueue failureVector;
	private SynchronizedQueue successVector;
	private int numCustomers;
	 
	public Licensor(SynchronizedQueue printQueue, int numCustomers, SynchronizedQueue licenseQueue,  SynchronizedQueue eyeTestQueue, SynchronizedQueue translatorQueue, SynchronizedQueue failureVector, SynchronizedQueue successVector) 
	{
		this.licenseQueue=licenseQueue;
		this.eyeTestQueue=eyeTestQueue;
		this.translatorQueue=translatorQueue;
		this.failureVector=failureVector;
		this.successVector=successVector;
		this.numCustomers=numCustomers;
		this.printQueue=printQueue;
	}	
	
	public boolean license(Customer customer)
	{
		if(customer.emiratesId == null || customer.driversLicense == null || customer.passport==null)
		{
			return true;
		}
		return false;
	}
	

	public void run() throws InterruptException{
		
		while((failureVector.size()+successVector.size())!=numCustomers)	
		{
			Thread.sleep(12 + (int)(Math.random()*19));
			//This may be bad code because I'm creating a new customer each iteration
			//Can I safely reuse customer without changing the value added to the license/eyetest queues?
			Customer customer = licenseQueue.pull();
			if(customer!=null)
			{
				if(license(customer))
				{
					failureVector.add(customer);
				}
				if(customer.eyeTest==null)
				{
					eyeTestQueue.add(customer);
				}
				if(customer.driversLicenseTranslation==null)
				{
					licenseQueue.add(customer);
				}
				printQueue.add(customer);
			}
		}	
}

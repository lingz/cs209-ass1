import java.util.*;

class Translator extends AbstractAgent {
	private SynchronizedQueue licenseQueue; 
	private SynchronizedQueue eyeTestQueue;
	private SynchronizedQueue translatorQueue; 
	private SynchronizedQueue failureVector;
	private SynchronizedQueue successVector;
	private int numCustomers;
	 
	public Translator(int numCustomers, SynchronizedQueue licenseQueue,  SynchronizedQueue eyeTestQueue, SynchronizedQueue translatorQueue, SynchronizedQueue failureVector, SynchronizedQueue successVector) 
	{
		this.licenseQueue=licenseQueue;
		this.eyeTestQueue=eyeTestQueue;
		this.translatorQueue=translatorQueue;
		this.failureVector=failureVector;
		this.successVector=successVector;
		this.numCustomers=numCustomers;
	}	
	
	public void translate(Customer customer) 
	{
		customer.driversLicenseTranslation = new DriversLicenseTranslation(customer.driversLicense);	
	}

	public void run() throws InterruptException{
		
		while((failureVector.size()+successVector.size())!=numCustomers)	
		{
			Thread.sleep(12 + (int)(Math.random()*19));
			//This may be bad code because I'm creating a new customer each iteration
			//Can I safely reuse customer without changing the value added to the license/eyetest queues?
			Customer customer = translatorQueue.pull();
			if(customer!=null)
			{
				translate(customer);
				if(customer.eyeTest==null)
				{
					eyeTestQueue.add(customer);
				}
				else
				{
					licenseQueue.add(customer);
				}
			}
		}		
	}
}

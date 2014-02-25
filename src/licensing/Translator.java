package licensing;

import java.util.*;

class Translator extends AbstractAgent {
	private SynchronizedQueue<Customer> licenseQueue; 
	private SynchronizedQueue<Customer> eyeTestQueue;
	private SynchronizedQueue<Customer> translatorQueue; 
	private SynchronizedQueue<Customer> failureVector;
	private SynchronizedQueue<UAEDriversLicense> successVector;
	private int numCustomers;
	 
	public Translator(SynchronizedQueue<Customer> translatorQueue,
            SynchronizedQueue<Customer> eyeTestQueue,
            SynchronizedQueue<Customer> licenseQueue,
            SynchronizedQueue<UAEDriversLicense> successVector,
            SynchronizedQueue<Customer> failureVector,
            int numCustomers)
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

	public void run() {
		
		while((failureVector.size()+successVector.size())!=numCustomers)	
		{
            try {
    			Thread.sleep(12 + (int)(Math.random()*19));
            } catch (InterruptedException ex) {

            }
			//This may be bad code because I'm creating a new customer each iteration
			//Can I safely reuse customer without changing the value added to the license/eyetest queues?
			Customer customer = translatorQueue.poll();
			if(customer!=null)
			{
				if(customer.emiratesId == null || customer.driversLicense == null || customer.passport==null)
				{	
					failureVector.add(customer);
					continue;
				}
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

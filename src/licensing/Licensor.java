package licensing;

import java.util.*;

class Licensor extends AbstractAgent {
	private SynchronizedQueue<Customer> licenseQueue; 
	private SynchronizedQueue<Customer> eyeTestQueue;
	private SynchronizedQueue<Customer> translatorQueue;
	private SynchronizedQueue<Customer> printQueue;
	private SynchronizedQueue<Customer> failureVector;
	private SynchronizedQueue<UAEDriversLicense> successVector;
	private int numCustomers;

    protected int minWait = 120;
    protected int maxWait = 300;
	 

	public Licensor(SynchronizedQueue<Customer> licenseQueue,
            SynchronizedQueue<Customer> eyeTestQueue,
            SynchronizedQueue<Customer> translatorQueue,
            SynchronizedQueue<Customer> printQueue,
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
		this.printQueue=printQueue;

        licenseQueue.registerAgent(this);
	}	
	
	public boolean license(Customer customer)
	{
		if(customer.emiratesId == null || customer.driversLicense == null || customer.passport==null)
		{
			return true;
		}
		return false;
	}
	

	public void run() {
		
		while((failureVector.size()+successVector.size())!=numCustomers)
		{
            //System.out.println("licensor: "+failureVector.size()+"; "+successVector.size()+"; "+numCustomers);
            process();
            
			Customer customer = licenseQueue.poll();
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
					translatorQueue.add(customer);
				}
				printQueue.add(customer);
			}
		}
        //System.out.println("LICENSOR TERMINATED");
    }
}

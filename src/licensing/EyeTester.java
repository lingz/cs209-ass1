import java.util.*;

class EyeTester extends AbstractAgent {
	private SynchronizedQueue licenseQueue; 
	private SynchronizedQueue eyeTestQueue;
	private SynchronizedQueue translatorQueue; 
	private SynchronizedQueue failureVector;
	private SynchronizedQueue successVector;
	private int numCustomers;
	 
	public EyeTester(int numCustomers, SynchronizedQueue licenseQueue,  SynchronizedQueue eyeTestQueue, SynchronizedQueue translatorQueue, SynchronizedQueue failureVector, SynchronizedQueue successVector) 
	{
		this.licenseQueue=licenseQueue;
		this.eyeTestQueue=eyeTestQueue;
		this.translatorQueue=translatorQueue;
		this.failureVector=failureVector;
		this.successVector=successVector;
		this.numCustomers=numCustomers;
	}	
	
	public void eyeTest(Customer customer) 
	{
	
        String emiratesIdNumber = UUID.randomUUID().toString();
		customer.eyeTest = new EyeTest(customer.firstName,customer.lastName,customer.nationality,customer.gender,customer.dateOfBirth,customer.expiryDate,emiratesIdNumber);	
	}

	public void run() {
		
		while((failureVector.size()+successVector.size())!=numCustomers)	
		{
			Thread.sleep(12 + (int)(Math.random()*19));
			//This may be bad code because I'm creating a new customer each iteration
			//Can I safely reuse customer without changing the value added to the license/eyetest queues?
			Customer customer = eyeTestQueue.pull();
			if(customer!=null)
			{
				eyeTest(customer);
				if(customer.driversLicenseTranslation==null)
				{
					translatorQueue.add(customer);
				}
				else
				{
					licenseQueue.add(customer);
				}
			}
		}		
	}
}

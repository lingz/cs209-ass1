package licensing;

import java.util.*;

class EyeTester extends AbstractAgent {
	private SynchronizedQueue<Customer> licenseQueue; 
	private SynchronizedQueue<Customer> eyeTestQueue;
	private SynchronizedQueue<Customer> translatorQueue; 
	private SynchronizedQueue<Customer> failureVector;
	private SynchronizedQueue<UAEDriversLicense> successVector;
	private int numCustomers;
	 
	public EyeTester(SynchronizedQueue<Customer> eyeTestQueue,
            SynchronizedQueue<Customer> translatorQueue,
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
	
	public void eyeTest(Customer customer)
	{
	
        String emiratesIdNumber = UUID.randomUUID().toString();
		customer.eyeTest = new EyeTest(customer.emiratesId.firstName,
                customer.emiratesId.lastName,
                customer.emiratesId.nationality,
                customer.emiratesId.gender,
                customer.emiratesId.dateOfBirth,
                customer.emiratesId.expiryDate,
                emiratesIdNumber);
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
			Customer customer = eyeTestQueue.poll();
			if(customer!=null)
			{
				if(customer.emiratesId == null || customer.driversLicense == null || customer.passport==null)
				{	
					failureVector.add(customer);
					continue;
				}
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

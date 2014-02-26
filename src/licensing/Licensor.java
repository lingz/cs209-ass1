package licensing;

import java.util.*;

class Licensor extends AbstractAgent {
	private SynchronizedQueue<Customer> licenseQueue; 
	private SynchronizedQueue<Customer> eyeTestQueue;
	private SynchronizedQueue<Customer> translatorQueue;
	private SynchronizedQueue<Customer> printQueue;
	private SynchronizedQueue<Customer> failureQueue;
	private SynchronizedQueue<UAEDriversLicense> successQueue;
	private int numCustomers;

    protected int minWait = 120;
    protected int maxWait = 300;
	 

	public Licensor(SynchronizedQueue<Customer> licenseQueue,
            SynchronizedQueue<Customer> eyeTestQueue,
            SynchronizedQueue<Customer> translatorQueue,
            SynchronizedQueue<Customer> printQueue,
            SynchronizedQueue<UAEDriversLicense> successQueue,
            SynchronizedQueue<Customer> failureQueue,
            int numCustomers) {
		this.licenseQueue=licenseQueue;
		this.eyeTestQueue=eyeTestQueue;
		this.translatorQueue=translatorQueue;
		this.failureQueue=failureQueue;
		this.successQueue=successQueue;
		this.numCustomers=numCustomers;
		this.printQueue=printQueue;

        licenseQueue.registerAgent(this);
	}	
	
	public boolean documentsCorrect(Customer customer) {
		if(customer.emiratesId == null ||
                customer.driversLicense == null ||
                customer.passport==null) {
			return false;
		} else {
            return true;
        }
	}

	public void run() {
		while ((failureQueue.size() + successQueue.size()) != numCustomers) {
			Customer customer = licenseQueue.poll();

            process();

			if (customer != null) {
				if (!documentsCorrect(customer)) {
					failureQueue.add(customer);
                    System.out.println("FAILURE AT LICENSING FOR: " + customer);
                    continue;
				}

                if (customer.eyeTest == null) {
                    System.out.println("Sent to eye testing (by licensor): " + customer);
					eyeTestQueue.add(customer);
				} else if(customer.driversLicenseTranslation == null) {
					System.out.println("Sent to translation (by licensor): " + customer);
                    translatorQueue.add(customer);
				} else {
                    System.out.println("Sent to printing (by licensor): " + customer);
                    printQueue.add(customer);
                }
			}
		}
        System.out.println("Licensor terminated.");
    }
}

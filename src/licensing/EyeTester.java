package licensing;

import java.util.*;

class EyeTester extends AbstractAgent {
	private SynchronizedQueue<Customer> licensingQueue;
	private SynchronizedQueue<Customer> eyeTestingQueue;
	private SynchronizedQueue<Customer> translatingQueue;
    private SynchronizedQueue<UAEDriversLicense> successQueue;
	private SynchronizedQueue<Customer> failureQueue;
	private int numCustomers;

    protected int minWait = 120;
    protected int maxWait = 300;
	 
	public EyeTester(SynchronizedQueue<Customer> eyeTestingQueue,
            SynchronizedQueue<Customer> translatingQueue,
            SynchronizedQueue<Customer> licensingQueue,
            SynchronizedQueue<UAEDriversLicense> successQueue,
            SynchronizedQueue<Customer> failureQueue,
            int numCustomers) {
		this.licensingQueue = licensingQueue;
		this.eyeTestingQueue = eyeTestingQueue;
		this.translatingQueue = translatingQueue;
        this.successQueue = successQueue;
		this.failureQueue = failureQueue;
		this.numCustomers = numCustomers;

        eyeTestingQueue.registerAgent(this);
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

	public void eyeTest(Customer customer) {
        String eyeTestNumber = UUID.randomUUID().toString();

		customer.eyeTest = new EyeTest(customer.emiratesId.firstName,
                customer.emiratesId.lastName,
                customer.emiratesId.nationality,
                customer.emiratesId.gender,
                customer.emiratesId.dateOfBirth,
                customer.emiratesId.expiryDate,
                eyeTestNumber);
	}

	public void run() {
		while ((failureQueue.size() + successQueue.size()) != numCustomers) {
            Customer customer = eyeTestingQueue.poll();

            process();
			
            if (customer != null) {
				if (!documentsCorrect(customer)) {
					failureQueue.add(customer);
                    System.out.println("FAILURE AT EYE TESTING FOR: " + customer);
					continue;
				}

				eyeTest(customer);

				if(customer.driversLicenseTranslation == null) {
					System.out.println("Sent to translation (by eye tester): " + customer);
                    translatingQueue.add(customer);
				} else {
                    System.out.println("Sent to licensing (by eye tester): " + customer);
					licensingQueue.add(customer);
				}
			}
		}
        System.out.println("Eye tester terminated.");
	}
}

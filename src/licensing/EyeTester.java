package licensing;

import java.util.*;

class EyeTester extends AbstractAgent {
	private SynchronizedQueue<Customer> licensingQueue;
	private SynchronizedQueue<Customer> eyeTestingQueue;
	private SynchronizedQueue<Customer> translatingQueue;
    private SynchronizedQueue<UAEDriversLicense> successQueue;
	private SynchronizedQueue<Customer> failureQueue;
	private int numCustomers;

    protected int minWait = 120000;
    protected int maxWait = 300000;
	 
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
                customer.passport == null) {
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
			
            if (customer != null) {
                System.out.println("\t\tCustomer processed by eye tester: " + customer);

				if (!documentsCorrect(customer)) {
					failureQueue.add(customer);
                    System.out.println("FAILURE AT EYE TESTING FOR: " + customer);
                    System.out.println("("+successQueue.size()+" successes, "+
                        failureQueue.size()+" failures, "+
                        numCustomers+" in total)");
					continue;
				} else {
                    process();
                }

				eyeTest(customer);

				if(customer.driversLicenseTranslation == null) {
					System.out.println("\t\tSent to translation (by eye tester): " + customer);
                    translatingQueue.add(customer);
				} else {
                    System.out.println("\t\tSent to licensing (by eye tester): " + customer);
					licensingQueue.add(customer);
				}
			} else {
                try {
                    // try again in 10 seconds to avoid busy-waiting
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
		}
        System.out.println("\t\tEye tester terminated.");
	}

    public int getMinWait() {
        return minWait;
    }

    public int getMaxWait() {
        return maxWait;
    }
}

package licensing;

import java.util.*;

class Licensor extends AbstractAgent {
	private SynchronizedQueue<Customer> licensingQueue;
	private SynchronizedQueue<Customer> eyeTestingQueue;
	private SynchronizedQueue<Customer> translatingQueue;
	private SynchronizedQueue<Customer> printingQueue;
    private SynchronizedQueue<UAEDriversLicense> successQueue;
	private SynchronizedQueue<Customer> failureQueue;
	private int numCustomers;

    protected int minWait = 120000;
    protected int maxWait = 300000;
	 

	public Licensor(SynchronizedQueue<Customer> licensingQueue,
            SynchronizedQueue<Customer> eyeTestingQueue,
            SynchronizedQueue<Customer> translatingQueue,
            SynchronizedQueue<Customer> printingQueue,
            SynchronizedQueue<UAEDriversLicense> successQueue,
            SynchronizedQueue<Customer> failureQueue,
            int numCustomers) {
		this.licensingQueue = licensingQueue;
		this.eyeTestingQueue = eyeTestingQueue;
		this.translatingQueue = translatingQueue;
		this.failureQueue = failureQueue;
		this.successQueue = successQueue;
		this.numCustomers = numCustomers;
		this.printingQueue = printingQueue;

        licensingQueue.registerAgent(this);
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

	public void run() {
		while ((failureQueue.size() + successQueue.size()) != numCustomers) {
			Customer customer = licensingQueue.poll();

			if (customer != null) {
                System.out.println("\t\t\t\tCustomer processed by licensor: " + customer);

                if (!documentsCorrect(customer)) {
					failureQueue.add(customer);
                    System.out.println("FAILURE AT LICENSING FOR: " + customer);
                    System.out.println("("+successQueue.size()+" successes, "+
                            failureQueue.size()+" failures, "+
                            numCustomers+" in total)");
                    continue;
				} else {
                    process();
                }

                if (customer.eyeTest == null) {
                    System.out.println("\t\t\t\tSent to eye testing (by licensor): " + customer);
					eyeTestingQueue.add(customer);
				} else if(customer.driversLicenseTranslation == null) {
					System.out.println("\t\t\t\tSent to translation (by licensor): " + customer);
                    translatingQueue.add(customer);
				} else {
                    System.out.println("\t\t\t\tSent to printing (by licensor): " + customer);
                    printingQueue.add(customer);
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
        System.out.println("\t\t\t\tLicensor terminated.");
    }

    public int getMinWait() {
        return minWait;
    }

    public int getMaxWait() {
        return maxWait;
    }
}

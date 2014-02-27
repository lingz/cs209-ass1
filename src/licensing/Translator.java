package licensing;

class Translator extends AbstractAgent {
	private SynchronizedQueue<Customer> licensingQueue;
	private SynchronizedQueue<Customer> eyeTestingQueue;
	private SynchronizedQueue<Customer> translatingQueue;
    private SynchronizedQueue<UAEDriversLicense> successQueue;
	private SynchronizedQueue<Customer> failureQueue;
	private int numCustomers;

    protected int minWait = 300000;
    protected int maxWait = 600000;
	 
	public Translator(SynchronizedQueue<Customer> translatingQueue,
            SynchronizedQueue<Customer> eyeTestingQueue,
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

        translatingQueue.registerAgent(this);
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

	public void translate(Customer customer) {
		customer.driversLicenseTranslation = new DriversLicenseTranslation(customer.driversLicense);	
	}

	public void run() {	
		while ((failureQueue.size() + successQueue.size()) != numCustomers) {
			Customer customer = translatingQueue.poll();

			if (customer != null) {
                System.out.println("\t\t\tCustomer processed by translator: " + customer);

				if (!documentsCorrect(customer)) {
					failureQueue.add(customer);
                    System.out.println("FAILURE AT TRANSLATING FOR: " + customer);
                    System.out.println("("+successQueue.size()+" successes, "+
                            failureQueue.size()+" failures, "+
                            numCustomers+" in total)");
					continue;
				} else {
                    process();
                }

				translate(customer);

				if(customer.eyeTest == null) {
                    System.out.println("\t\t\tSent to eye testing (by translator): " + customer);
					eyeTestingQueue.add(customer);
				} else {
                    System.out.println("\t\t\tSent to licensing (by translator): " + customer);
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
        System.out.println("\t\t\tTranslator terminated.");
	}

    public int getMinWait() {
        return minWait;
    }

    public int getMaxWait() {
        return maxWait;
    }
}

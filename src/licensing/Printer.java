package licensing;

public class Printer implements Runnable {
    private SynchronizedQueue<UAEDriversLicense> successQueue;
    private SynchronizedQueue<Customer> failureQueue;
	private int numCustomers;
    private Customer activeJob;
    private boolean isOn = true;
	private boolean isIdle = true;

    Printer(SynchronizedQueue<UAEDriversLicense> successQueue,
            SynchronizedQueue<Customer> failureQueue,
            int numCustomers) {
        this.successQueue = successQueue;
        this.failureQueue = failureQueue;
        this.numCustomers = numCustomers;
	}

    public boolean isIdle() {
		return isIdle;
	}

    public void turnOff() {
        isOn = false;
    }

    public void print(Customer customer) {
        isIdle = false;
        activeJob = customer;
    }

	private void printLicense() {
        UAEDriversLicense dl = new UAEDriversLicense(activeJob);

        System.out.println("\t\t\t\t\t\tPrinting: "+activeJob);

        try {
            Thread.sleep(300000);
        } catch (InterruptedException ex) {
            // ignore
        }

        successQueue.add(dl);
        System.out.println("PRINTER SUCCESS FOR: " + activeJob);
        System.out.println("("+successQueue.size()+" successes, "+
                failureQueue.size()+" failures, "+
                numCustomers+" in total)");
        activeJob = null;
        isIdle = true;
	}

    public void run() {
        while (isOn) {
            if (!isIdle) {
                printLicense();
            }

            // wait 10 seconds before polling to avoid busy-waiting
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                // ignore
            }
        }
        System.out.println("\t\t\t\t\t\tPrinter turned off.");
    }
}

package licensing;

import java.util.concurrent.*;

public class PrintingAgent implements Runnable {
    private CountDownLatch latch;
	private SynchronizedQueue<Customer> printingQueue;
	private SynchronizedQueue<UAEDriversLicense> successQueue;
    private SynchronizedQueue<Customer> failureQueue;
	private int numCustomers;
    private Printer printer;
    public static boolean isFinished = false;

	public PrintingAgent(CountDownLatch latch,
            SynchronizedQueue printingQueue,
            SynchronizedQueue successQueue,
            SynchronizedQueue failureQueue,
            int numCustomers) {
        this.latch = latch;
		this.failureQueue = failureQueue;
		this.successQueue = successQueue;
		this.numCustomers = numCustomers;
		this.printingQueue = printingQueue;

        printer = new Printer(
                successQueue, failureQueue, numCustomers);
        new Thread(printer).start();
	}

    private synchronized void signalResults() {
        if (!isFinished) {
            isFinished = true;
            System.out.println("TOTAL STATS: "+
                    failureQueue.size()+" failures, "+
                    successQueue.size()+" successes "+
                    "("+numCustomers+" total)");
        }
        
        latch.countDown();
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
		while((failureQueue.size()+successQueue.size()) != numCustomers) {
            while (!printer.isIdle()) {
                try {
                    // wait for 10 seconds
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {

                }
            }

			Customer customer = printingQueue.poll();

			if (customer != null) {
                System.out.println("\t\t\t\t\tCustomer processed by printing agent: " + customer);

                try {
                    // takes between 5 and 10 seconds
                    Thread.sleep((long) (Math.random()*5000 + 5000));
                } catch (InterruptedException ex) {
                    // ignore
                }

                System.out.println("\t\t\t\t\tSent to print (by printing agent): "+ customer);
				printer.print(customer);
			} else {
                try {
                    // try again in 10 seconds to avoid busy-waiting
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
		}
        printer.turnOff();
        signalResults();
        System.out.println("\t\t\t\t\tPrinting agent terminated.");
    }
}

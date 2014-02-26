package licensing;

public class PrintingAgent implements Runnable {
	private SynchronizedQueue<Customer> printingQueue;
	private SynchronizedQueue<Customer> failureQueue;
	private SynchronizedQueue<UAEDriversLicense> successQueue;
	private int numCustomers;
    private Printer printer;
    public static boolean isFinished = false;

	public PrintingAgent(SynchronizedQueue printingQueue,
            SynchronizedQueue successQueue,
            SynchronizedQueue failureQueue,
            int numCustomers) {
		this.failureQueue = failureQueue;
		this.successQueue = successQueue;
		this.numCustomers = numCustomers;
		this.printingQueue = printingQueue;

        printer = new Printer(successQueue);
        new Thread(printer).start();
	}

    private synchronized void signalResults() {
        if (!isFinished) {
            System.out.println("TOTAL STATS: "+
                    failureQueue.size()+" failures, "+
                    successQueue.size()+" successes "+
                    "("+numCustomers+" total)");
            isFinished = true;
        }
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
		while((failureQueue.size()+successQueue.size())!=numCustomers)
		{
            //System.out.println("pa: "+failureQueue.size()+"; "+successQueue.size()+"; "+numCustomers);
            while (!printer.isIdle()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {

                }
            }

			Customer customer = printingQueue.poll();
			if(customer!=null)
			{
				printer.print(customer);
			}
		}
        printer.turnOff();
        signalResults();
        System.out.println("Printing agent terminated.");
    }
}

package licensing;

public class PrintingAgent implements Runnable {
	private SynchronizedQueue<Customer> printQueue;
	private SynchronizedQueue<Customer> failureVector;
	private SynchronizedQueue<UAEDriversLicense> successVector;
	private int numCustomers;

    private Printer printer;

	public PrintingAgent(SynchronizedQueue printQueue,
            SynchronizedQueue successVector,
            SynchronizedQueue failureVector,
            int numCustomers)
	{
		this.failureVector=failureVector;
		this.successVector=successVector;
		this.numCustomers=numCustomers;
		this.printQueue=printQueue;

        printer = new Printer();
        new Thread(printer).start();
	}

	public boolean license(Customer customer)
	{
		if(customer.emiratesId == null || customer.driversLicense == null || customer.passport==null)
		{
			return true;
		}
		return false;
	}


	public void run() {
		while((failureVector.size()+successVector.size())!=numCustomers)
		{
            while (!printer.isIdle()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {

                }
            }

			Customer customer = printQueue.poll();
			if(customer!=null)
			{
				printer.print(customer, successVector);
			}
		}
    }
}

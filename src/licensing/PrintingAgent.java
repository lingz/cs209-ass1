package licensing;

public class PrintingAgent {
	private SynchronizedQueue printQueue;
	private SynchronizedQueue failureVector;
	private SynchronizedQueue successVector;
	private int numCustomers;

    private Printer printer;

	public PrintingAgent(SynchronizedQueue printQueue, SynchronizedQueue licenseQueue,  SynchronizedQueue eyeTestQueue, SynchronizedQueue translatorQueue, SynchronizedQueue failureVector, SynchronizedQueue successVector, int numCustomers)
	{
		this.failureVector=failureVector;
		this.successVector=successVector;
		this.numCustomers=numCustomers;
		this.printQueue=printQueue;

        printer = (new Thread(new Printer(
                customerQueue,
                licensingQueue, eyeTestingQueue, translatingQueue,
                successQueue, failureQueue, NUM_CUSTOMERS
                )));
        printer.start();
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
                Thread.sleep(10);
            }

			Customer customer = printQueue.pull();
			if(customer!=null)
			{
				printer.print(customer, successQueue);
			}
		}
    }
}

package licensing;

public class PrintingAgent {
	private SynchronizedQueue<Customer> printQueue;
	private SynchronizedQueue<Customer> failureVector;
	private SynchronizedQueue<UAEDriversLicense> successVector;
	private int numCustomers;

    private Printer printer;

	public PrintingAgent(SynchronizedQueue printQueue, SynchronizedQueue licenseQueue,  SynchronizedQueue eyeTestQueue, SynchronizedQueue translatorQueue, SynchronizedQueue failureVector, SynchronizedQueue successVector, int numCustomers)
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
                Thread.sleep(10);
            }

			Customer customer = printQueue.poll();
			if(customer!=null)
			{
				printer.print(customer, successVector);
			}
		}
    }
}

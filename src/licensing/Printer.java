package licensing;

public class Printer implements Runnable {
	private boolean isIdle = true;

    Printer()
	{

	}

	public void run() {

    }

	public void print(Customer customer, SynchronizedQueue<UAEDriversLicense> successQueue)
	{
		isIdle = false;
        UAEDriversLicense dl = new UAEDriversLicense(customer);
        try {
            Thread.sleep(30);
        } catch (InterruptedException ex) {

        }
        successQueue.add(dl);
        System.out.println(customer + "");
	}

	public boolean isIdle()
	{
		return isIdle;
	}
}

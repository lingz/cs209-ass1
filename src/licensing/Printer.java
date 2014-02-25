package licensing;

public class Printer implements Runnable {
	private boolean isIdle = true;
    private Customer activeJob;
    private boolean isOn = true;
    private SynchronizedQueue<UAEDriversLicense> successQueue;

    Printer()
	{

	}

	public void run() {
        while (isOn) {
            if (!isIdle) {
                printLicense();
            }
            // wait 30ms before polling
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void turnOff() {
        isOn = false;
    }


    public void print(Customer customer) {
        isIdle = false;
        activeJob = customer;
    }
	private void printLicense()
	{
        UAEDriversLicense dl = new UAEDriversLicense(activeJob);
        try {
            Thread.sleep(30);
        } catch (InterruptedException ex) {

        }
        successQueue.add(dl);
        System.out.println(activeJob + "");
        activeJob = null;
        isIdle = true;
	}

	public boolean isIdle()
	{
		return isIdle;
	}
}

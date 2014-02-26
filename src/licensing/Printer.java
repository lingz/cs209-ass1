package licensing;

public class Printer implements Runnable {
    private SynchronizedQueue<UAEDriversLicense> successQueue;
    private Customer activeJob;
    private boolean isOn = true;
	private boolean isIdle = true;

    Printer(SynchronizedQueue<UAEDriversLicense> successQueue) {
        this.successQueue = successQueue;
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
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            // ignore
        }

        successQueue.add(dl);
        System.out.println("PRINTER SUCCESS FOR: " + activeJob);
        activeJob = null;
        isIdle = true;
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
                // ignore
            }
        }
        System.out.println("Printer turned off.");
    }
}

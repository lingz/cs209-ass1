package licensing;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static int numCustomers;
    
    private static final int NUM_LICENSORS = 5;
    private static final int NUM_EYE_TESTERS = 5;
    private static final int NUM_TRANSLATORS = 5;
    private static final int NUM_PRINTERS = 5;

    private static CountDownLatch latch = new CountDownLatch(NUM_PRINTERS);

    private static SynchronizedQueue<Customer> customerQueue = new SynchronizedQueue<Customer>(); // handled by Receptionist
    private static SynchronizedQueue<Customer> licensingQueue = new SynchronizedQueue<Customer>(); // handled by Licensor
    private static SynchronizedQueue<Customer> eyeTestingQueue = new SynchronizedQueue<Customer>(); // handled by EyeTester
    private static SynchronizedQueue<Customer> translatingQueue = new SynchronizedQueue<Customer>(); // handled by Translator
    private static SynchronizedQueue<Customer> printingQueue = new SynchronizedQueue<Customer>(); // handled by PrintingAgent

    private static SynchronizedQueue<UAEDriversLicense> successQueue = new SynchronizedQueue<UAEDriversLicense>();
    private static SynchronizedQueue<Customer> failureQueue = new SynchronizedQueue<Customer>();

    public static void main(String args[]) {
        run(makeCustomerVector(10));
    }

    public static Vector<UAEDriversLicense> run(Vector<Object[]> customerObjectVector) {
        numCustomers = customerObjectVector.size();

        (new Thread(new Receptionist(
                // Options for strategy are 'RANDOM' for random placement, 'PEEK' for picking the queue with the lowest time by
                // peaking at the queues, and 'FEWEST' for picking the queue with the least number of people in it.
                "RANDOM",
                customerQueue,
                licensingQueue, eyeTestingQueue, translatingQueue,
                successQueue, failureQueue, numCustomers
                ))).start();
        System.out.println("\tReceptionist thread started.");

        for (int i = 0; i < NUM_EYE_TESTERS; i++) {
            (new Thread(new EyeTester(
                    eyeTestingQueue,
                    translatingQueue, licensingQueue,
                    successQueue, failureQueue, numCustomers
                    ))).start();
            System.out.println("\t\tEye tester thread started.");
        }

        for (int i = 0; i < NUM_TRANSLATORS; i++) {
            (new Thread(new Translator(
                    translatingQueue,
                    eyeTestingQueue, licensingQueue,
                    successQueue, failureQueue, numCustomers
                    ))).start();
            System.out.println("\t\t\tTranslator thread started.");
        }

        for (int i = 0; i < NUM_LICENSORS; i++) {
            (new Thread(new Licensor(
                    licensingQueue,
                    eyeTestingQueue, translatingQueue, printingQueue,
                    successQueue, failureQueue, numCustomers
                    ))).start();
            System.out.println("\t\t\t\tLicensor thread started.");
        }

        for (int i = 0; i < NUM_PRINTERS; i++) {
            (new Thread(new PrintingAgent(
                    latch,
                    printingQueue,
                    successQueue, failureQueue, numCustomers
                    ))).start();
            System.out.println("\t\t\t\t\tPrinting agent thread started.");
        }

        initializeCustomers(customerObjectVector, customerQueue);

        try {
            latch.await();
        } catch (InterruptedException ex) {
            // ignore
        }

        return convertToVector(successQueue);
    }

    private static void initializeCustomers(
            Vector<Object[]> customerObjectVector,
            SynchronizedQueue<Customer> customerQueue) {
        long sleepSoFar = 0;

        while (!customerObjectVector.isEmpty()) {
            Object[] currentObject = customerObjectVector.remove(0);
            long sleepTime = (Long) currentObject[0];
            Customer customer = (Customer) currentObject[1];

            long sleepDifference = sleepTime-sleepSoFar;

            try {
                //System.out.println("sleepTime: "+sleepTime+"; sleepSoFar: "+sleepSoFar);
                Thread.sleep(sleepDifference);
            } catch (InterruptedException ex) {
                // ignore
            }

            sleepSoFar += sleepDifference;

            customerQueue.add(customer);
        }

        System.out.println("Customers intialized.");
    }

    private static Vector<UAEDriversLicense> convertToVector(
            SynchronizedQueue<UAEDriversLicense> successQueue) {
        Vector<UAEDriversLicense> successVector = new Vector<UAEDriversLicense>();
        while (!successQueue.isEmpty()) {
            successVector.add(successQueue.poll());
        }
        return successVector;
    }

    private static Vector<Object[]> makeCustomerVector(int numCustomers) {
        String firstName;
        String lastName;
        String nationality;
        char gender;
        Date dateOfBirth;
        Date expiryDate;
        String idNumber;

        Vector<Object[]> customerObjectVector = new Vector<Object[]>();
        long sleepSoFar = 0;

        for (int i = 0; i < numCustomers; i++) {
            firstName = randomName();
            lastName = randomName();
            nationality = randomNationality();
            gender = randomGender();
            dateOfBirth = randomDate(new Date(0));
            expiryDate = randomDate(new Date(System.currentTimeMillis()));
            idNumber = randomId();

            EmiratesId emiratesId = new EmiratesId(
                    firstName, lastName, nationality, gender, dateOfBirth,
                    expiryDate, idNumber);
            DriversLicense driversLicense = new DriversLicense(
                    firstName, lastName, nationality, gender, dateOfBirth,
                    expiryDate);
            Passport passport = new Passport(
                    firstName, lastName, nationality, gender, dateOfBirth,
                    expiryDate);
            
            if ((i % 11) == 0) {
                passport = null;
            }
            if ((i % 13) == 0) {
                driversLicense = null;
            }
            if ((i % 15) == 0) {
                emiratesId = null;
            }

            Customer newCustomer = new Customer(
                    emiratesId, driversLicense, passport,
                    null, null);

            long randomSleep = randomSleep();
            long cumulativeSleep = randomSleep + sleepSoFar;
            //System.out.println("random sleep: "+randomSleep+"; cumulativeSleep: "+cumulativeSleep+"; sleepSoFar: "+sleepSoFar);

            Object[] customerObject = {cumulativeSleep, newCustomer};
            sleepSoFar += randomSleep;

            //System.out.println("New customer: "+newCustomer);
            customerObjectVector.add(customerObject);
        }

        System.out.println("Customers made.");
        return customerObjectVector;
    }

    private static String randomName() {
        String randomName = "";
        for (int i = 0; i < 6; i++) {
            char addChar = ALPHABET[(int) (Math.random()*26)];
            if (i == 0) {
                addChar = Character.toUpperCase(addChar);
            }
            randomName += addChar+"";
        }
        return randomName;
    }

    private static String randomNationality() {
        String randomNationality = "";
        for (int i = 0; i < 3; i++) {
            char addChar = ALPHABET[(int) (Math.random()*26)];
            Character.toUpperCase(addChar);
            randomNationality += addChar;
        }
        return randomNationality;
    }

    private static char randomGender() {
        char randomGender = 'X';
        randomGender = (((int) (Math.random()*2)) == 0) ? 'M' : 'F';
        return randomGender;
    }

    private static Date randomDate(Date startDate) { // date 1-4 years from start
        Date randomDate = new Date(
                startDate.getTime()+(((long) Math.random())*94672800L+31557600L));
        return randomDate;
    }

    private static String randomId() {
        int randomId = 0;
        for (int i = 0; i < 9; i++) {
            int currentPower = ((int) Math.pow((double) 10, (double) i));
            randomId += (currentPower * ((int) (Math.random()*10)));
        }
        return randomId+"";
    }

    private static long randomSleep() {
        long randomSleep = (long) ((Math.random()*1000)+1000);
        return randomSleep;
    }
}

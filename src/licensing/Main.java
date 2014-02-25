package licensing;

import java.util.*;

public class Main {
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final int NUM_CUSTOMERS = 100;
    
    private static final int NUM_LICENSORS = 5;
    private static final int NUM_EYE_TESTERS = 5;
    private static final int NUM_TRANSLATORS = 5;
    private static final int NUM_PRINTERS = 5;

    private static SynchronizedQueue<Customer> customerQueue; // handled by Receptionist
    private static SynchronizedQueue<Customer> licensingQueue; // handled by Licensor
    private static SynchronizedQueue<Customer> eyeTestingQueue; // handled by EyeTester
    private static SynchronizedQueue<Customer> translatingQueue; // handled by Translator
    private static SynchronizedQueue<Customer> printingQueue; // handled by PrintingAgent

    private static SynchronizedQueue<UAEDriversLicense> successQueue;
    private static SynchronizedQueue<Customer> failureQueue;

    public static void main(String args[]) {
        initializeCustomers(customerQueue, NUM_CUSTOMERS);

        (new Thread(new Receptionist(
                customerQueue,
                licensingQueue, eyeTestingQueue, translatingQueue,
                successQueue, failureQueue, NUM_CUSTOMERS
                ))).start();

        for (int i = 0; i < NUM_LICENSORS; i++) {
            (new Thread(new Licensor(
                    licensingQueue,
                    eyeTestingQueue, translatingQueue, printingQueue,
                    successQueue, failureQueue, NUM_CUSTOMERS
                    ))).start();
        }

        for (int i = 0; i < NUM_EYE_TESTERS; i++) {
            (new Thread(new EyeTester(
                    eyeTestingQueue,
                    translatingQueue, licensingQueue,
                    successQueue, failureQueue, NUM_CUSTOMERS
                    ))).start();
        }

        for (int i = 0; i < NUM_TRANSLATORS; i++) {
            (new Thread(new Translator(
                    translatingQueue,
                    eyeTestingQueue, licensingQueue,
                    successQueue, failureQueue, NUM_CUSTOMERS
                    ))).start();
        }

        for (int i = 0; i < NUM_PRINTERS; i++) {
            (new Thread(new PrintingAgent(
                    printingQueue,
                    successQueue, failureQueue, NUM_CUSTOMERS
                    ))).start();
        }
    }

    private static void initializeCustomers(
            SynchronizedQueue customerQueue, int numCustomers
            ) {
        String firstName = randomName();
        String lastName = randomName();
        String nationality = randomNationality();
        char gender = randomGender();
        Date dateOfBirth = randomDate(new Date(0));
        Date expiryDate = randomDate(new Date(System.currentTimeMillis()));
        int idNumber = randomId();

        EmiratesId emiratesId = new EmiratesId(
                firstName,lastName,nationality,gender,dateOfBirth,expiryDate,
                idNumber
                );
        DriversLicense driversLicense = new DriversLicense(
                firstName,lastName,nationality,gender,dateOfBirth,expiryDate
                );
        Passport passport = new Passport(
                firstName,lastName,nationality,gender,dateOfBirth,expiryDate
                );

        for (int i = 0; i < numCustomers; i++) {
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
                    null, null
                    );

            customerQueue.add(newCustomer);
        }
    }

    private static String randomName() {
        String randomName = "";
        for (int i = 0; i < 6; i++) {
            char addChar = ALPHABET[(int) (Math.random()*26)];
            if (i == 0) Character.toUpperCase(addChar);
            randomName += addChar;
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

    private static int randomId() {
        int randomId = 0;
        for (int i = 0; i < 9; i++) {
            int currentPower = ((int) Math.pow((double) 10, (double) i));
            randomId += (currentPower * ((int) (Math.random()*10)));
        }
        return randomId;
    }
}

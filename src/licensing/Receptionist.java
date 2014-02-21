package licensing;
import java.util.LinkedList;
import java.util.Vector;

public class Receptionist extends AbstractAgent{
    SynchronizedQueue customerQueue;
    SynchronizedQueue licensingQueue;
    SynchronizedQueue eyeTestingQueue;
    SynchronizedQueue translatingQueue;
    SynchronizedQueue successQueue;
    SynchronizedQueue failureQueue;
    int numCustomers;

    // Check if a customer has all the correct documents (but not if those documents are correct).
    // It has a 50% failure rate of checking, to simulate people reporting bad answers.
    private boolean checkCustomer(Customer customer) {
        return ((customer.driversLicense != null && customer.emiratesId != null && customer.passport != null) ||
                (((int) (Math.random()*2)) == 1) );
    }





    public void run();
}

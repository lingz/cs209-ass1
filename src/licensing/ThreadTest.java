/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package licensing;

/**
 *
 * @author Zbynda
 */
public class ThreadTest implements Runnable {
    private int counter = 0;

    public ThreadTest(int counter) {
        this.counter = counter;
    }

    public void run() {
        try {
            Thread.sleep((long) (Math.random()%1000));
        } catch (java.lang.InterruptedException ex) {}

        System.out.println("Hello from a thread #"+counter+"!");
    }
}

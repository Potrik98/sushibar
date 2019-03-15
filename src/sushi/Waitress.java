package sushi;

import java.util.Optional;
import java.util.Random;

import static sushi.StreamUtils.uncheckRun;

/**
 * This class implements the consumer part of the producer/consumer problem.
 * One waitress instance corresponds to one consumer.
 */
public class Waitress implements Runnable {
    private final WaitingArea waitingArea;
    private final int waitressId;

    /**
     * Creates a new waitress. Make sure to save the parameter in the class
     *
     * @param waitingArea The waiting area for customers
     */
    public Waitress(final WaitingArea waitingArea, final int waitressId) {
        this.waitingArea = waitingArea;
        this.waitressId = waitressId;
    }

    /**
     * This is the code that will run when a new thread is
     * created for this instance
     */
    @Override
    public void run() {
        final Random random = new Random();
        while (SushiBar.isOpen) {
            SushiBar.write(this + " is checking for customers");
            Optional<Customer> customer = waitingArea.pollNext();
            if (customer.isPresent()) {
                final Customer c = customer.get();
                SushiBar.write(this + " is serving customer " + c.getCustomerID());
                final int t = random.nextInt(SushiBar.waitressWait);
                uncheckRun(() -> Thread.sleep(t));
                c.order();
                SushiBar.write(this + " done serving customer");
            } else {
                SushiBar.write(this + " waiting for customers");
                synchronized (waitingArea) {
                    uncheckRun(waitingArea::wait);
                }
                SushiBar.write(this + " notified");
            }
        }
        SushiBar.write(this + " stopping, notifying door");
        synchronized (Door.doorMonitor) {
            Door.doorMonitor.notify();
        }
    }

    public String toString() {
        return "Waitress " + waitressId;
    }
}

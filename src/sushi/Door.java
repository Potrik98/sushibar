package sushi;

import java.util.Random;

import static sushi.StreamUtils.uncheckRun;

/**
 * This class implements the Door component of the sushi bar assignment
 * The Door corresponds to the Producer in the producer/consumer problem
 */
public class Door implements Runnable {
    public static Thread doorThread;
    public static final Object doorMonitor = new Object();
    private final WaitingArea waitingArea;

    /**
     * Creates a new Door. Make sure to save the
     * @param waitingArea   The customer queue waiting for a seat
     */
    public Door(final WaitingArea waitingArea) {
        this.waitingArea = waitingArea;
    }

    /**
     * This method will run when the door thread is created (and started)
     * The method should create customers at random intervals and try to put them in the waiting area
     */
    @Override
    public void run() {
        final Random rand = new Random();
        while (SushiBar.isOpen) {
            uncheckRun(() -> Thread.sleep(SushiBar.doorWait));
            final int r = rand.nextInt(100);
            if (r < 20) {
                SushiBar.write("Door: Creating new customer");
                final Customer c = new Customer();
                final boolean success = waitingArea.enter(c);

                if (!success) {
                    SushiBar.write("Door: Waiting area full!");
                    synchronized (doorMonitor) {
                        uncheckRun(doorMonitor::wait);
                    }
                }
            }
        }
        SushiBar.write("Door closing");
        synchronized (waitingArea) {
            waitingArea.notifyAll();
        }
    }

    // Add more methods as you see fit
}

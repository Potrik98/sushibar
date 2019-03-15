package sushi;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class implements a waiting area used as the bounded buffer, in the producer/consumer problem.
 */
public class WaitingArea {
    private final LinkedBlockingQueue<Customer> queue;

    /**
     * Creates a new waiting area.
     *
     * @param size The maximum number of Customers that can be waiting.
     */
    public WaitingArea(final int size) {
        queue = new LinkedBlockingQueue<>(size);
    }

    /**
     * This method should put the customer into the waitingArea
     *
     * @param customer A customer created by Door, trying to enter the waiting area
     */
    public boolean enter(final Customer customer) {
        final boolean result = queue.offer(customer);
        SushiBar.write("Waiting Area: new customer added: " + result);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    /**
     * @return The customer that is first in line.
     */
    public Optional<Customer> pollNext() {
        final Customer c = queue.poll();
        SushiBar.write("Waiting Area: polled customer from queue, left in queue: " + queue.size());
        SushiBar.write("Waiting Area: notifying door");
        synchronized (Door.doorMonitor) {
            Door.doorMonitor.notify();
        }
        return Optional.ofNullable(c);
    }

    // Add more methods as you see fit
}

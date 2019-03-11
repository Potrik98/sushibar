package sushi;

import java.util.Random;
import java.util.UUID;

/**
 * This class implements a customer, which is used for holding data and update the statistics
 *
 */
public class Customer {
    private final UUID id;

    /**
     *  Creates a new Customer.
     *  Each customer should be given a unique ID
     */
    public Customer() {
        id = UUID.randomUUID();
        SushiBar.customerCounter.increment();
    }

    /**
     * Here you should implement the functionality for ordering food as described in the assignment.
     */
    public synchronized void order() {
        final Random random = new Random();
        final int t = random.nextInt(50);
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int orderCount = 1 + random.nextInt(SushiBar.maxOrder - 1);
        final int takeAway = random.nextInt(orderCount);
        SushiBar.write("Customer " + id.toString() + " ordered " + orderCount + " pieces; takeaway " + takeAway);
        SushiBar.servedOrders.add(orderCount - takeAway);
        SushiBar.takeawayOrders.add(takeAway);
        SushiBar.totalOrders.add(orderCount);
    }

    /**
     *
     * @return Should return the customerID
     */
    public final UUID getCustomerID() {
        return id;
    }

    // Add more methods as you see fit
}

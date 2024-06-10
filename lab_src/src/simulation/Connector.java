package src.simulation;

import java.awt.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Connector class extends the Visitable class.
 * It represents a connector in the simulation.
 * It uses a ReentrantLock to allow multiple entries from the same thread.
 */
public class Connector extends Visitable {
    // Reentrant lock to allow multiple entries from the same thread
    private ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * Constructor for the Connector class.
     *
     * @param area The area of the connector.
     */
    public Connector(Rectangle area) {
        super(null, area); // super because Visitable is the parent class
        color = Color.RED;
    }

    /**
     * Entry method for the Connector class.
     * It locks the reentrant lock.
     *
     * @throws InterruptedException If the thread is interrupted.
     */
    @Override
    public void onEntry(Visitor v) throws InterruptedException {
        reentrantLock.lock();
    }

    /**
     * Exit method for the Connector class.
     * It unlocks the reentrant lock.
     */
    @Override
    public void onExit(Visitor v) {
        reentrantLock.unlock();
    }
}
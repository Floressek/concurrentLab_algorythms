package src.simulation;

import java.awt.*;
import java.util.concurrent.locks.ReentrantLock;

public class Connector extends Visitable {
    // Reentrant lock to allow multiple entries from the same thread
    private ReentrantLock reentrantLock = new ReentrantLock();

    // Constructor
    public Connector(Rectangle area) {
        super(area);
        color = Color.RED;
    }

    // Entry method
    void onEntry() throws InterruptedException {
        reentrantLock.lock();
    }

    // Exit method
    void onExit() {
        reentrantLock.unlock();
    }
}
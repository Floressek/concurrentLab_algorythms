package src.lab7_8.Zadanie_1_v2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final int bufferSize;
    private final String[] buffer;
    private int count = 0, in = 0, out = 0;
    private boolean allProduced = false;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public Buffer(int size) {
        this.bufferSize = size;
        this.buffer = new String[bufferSize];
    }

    public void setAllProduced() {
        lock.lock();
        try {
            allProduced = true;
            notEmpty.signalAll(); // Wake up all waiting consumers when production is complete
        } finally {
            lock.unlock();
        }
    }

    public void insert(String item, int producerId) throws InterruptedException {
        lock.lock();
        try {
            while (count == bufferSize) {
                notFull.await();
            }
            buffer[in] = item;
            System.out.printf("[P-%d, %d, %d, %d]\n", producerId, count, Integer.parseInt(item), in);
            in = (in + 1) % bufferSize;
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public void remove(int consumerId) throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                if (allProduced) {
                    return; // Signal consumer to stop
                }
                notEmpty.await();
            }
            String item = buffer[out];
            System.out.printf("[K-%d, %d, %d, %d]\n", consumerId, count, Integer.parseInt(item), out);
            out = (out + 1) % bufferSize;
            count--;
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
}

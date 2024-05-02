package src.lab7_8.Zadanie_1;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


public class Buffer {
    private final int bufferSize;
    private final String[] buffer;
    private int count = 0, in = 0, out = 0;
    private boolean allProduced = false; // Flag to indicate all producers have finished

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public Buffer(int size){
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

    public void insert(String item) throws InterruptedException {
        lock.lock();
        try{
            while(count == bufferSize){ // while buffer is full
                notFull.await();
            }
            buffer[in] = item; // insert item into buffer
            in = (in + 1) % bufferSize; // increment in index and wrap around if necessary
            count++;
            notEmpty.signal(); // signal consumer that buffer is not empty
        } finally {
            lock.unlock();
        }
    }

    public String remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                if (allProduced) { // Check if no more items will be produced
                    return null; // Signal consumer to stop
                }
                notEmpty.await(); // Wait only if more items are expected
            }
            String item = buffer[out];
            out = (out + 1) % bufferSize;
            count--;
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }


}

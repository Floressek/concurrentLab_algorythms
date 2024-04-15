package Zadanie_5_lab2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CircularBuffer {
    // Shared space for consumer and producer
    private final String[] buffer;
    // Cyclic buffering, head is for consumer (deleting), tail is for producer (adding)
    private int head = 0;
    private int tail = 0;
    private final Semaphore semFree;
    private final Semaphore semFull;
    private final AtomicInteger producersFinished = new AtomicInteger(0);
    private int numberOfConsumers;
    private int totalProducers;

    public CircularBuffer(int size, int totalProducers, int numberOfConsumers){
        buffer = new String[size];
        semFree = new Semaphore(size);
        // Initially, the buffer is empty
        semFull = new Semaphore(0);
        this.totalProducers = totalProducers;
        this.numberOfConsumers = numberOfConsumers;
    }

    public void producersFinished() throws InterruptedException {
        if (producersFinished.incrementAndGet() == totalProducers) {
            for (int i = 0; i < numberOfConsumers; i++) {
                produce("POISON_PILL");
            }
        }
    }
    public void produce(String item) throws InterruptedException {
        // Wait until there is a free space in the buffer
        semFree.acquire();
        synchronized (this) {
            buffer[tail] = item;
            // Move the tail to the next position
            tail = (tail + 1) % buffer.length;
        }
        semFull.release();
    }

    public String consume() throws InterruptedException{
        // Wait until there is an item in the buffer
        semFull.acquire();
        String item;
        synchronized (this) {
            item = buffer[head];
            // Still moves the node to the next position(same as tail) difference in semantics
            head = (head + 1) % buffer.length;
        }
        semFree.release();
        return item;
    }
}

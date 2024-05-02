package src.lab7_8.Zadanie_3;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Philosophers implements Runnable {
    private int id;
    private ReentrantLock leftFork;
    private ReentrantLock rightFork;
    private DiningMonitor monitor;
    private int repetition;
    private static final Random random = new Random();

    public Philosophers(int id, ReentrantLock leftFork, ReentrantLock rightFork, DiningMonitor monitor, int repetitions) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.monitor = monitor;
        this.repetition = repetitions;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < repetition; i++) {
                think();
                eat(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking"); //todo remove this line
        Thread.sleep(random.nextInt(11) + 5);
    }

    private void eat(int count) throws InterruptedException {
        leftFork.lock();
        rightFork.lock();
        try {
            monitor.startEating(id, count);
            Thread.sleep(random.nextInt(5) + 1);
            monitor.stopEating(id, count);
        } finally {
            leftFork.unlock();
            rightFork.unlock();
        }
    }
}

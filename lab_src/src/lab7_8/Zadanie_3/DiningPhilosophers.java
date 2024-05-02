package src.lab7_8.Zadanie_3;

import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    public static void main(String[] args) {
        int philosophersCount = 5;
        ReentrantLock[] forks = new ReentrantLock[philosophersCount];
        Thread[] philosophers = new Thread[philosophersCount];
        DiningMonitor monitor = new DiningMonitor(philosophersCount);

        for (int i = 0; i < philosophersCount; i++) {
            forks[i] = new ReentrantLock();
        }

        for (int i = 0; i < philosophersCount; i++) {
            ReentrantLock leftFork = forks[i];
            ReentrantLock rightFork = forks[(i + 1) % philosophersCount];
            philosophers[i] = new Thread(new Philosophers(i + 1, leftFork, rightFork, monitor, 100));
            philosophers[i].start();
        }

        for (int i = 0; i < philosophersCount; i++) {
            try {
                philosophers[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

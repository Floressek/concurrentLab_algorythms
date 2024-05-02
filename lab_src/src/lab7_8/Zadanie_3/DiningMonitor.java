package src.lab7_8.Zadanie_3;

import java.util.concurrent.locks.ReentrantLock;

public class DiningMonitor {
    private boolean[] forks;
    private ReentrantLock lock = new ReentrantLock();
    private int philosophersAtTheTable = 0;

    public DiningMonitor(int numForks) {
        forks = new boolean[numForks];
    }

    public void startEating(int philosophersId, int count) {
        lock.lock();
        try {
            int leftIndex = philosophersId - 1; // Assuming forks are indexed from 0
            int rightIndex = philosophersId % forks.length;
            // Check if both forks are available
            if (!forks[leftIndex] && !forks[rightIndex]) {
                forks[leftIndex] = true; // left fork
                forks[rightIndex] = true; // right fork
                philosophersAtTheTable++;
                printState(">>> (1)", philosophersId, count);
            } else {
                System.out.printf("Philosopher %d is unable to eat\n", philosophersId);
            }

        } finally {
            lock.unlock();
        }
    }

    public void stopEating(int philosophersId, int count) {
        lock.lock();
        try {
            int leftIndex = philosophersId - 1;
            int rightIndex = philosophersId % forks.length;
            forks[leftIndex] = false;
            forks[rightIndex] = false;
            philosophersAtTheTable--;
            printState("<<< (1)", philosophersId, count);
        } finally {
            lock.unlock();
        }
    }

    private void printState(String prefix, int philosopherId, int count) {
        // Forming the status array string
        StringBuilder forkStatusBuilder = new StringBuilder("[");
        for (int i = 0; i < forks.length; i++) {
            forkStatusBuilder.append(forks[i] ? 1 : 0);
            if (i < forks.length - 1) {
                forkStatusBuilder.append(", ");
            }
        }
        forkStatusBuilder.append("]");

        // Printing the formatted message
        System.out.printf("%s [F-%d, %d] :: %s – %d\n", prefix, philosopherId, count,
                forkStatusBuilder.toString(), philosophersAtTheTable);
    }
}

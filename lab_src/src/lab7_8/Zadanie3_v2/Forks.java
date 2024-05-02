// Forks.java
package src.lab7_8.Zadanie3_v2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Forks {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition[] fork;
    boolean[] occupied;
    private final Condition waiter;
    private int present;

    public Forks(int numberOfForks) {
        fork = new Condition[numberOfForks];
        occupied = new boolean[numberOfForks];
        waiter = lock.newCondition();
        present = 0;

        for (int i = 0; i < numberOfForks; i++) {
            fork[i] = lock.newCondition();
            occupied[i] = false;
        }
    }

    private void printState(String phase, int id, int repetition) {
        String forksState = "[";
        for (int j = 0; j < occupied.length; j++) {
            forksState += occupied[j] ? "1" : "0";
            if (j < occupied.length - 1) {
                forksState += ", ";
            }
        }
        forksState += "]";
        System.out.println(phase + " [F-" + id + ", " + repetition + "] :: " + forksState + " – " + present);
    }

    public void take(int i, int repetition) throws InterruptedException {
        lock.lock();
        try {
            present++;
            printState(">>> (1)", i, repetition);
            while (occupied[i] || occupied[(i + 1) % occupied.length]) {
                if (occupied[i]) {
                    fork[i].await();
                }
                if (occupied[(i + 1) % occupied.length]) {
                    fork[(i + 1) % occupied.length].await();
                }
            }
            occupied[i] = true;
            occupied[(i + 1) % occupied.length] = true;
            printState(">>> (2)", i, repetition);
        } finally {
            lock.unlock();
        }
    }

    public void putDown(int i, int repetition) {
        lock.lock();
        try {
            printState("<<< (1)", i, repetition);
            occupied[i] = false;
            fork[i].signal();
            occupied[(i + 1) % occupied.length] = false;
            fork[(i + 1) % occupied.length].signal();
            present--;
            printState("<<< (2)", i, repetition);
        } finally {
            lock.unlock();
        }
    }
}
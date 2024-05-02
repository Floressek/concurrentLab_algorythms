// Main.java
package src.lab7_8.Zadanie3_v2;


public class Main_fin {
    public static void main(String[] args) {
        final int numberOfPhilosophers = 5;
        final int repetitions = 100;
        // Thinking time
        Philosopher.a = 8;
        Philosopher.b = 12;
        // Meal time
        Philosopher.c = 2;
        Philosopher.d = 6;

        Forks forks = new Forks(numberOfPhilosophers);
        Thread[] philosophers = new Thread[numberOfPhilosophers];
        for (int i = 0; i < numberOfPhilosophers; i++) {
            philosophers[i] = new Thread(new Philosopher(i, forks, repetitions));
            philosophers[i].start();
        }
        for (Thread philosopher : philosophers) {
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

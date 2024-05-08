package src.lab5_6.Exercise_4;

public class Main {
    public static void main(String[] args) {
        DiningPhilosophers.a = 5;
        DiningPhilosophers.b = 15;
        DiningPhilosophers.c = 1;
        DiningPhilosophers.d = 5;

        DiningPhilosophers dp = new DiningPhilosophers();
        Thread[] philosophers = new Thread[5];

        for (int i = 0; i < philosophers.length; i++) {
            final int id = i;
            philosophers[i] = new Thread(() -> dp.philosopher(id, 10));
            philosophers[i].start();
        }

        try {
            Thread.sleep(1000);  // Ustawienie limitu czasowego na 10 sekund.
            for (Thread philosopher : philosophers) {
                philosopher.interrupt();  // Przerwanie wątków po 10 sekundach.
                philosopher.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Philosopher.java
package src.lab7_8.Zadanie3_v2;

import java.util.Random;

public class Philosopher implements Runnable {
    private final int id;
    static int a;
    static int b;
    static int c;
    static int d;
    private final Forks forks;
    private final int repetitions;
    private final Random random = new Random();

    public Philosopher(int id, Forks forks, int repetitions) {
        this.id = id;
        this.forks = forks;
        this.repetitions = repetitions;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < repetitions; i++) {
                // Thinking
                Thread.sleep(random.nextInt(b) + a); // 5-15 ms
                // Pick up forks and eat
                forks.take(id, i + 1);
                Thread.sleep(random.nextInt(d) + c); // 1-5 ms
                // Put down forks
                forks.putDown(id, i + 1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


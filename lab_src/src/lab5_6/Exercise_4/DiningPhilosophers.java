package src.lab5_6.Exercise_4;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class DiningPhilosophers {
    private final Semaphore[] forks = new Semaphore[5];
    private final Semaphore allowEntry = new Semaphore(4);
    private final int[] forksOwner = new int[5];
    public static int a, b, c, d;  // Teraz wartości te będą ustawiane w Main.

    public DiningPhilosophers() {
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Semaphore(1);
            forksOwner[i] = -1;
        }
    }

    public void philosopher(int id, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            doPrivateMatters();
            eat(id, i);
        }
    }

    private void doPrivateMatters() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(a, b + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void eat(int id, int repetition) {
        try {
            allowEntry.acquire();
            forks[id].acquire();
            forksOwner[id] = id;
            forks[(id + 1) % 5].acquire();
            forksOwner[(id + 1) % 5] = id;

            logState(">>> [F-" + id + ", " + repetition + "]", true);

            Thread.sleep(ThreadLocalRandom.current().nextInt(c, d + 1));

            logState("<<< [F-" + id + ", " + repetition + "]", false);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            forksOwner[id] = -1;
            forks[id].release();
            forksOwner[(id + 1) % 5] = -1;
            forks[(id + 1) % 5].release();
            allowEntry.release();
        }
    }

    private synchronized void logState(String prefix, boolean isEating) {
        int philosophersAtTable = 0;
        for (int j = 0; j < forksOwner.length; j++) {
            if (forksOwner[j] != -1) philosophersAtTable++;
        }
        philosophersAtTable /= 2;

        System.out.print(prefix + " :: licz_fil_przy_stole=" + philosophersAtTable + ", [");
        for (int j = 0; j < forksOwner.length; j++) {
            System.out.print("w" + j + "=" + (forksOwner[j] != -1 ? 1 : 0) + (j < forksOwner.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }
}

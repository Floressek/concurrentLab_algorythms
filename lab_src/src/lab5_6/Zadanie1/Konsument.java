package src.lab5_6.Zadanie1;

import java.util.concurrent.Semaphore;

public class Konsument extends Thread {
    int id;
    Bufor bufor;
    Semaphore sem;
    static int c, d;

    public Konsument(Semaphore semaphor, int id, Bufor buffer) {
        this.id = id;
        bufor = buffer;
        sem = semaphor;
    }

    public void run() {
        for (int i = 1; i <= 40; i++) {
            try {
                long start = System.currentTimeMillis();
                Thread.sleep((long) (Math.random() * (d - c + 1) + c));

                sem.acquire();
                System.out.println("[K-" + id + ", " + i + ", " + bufor.pozycja() + "] :: " + bufor.odczyt());
                sem.release();

                long end = System.currentTimeMillis();
            } catch (InterruptedException ignored) {
            }
        }
    }
}

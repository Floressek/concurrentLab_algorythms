package src.lab5_6.Zadanie1;

import java.util.concurrent.Semaphore;

public class Producent extends Thread {
    int id;
    Bufor bufor;
    Semaphore sem;
    static int a, b;

    public Producent(Semaphore semaphor, int id, Bufor buffer) {
        this.id = id;
        bufor = buffer;
        sem = semaphor;
    }

    public void run() {
        for (int i = 1; i <= 50; i++) {
            try {
                long start = System.currentTimeMillis();
                Thread.sleep((long) (Math.random() * (b - a + 1) + a));
                String dana = "Dana=[P-" + id + ", " + i + ", ";
                sem.acquire();
                bufor.dodaj(dana, Math.random() * 99);
                sem.release();

                long end = System.currentTimeMillis();
            } catch (InterruptedException ignored) {
            }
        }
    }
}

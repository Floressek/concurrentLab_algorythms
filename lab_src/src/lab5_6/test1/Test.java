package src.lab5_6.test1;

import java.util.concurrent.Semaphore;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        int m = 4;
        int n = 5;
        Bufor buff = new Bufor();
        Semaphore sem = new Semaphore(1);

        Thread[] k = new Thread[m];
        for (int i = 0; i < k.length; i++) {
            k[i] = new Producent(sem, (i + 1), buff);
            k[i].start();
        }

        for (Thread value : k) {
            try {
                value.join();
            } catch (InterruptedException ignored) {
            }
        }

        Thread[] p = new Thread[n];
        for (int i = 0; i < p.length; i++) {
            p[i] = new Konsument(sem, (i + 1), buff);
            p[i].start();
            //     Thread.sleep(1000);
        }

        for (Thread thread : p) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }


    }
}

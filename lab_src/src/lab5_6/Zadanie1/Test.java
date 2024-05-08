package src.lab5_6.Zadanie1;

import java.util.concurrent.Semaphore;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        int m = 4;
        int n = 5;
        Producent.a = 10;
        Producent.b = 20;
        Konsument.c = 5;
        Konsument.d = 15;

        Bufor buff = new Bufor();
        Semaphore sem = new Semaphore(1);

        Thread[] producenci = new Thread[m];
        for (int i = 0; i < producenci.length; i++) {
            producenci[i] = new Producent(sem, (i + 1), buff);
            producenci[i].start();
        }

        for (Thread producent : producenci) {
            try {
                producent.join();
            } catch (InterruptedException ignored) {
            }
        }

        Thread[] konsumenci = new Thread[n];
        for (int i = 0; i < konsumenci.length; i++) {
            konsumenci[i] = new Konsument(sem, (i + 1), buff);
            konsumenci[i].start();
        }

        for (Thread konsument : konsumenci) {
            try {
                konsument.join();
            } catch (InterruptedException ignored) {
            }
        }
    }
}

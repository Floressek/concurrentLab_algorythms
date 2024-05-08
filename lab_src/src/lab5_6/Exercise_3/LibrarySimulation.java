package src.lab5_6.Exercise_3;

import java.util.concurrent.Semaphore;
import java.util.Random;

public class LibrarySimulation {

    private static int cp = 0; // liczba czytelników w poczekalni
    private static int cc = 0; // liczba czytelników w czytelni
    private static int pp = 0; // liczba pisarzy w poczekalni
    private static int pc = 0; // liczba pisarzy w czytelni

    private static Semaphore czyt = new Semaphore(0);
    private static Semaphore pis = new Semaphore(0);
    private static Semaphore chron = new Semaphore(1); // BinarySemaphore

    public static void main(String[] args) {
        int m = 5; // czytelnicy
        int n = 2; // pisarze
        int iterations = 10; // liczba powtórzeń działania wątku

        Thread[] readers = new Thread[m];
        Thread[] writers = new Thread[n];

        for (int i = 0; i < m; i++) {
            final int id = i;
            readers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < iterations; j++) {
                        personalMatters();
                        chron.acquire();
                        if (pp + pc == 0) {
                            cc++;
                            czyt.release();
                        } else {
                            cp++;
                        }
                        chron.release();
                        czyt.acquire();
                        read(id, j);
                        chron.acquire();
                        cc--;
                        if (cc == 0 && pp > 0) {
                            pc = 1;
                            pp--;
                            pis.release();
                        }
                        chron.release();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (int i = 0; i < n; i++) {
            final int id = i;
            writers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < iterations; j++) {
                        personalMatters();
                        chron.acquire();
                        if (cc + pc == 0) {
                            pc = 1;
                            pis.release();
                        } else {
                            pp++;
                        }
                        chron.release();
                        pis.acquire();
                        write(id, j);
                        chron.acquire();
                        pc = 0;
                        if (cp > 0) {
                            while (cp > 0) {
                                cc++;
                                cp--;
                                czyt.release();
                            }
                        } else if (pp > 0) {
                            pc = 1;
                            pp--;
                            pis.release();
                        }
                        chron.release();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread writer : writers) {
            writer.start();
        }
    }

    private static void personalMatters() throws InterruptedException {
        Random rand = new Random();
        Thread.sleep(rand.nextInt(11) + 5); // losowy czas w przedziale <5, 15> milisekund
    }

    private static void read(int id, int iteration) throws InterruptedException {
        System.out.println(">>> [C-" + id + ", " + iteration + "]");
        Random rand = new Random();
        Thread.sleep(rand.nextInt(5) + 1); // losowy czas w przedziale <1, 5> milisekund
        System.out.println("<<< [C-" + id + ", " + iteration + "]");
    }

    private static void write(int id, int iteration) throws InterruptedException {
        System.out.println("==> [P-" + id + ", " + iteration + "]");
        Random rand = new Random();
        Thread.sleep(rand.nextInt(5) + 1); // losowy czas w przedziale <1, 5> milisekund
        System.out.println("<== [P-" + id + ", " + iteration + "]");
    }
}

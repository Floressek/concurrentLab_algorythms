package src.lab5_6.Exercise_3;

import java.util.concurrent.Semaphore;
import java.util.Random;

public class LibrarySimulation_v2 {

    public static void main(String[] args) {
        int m = 5; // liczba czytelników
        int n = 2; // liczba pisarzy
        int K = 3; // pojemność czytelni
        int a = 5; // dolny limit czasu dla spraw własnych czytelników
        int b = 15; // górny limit czasu dla spraw własnych czytelników
        int c = 1; // dolny limit czasu dla synchronizacji czytelników i pisarzy
        int d = 5; // górny limit czasu dla synchronizacji czytelników i pisarzy
        int iterations = 10; // liczba powtórzeń działania wątku

        Library library = new Library(K);
        Thread[] readers = new Thread[m];
        Thread[] writers = new Thread[n];

        for (int i = 0; i < m; i++) {
            readers[i] = new Reader(i, iterations, a, b, c, d, library);
            readers[i].start();
        }

        for (int i = 0; i < n; i++) {
            writers[i] = new Writer(i, iterations, a, b, c, d, library);
            writers[i].start();
        }
    }
}

class Library {
    private int K;
    private int cp = 0; // liczba czytelników w poczekalni
    private int cc = 0; // liczba czytelników w czytelni
    private int pp = 0; // liczba pisarzy w poczekalni
    private int pc = 0; // liczba pisarzy w czytelni

    private final Semaphore czyt;
    private final Semaphore pis;
    private final Semaphore chron;

    public Library(int K) {
        this.K = K;
        czyt = new Semaphore(0);
        pis = new Semaphore(0);
        chron = new Semaphore(1); // BinarySemaphore
    }


    public void setCp(int cp) {
        this.cp = cp;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }


    public int getK() {
        return K;
    }

    public void setK(int K) {
        this.K = K;
    }

    public Semaphore getCzyt() {
        return czyt;
    }

    public Semaphore getPis() {
        return pis;
    }

    public Semaphore getChron() {
        return chron;
    }

    public int getCp() {
        return cp;
    }

    public void incrementCp() {
        this.cp++;
    }

    public int getCc() {
        return cc;
    }

    public void incrementCc() {
        this.cc++;
    }

    public void decrementCc() {
        this.cc--;
    }

    public int getPp() {
        return pp;
    }

    public void incrementPp() {
        this.pp++;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
}

class Reader extends Thread {
    private final int id;
    private final int iterations;
    private final int a;
    private final int b;
    private final int c;
    private final int d;
    private final Library library;

    public Reader(int id, int iterations, int a, int b, int c, int d, Library library) {
        this.id = id;
        this.iterations = iterations;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.library = library;
    }

    @Override
    public void run() {
        try {
            for (int j = 0; j < iterations; j++) {
                personalMatters();
                library.getChron().acquire();
                if (library.getPp() + library.getPc() == 0) {
                    library.incrementCc();
                    library.getCzyt().release();
                } else {
                    library.incrementCp();
                }
                library.getChron().release();
                library.getCzyt().acquire();
                read(id, j);
                library.getChron().acquire();
                library.decrementCc();
                if (library.getCc() == 0 && library.getPp() > 0) {
                    library.setPc(1);
                    library.setPp(library.getPp() - 1);
                    library.getPis().release();
                }
                library.getChron().release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void personalMatters() throws InterruptedException {
        Random rand = new Random();
        Thread.sleep(rand.nextInt(b - a + 1) + a); // losowy czas w przedziale <a, b> milisekund
    }

    private void read(int id, int iteration) throws InterruptedException {
        System.out.println(">>> [C-" + id + ", " + iteration + "]");
        Random rand = new Random();
        Thread.sleep(rand.nextInt(d - c + 1) + c); // losowy czas w przedziale <c, d> milisekund
        System.out.println("<<< [C-" + id + ", " + iteration + "]");
    }
}

class Writer extends Thread {
    private int id;
    private int iterations;
    private int a, b, c, d;
    private Library library;

    public Writer(int id, int iterations, int a, int b, int c, int d, Library library) {
        this.id = id;
        this.iterations = iterations;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.library = library;
    }

    @Override
    public void run() {
        try {
            for (int j = 0; j < iterations; j++) {
                personalMatters();
                library.getChron().acquire();
                if (library.getCc() + library.getPc() == 0) {
                    library.setPc(1);
                    library.getPis().release();
                } else {
                    library.incrementPp();
                }
                library.getChron().release();
                library.getPis().acquire();
                write(id, j);
                library.getChron().acquire();
                library.setPc(0);
                if (library.getCp() > 0) {
                    while (library.getCp() > 0) {
                        library.incrementCc();
                        library.setCp(library.getCp() - 1);
                        library.getCzyt().release();
                    }
                } else if (library.getPp() > 0) {
                    library.setPc(1);
                    library.setPp(library.getPp() - 1);
                    library.getPis().release();
                }
                library.getChron().release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void personalMatters() throws InterruptedException {
        Random rand = new Random();
        Thread.sleep(rand.nextInt(b - a + 1) + a); // losowy czas w przedziale <a, b> milisekund
    }

    private void write(int id, int iteration) throws InterruptedException {
        System.out.println("==> [P-" + id + ", " + iteration + "]");
        Random rand = new Random();
        Thread.sleep(rand.nextInt(d - c + 1) + c); // losowy czas w przedziale <c, d> milisekund
        System.out.println("<== [P-" + id + ", " + iteration + "]");
    }
}


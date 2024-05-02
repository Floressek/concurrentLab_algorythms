package src.lab7_8.Zadanie_2;

public class MainLibrary {
    public static void main(String[] args) {
        LibraryMonitor monitor = new LibraryMonitor();
        int m = 4; // liczba czytelników
        int n = 2; // liczba pisarzy
        int repetitions = 100;

        Thread[] readers = new Thread[m];
        Thread[] writers = new Thread[n];

        for (int i = 0; i < m; i++) {
            readers[i] = new Thread(new Reader(i + 1, repetitions, monitor));
            readers[i].start();
        }
        for (int i = 0; i < n; i++) {
            writers[i] = new Thread(new Writer(i + 1, repetitions, monitor));
            writers[i].start();
        }

        try {
            for (Thread reader : readers) {
                reader.join();
            }
            for (Thread writer : writers) {
                writer.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

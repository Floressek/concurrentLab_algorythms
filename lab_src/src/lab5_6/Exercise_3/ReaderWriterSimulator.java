package src.lab5_6.Exercise_3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class ReaderWriterSimulator {
    private int readingReaders = 0; // Liczba czytelników w czytelni
    private int writingWriters = 0; // Liczba pisarzy w czytelni

    private final Semaphore readersSemaphore = new Semaphore(3); // Liczba dostępów dla czytelników
    private final Semaphore writersSemaphore = new Semaphore(1); // Tylko jeden pisarz może wejść na raz

    private void logState(String prefix, int id, int iteration, String type) {
        System.out.printf("%s [%s-%d, %d] :: [%d, %d]%n",
                prefix, type, id, iteration,
                readingReaders, writingWriters);
    }

    public void reader(int id, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            privateMatters();
            try {
                readersSemaphore.acquire(); // Zajęcie dostępu dla czytelnika
                logState(">>>", id, i, "C");
                readingReaders++;
                logState("<<<", id, i, "C");
                readersSemaphore.release(); // Zwolnienie dostępu dla czytelnika
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            reading();
            readersSemaphore.release(); // Zwolnienie dostępu dla czytelnika
        }
    }

    public void writer(int id, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            privateMatters();
            try {
                writersSemaphore.acquire(); // Zajęcie dostępu dla pisarza
                logState(">>>", id, i, "P");
                writingWriters++;
                logState("<<<", id, i, "P");
                writersSemaphore.release(); // Zwolnienie dostępu dla pisarza
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            writing();
            writersSemaphore.release(); // Zwolnienie dostępu dla pisarza
        }
    }

    private void privateMatters() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5, 16));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void reading() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1, 6));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void writing() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1, 6));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

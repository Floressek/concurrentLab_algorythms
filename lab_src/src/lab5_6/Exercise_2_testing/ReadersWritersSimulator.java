package src.lab5_6.Exercise_2_testing;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class ReadersWritersSimulator {
    private int waitingReaders = 0; // Liczba czytelników w poczekalni
    private int readingReaders = 0; // Liczba czytelników w czytelni
    private int waitingWriters = 0; // Liczba pisarzy w poczekalni
    private int writingWriters = 0; // Liczba pisarzy w czytelni

    private Semaphore readersSemaphore = new Semaphore(0);
    private Semaphore writersSemaphore = new Semaphore(0);
    private ReentrantLock protectLock = new ReentrantLock(true);

    private void logState(String prefix, int id, int iteration, String type) {
        System.out.printf("%s [%s-%d, %d] :: [%d, %d, %d, %d]%n",
                prefix, type, id, iteration,
                readingReaders, waitingReaders,
                writingWriters, waitingWriters);
    }

    public void reader(int id, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            privateMatters();
            protectLock.lock();
            try {
                logState(">>>", id, i, "C");
                if (waitingWriters + writingWriters == 0) {
                    readingReaders++;
                    readersSemaphore.release();
                } else {
                    waitingReaders++;
                }
                logState("<<<", id, i, "C");
            } finally {
                protectLock.unlock();
            }

            try {
                readersSemaphore.acquire();
                logState("==>", id, i, "C");
                reading();
                logState("<==", id, i, "C");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            protectLock.lock();
            try {
                logState(">>>", id, i, "C");
                readingReaders--;
                if (readingReaders == 0 && waitingWriters > 0) {
                    writingWriters = 1;
                    waitingWriters--;
                    writersSemaphore.release();
                }
                logState("<<<", id, i, "C");
            } finally {
                protectLock.unlock();
            }
        }
    }

    public void writer(int id, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            privateMatters();
            protectLock.lock();
            try {
                logState(">>>", id, i, "P");
                if (readingReaders + writingWriters == 0) {
                    writingWriters = 1;
                    writersSemaphore.release();
                } else {
                    waitingWriters++;
                }
                logState("<<<", id, i, "P");
            } finally {
                protectLock.unlock();
            }

            try {
                writersSemaphore.acquire();
                logState("==>", id, i, "P");
                writing();
                logState("<==", id, i, "P");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            protectLock.lock();
            try {
                logState(">>>", id, i, "P");
                writingWriters = 0;
                if (waitingReaders > 0) {
                    while (waitingReaders > 0) {
                        readingReaders++;
                        waitingReaders--;
                        readersSemaphore.release();
                    }
                } else if (waitingWriters > 0) {
                    writingWriters = 1;
                    waitingWriters--;
                    writersSemaphore.release();
                }
                logState("<<<", id, i, "P");
            } finally {
                protectLock.unlock();
            }
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

    public static void main(String[] args) {
        ReadersWritersSimulator simulator = new ReadersWritersSimulator();
        int readersCount = 4; // Liczba czytelników
        int writersCount = 2; // Liczba pisarzy
        int repetitions = 10; // Liczba powtórzeń dla każdego wątku
        for (int i = 0; i < readersCount; i++) {
            int finalI = i;
            new Thread(() -> simulator.reader(finalI, repetitions)).start();
        }
        for (int i = 0; i < writersCount; i++) {
            int finalI = i;
            new Thread(() -> simulator.writer(finalI, repetitions)).start();
        }
    }
}

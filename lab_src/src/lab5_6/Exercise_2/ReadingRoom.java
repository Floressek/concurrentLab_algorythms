package src.lab5_6.Exercise_2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class ReadingRoom {
    private int waitingReaders = 0;
    private int readingReaders = 0;
    private int waitingWriters = 0;
    private int writingWriters = 0;
    static int c;
    static int d;

    Semaphore readersSemaphore = new Semaphore(0);
    Semaphore writersSemaphore = new Semaphore(0);
    ReentrantLock protectLock = new ReentrantLock(true);

    public void logState(String prefix, int id, int iteration, String type) {
        System.out.printf("%s [%s-%d, %d] :: [%d, %d, %d, %d]%n",
                prefix, type, id, iteration,
                readingReaders, waitingReaders,
                writingWriters, waitingWriters);
    }

    public void startReading(int id, int iteration) throws InterruptedException {
        protectLock.lock();
        try {
            logState(">>>", id, iteration, "C");
            if (waitingWriters + writingWriters == 0) {
                readingReaders++;
                readersSemaphore.release();
            } else {
                waitingReaders++;
            }
            logState("<<<", id, iteration, "C");
        } finally {
            protectLock.unlock();
        }

        readersSemaphore.acquire();
        logState(">>>", id, iteration, "C");
        Thread.sleep(ThreadLocalRandom.current().nextInt(c, d));  // Simulate reading
        logState("<<<", id, iteration, "C");

        protectLock.lock();
        try {
            logState(">>>", id, iteration, "C");
            readingReaders--;
            if (readingReaders == 0 && waitingWriters > 0) {
                writingWriters = 1;
                waitingWriters--;
                writersSemaphore.release();
            }
            logState("<<<", id, iteration, "C");
        } finally {
            protectLock.unlock();
        }
    }

    public void startWriting(int id, int iteration) throws InterruptedException {
        protectLock.lock();
        try {
            logState("==>", id, iteration, "P");
            if (readingReaders + writingWriters == 0) {
                writingWriters = 1;
                writersSemaphore.release();
            } else {
                waitingWriters++;
            }
            logState("<==", id, iteration, "P");
        } finally {
            protectLock.unlock();
        }

        writersSemaphore.acquire();
        logState("==>", id, iteration, "P");
        Thread.sleep(ThreadLocalRandom.current().nextInt(c, d));  // Simulate writing
        logState("<==", id, iteration, "P");

        protectLock.lock();
        try {
            logState("==>", id, iteration, "P");
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
            logState("<==", id, iteration, "P");
        } finally {
            protectLock.unlock();
        }
    }
}

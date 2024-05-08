package src.lab5_6.Exercise_2_testing;
//
//import java.util.concurrent.Semaphore;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class ReadingRoom {
//    private int readers = 0;
//    private final Semaphore accessRoom = new Semaphore(1);
//    private final Semaphore readMutex = new Semaphore(1);
//    private final AtomicInteger waitingWriters = new AtomicInteger(0);
//    private final Semaphore writeMutex = new Semaphore(1);
//
//    public void startRead(int id, int iteration) throws InterruptedException {
//        writeMutex.acquire();
//        writeMutex.release();
//
//        readMutex.acquire();
//        if (readers == 0) {
//            accessRoom.acquire();
//        }
//        readers++;
//        readMutex.release();
//
//        System.out.printf(">>> [C-%d, %d] :: Reader enters, Total Readers: %d\n", id, iteration, readers);
//        Thread.sleep((int) (Math.random() * 5 + 1));
//        System.out.printf("==> [C-%d, %d] :: Reading, Total Readers: %d\n", id, iteration, readers);
//    }
//
//    public void endRead(int id, int iteration) throws InterruptedException {
//        readMutex.acquire();
//        readers--;
//        if (readers == 0) {
//            accessRoom.release();
//        }
//        readMutex.release();
//        System.out.printf("<== [C-%d, %d] :: Reader exits, Remaining Readers: %d\n", id, iteration, readers);
//    }
//
//    public void startWrite(int id, int iteration) throws InterruptedException {
//        waitingWriters.incrementAndGet();
//        writeMutex.acquire();
//        accessRoom.acquire();
//        waitingWriters.decrementAndGet();
//        System.out.printf(">>> [P-%d, %d] :: Writer starts writing\n", id, iteration);
//        Thread.sleep((int) (Math.random() * 5 + 1));
//        System.out.printf("==> [P-%d, %d] :: Writing\n", id, iteration);
//    }
//
//    public void endWrite(int id, int iteration) {
//        System.out.printf("<== [P-%d, %d] :: Writer finishes writing\n", id, iteration);
//        accessRoom.release();
//        writeMutex.release();
//    }
//}
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadingRoom {
    private final AtomicInteger readers = new AtomicInteger(0);
    private final AtomicInteger waitingWriters = new AtomicInteger(0);
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore roomEmpty = new Semaphore(1);
    private final Semaphore fifoQueue = new Semaphore(1);  // FIFO queue semaphore

    public void startRead(int id, int iteration) throws InterruptedException {
        fifoQueue.acquire(); // Join the FIFO queue
        mutex.acquire();     // Access to readers count
        if (readers.incrementAndGet() == 1) {
            roomEmpty.acquire(); // First reader locks the roomEmpty
        }
        System.out.printf(">>> [C-%d, %d] :: [%d, %d, %d, %d]\n", id, iteration,
                readers.get(), readers.get() - 1, waitingWriters.get(), waitingWriters.get());
        mutex.release();
        fifoQueue.release(); // Leave the FIFO queue

        System.out.printf("==> [C-%d, %d] :: Reading starts\n", id, iteration);
        Thread.sleep((int) (Math.random() * 5 + 1));
    }

    public void endRead(int id, int iteration) throws InterruptedException {
        mutex.acquire(); // Access to readers count
        if (readers.decrementAndGet() == 0) {
            roomEmpty.release(); // Last reader unlocks the roomEmpty
        }
        System.out.printf("<== [C-%d, %d] :: Reading ends\n", id, iteration);
        mutex.release();
    }

    public void startWrite(int id, int iteration) throws InterruptedException {
        waitingWriters.incrementAndGet();
        fifoQueue.acquire(); // Join the FIFO queue
        waitingWriters.decrementAndGet();
        roomEmpty.acquire(); // Lock the room for exclusive access

        System.out.printf(">>> [P-%d, %d] :: Writing starts\n", id, iteration);
        Thread.sleep((int) (Math.random() * 5 + 1));
    }

    public void endWrite(int id, int iteration) {
        System.out.printf("<== [P-%d, %d] :: Writing ends\n", id, iteration);
        roomEmpty.release(); // Release the room for others
        fifoQueue.release(); // Leave the FIFO queue
    }
}

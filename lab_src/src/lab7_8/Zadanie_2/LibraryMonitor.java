package src.lab7_8.Zadanie_2;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Random;

public class LibraryMonitor {
    private int readers = 0;
    private int writers = 0;
    private int waitingWriters = 0;
    private int waitingReaders = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void startRead(int id, int repetition) throws InterruptedException{
        lock.lock();
        try {
            waitingReaders++;
            while(writers > 0 || waitingWriters > 0){
                condition.await();
            }
            waitingReaders--;
            readers++;
            System.out.printf(">>> [C-%d, %d] :: [licz_czyt=%d, licz_czyt_pocz=%d, licz_pis=%d, licz_pis_pocz=%d]\n", id, repetition, readers, waitingReaders, writers, waitingWriters);
        } finally {
            lock.unlock();
        }
    }

    public void stopRead(int id, int repetition) {
        lock.lock();
        try {
            readers--;
            if(readers == 0){
                condition.signalAll();
            }
            System.out.printf("<<< [C-%d, %d] :: [licz_czyt=%d, licz_czyt_pocz=%d, licz_pis=%d, licz_pis_pocz=%d]\n", id, repetition, readers, waitingReaders, writers, waitingWriters);
        } finally {
            lock.unlock();
        }
    }

    public void startWrite(int id, int repetition) throws InterruptedException {
        lock.lock();
        try {
            waitingWriters++;
            while(readers > 0 || writers > 0) {
                condition.await();
            }
            waitingWriters--;
            writers++;
            System.out.printf("==> [W-%d, %d] :: [licz_czyt=%d, licz_czyt_pocz=%d, licz_pis=%d, licz_pis_pocz=%d]\n", id, repetition, readers, waitingReaders, writers, waitingWriters);
        } finally {
            lock.unlock();
        }
    }

    public void stopWrite(int id, int repetition) {
        lock.lock();
        try {
            writers--;
            condition.signalAll();
            System.out.printf("<== [W-%d, %d] :: [licz_czyt=%d, licz_czyt_pocz=%d, licz_pis=%d, licz_pis_pocz=%d]\n", id, repetition, readers, waitingReaders, writers, waitingWriters);
        } finally {
            lock.unlock();
        }
    }
}

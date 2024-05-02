package src.lab3_4.Zadanie_3;

import java.util.Random;

public class Lamport extends Thread {
    private static volatile int desireNp = 0;
    private static volatile int desireNq = 0;
    private static volatile int[] desire = {0, 0};

    private final int nr;
    private final int iterationCount;
    private final char[] symbols = {'+', '-'};
    private final boolean synchronizedMode;


    Random random = new Random();

    public Lamport(int nr, int iterationCount, boolean synchronizedMode) {
        this.nr = nr;
        this.iterationCount = iterationCount;
        this.synchronizedMode = synchronizedMode;
    }

    private void personalAffairs() throws InterruptedException {

        int sleepTime = this.random.nextInt(10) + 1; // Random time in range 1-10 ms
        Thread.sleep(sleepTime);
    }

    private void criticalSection(int repetition) throws InterruptedException {
        System.out.println("Critical Section of thread " + (nr + 1) + ", nr powt. = " + repetition);
        for (int i = 0; i < 100; i++) {
            System.out.print(symbols[nr]);
        }
        System.out.println("\n");
    }

    private void notSynchronized() throws InterruptedException {
        for (int i = 0; i < iterationCount; i++) {
            personalAffairs();
            criticalSection(i);
        }
    }

//    private void synchronizedAction() throws InterruptedException {
//        for (int i = 0; i < iterationCount; i++) {
//            personalAffairs();
//            desireNp = desireNq + 1;
//            while( desireNq == 0 || desireNp <= desireNq) {
//                // Active waiting
//                int x = 0;
//            }
//            criticalSection(i);
//            desireNp = 0;
//        }
//    }
    private void synchronizedAction() throws InterruptedException {
        for (int i = 0; i < iterationCount; i++) {
            personalAffairs();
            desire[nr] = desire[1-nr] + 1;
            while( desire[1-nr] == 0 || (nr == 0 && desire[nr] <= desire[1-nr]) || (nr == 1 && desire[nr] < desire[1-nr])) {
                // Active waiting
                int x = 0;
            }
            criticalSection(i);
            desire[nr] = 0;
        }
    }

    @Override
    public void run() {
        try {
            if (synchronizedMode) {
                synchronizedAction();
            } else {
                notSynchronized();
            }
        } catch (InterruptedException e) {
            System.out.println("Error caught: " + e);
        }
    }
}

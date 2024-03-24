package Zadanie_2;

import java.util.Random;

public class Peterson extends Thread {
    private static final boolean[] desire = new boolean[2];
    private static volatile int whosLast = 0;
    private final int nr;
    private final int iterationCount;
    private final char[] symbols = {'+', '-'};
    private final boolean synchronizedMode;

    Random random = new Random();


    public Peterson(int nr, int iterationCount, boolean synchronizedMode) {
        this.nr = nr;
        this.iterationCount = iterationCount;
        this.synchronizedMode = synchronizedMode;
    }

    private void personalAffairs() throws InterruptedException {
        int sleepTime = random.nextInt(10) + 1; // Random time in range 1-10 ms
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

    private void synchronizedAction() throws InterruptedException {
        for (int i = 0; i < iterationCount; i++) {
            personalAffairs();
            desire[nr] = true;
            whosLast = nr;
            // Tells the other thread that it wants to enter the critical section
            while (desire[1 - nr] && whosLast == nr) {
                // Active waiting
            }
            criticalSection(i);
            desire[nr] = false;

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

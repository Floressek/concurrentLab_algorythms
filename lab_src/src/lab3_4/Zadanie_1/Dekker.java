package Zadanie_1;

import java.util.Random;

public class Dekker extends Thread {
    private static final boolean[] desire = new boolean[2];
    private static volatile int whosGoing = 0;
    private final int nr;
    private final int iterationCount;
    private final char[] symbols = {'+', '-'};
    private final boolean synchronizedMode;

    Random random = new Random();

    public Dekker(int nr, int iterationCount, boolean synchronizedMode) {
        this.nr = nr;
        this.iterationCount = iterationCount;
        this.synchronizedMode = synchronizedMode;
    }

    private void personalAffairs() throws InterruptedException {
        int sleepTime = random.nextInt(10) + 1; // Random time in range 1-10 ms
        Thread.sleep(sleepTime);
    }

    void printSeparator() {
        for (int i = 0; i < 100; i++) {
            System.out.print(symbols[nr]);
        }
        System.out.println("\n");
    }

    private void criticalSection(int repetition) throws InterruptedException {
//        System.out.println("Critical Section of thread " + (nr + 1) + ", nr powt. = " + repetition);
        System.out.printf("Critical Section of thread %d, nr powt. = %d\n", nr + 1, repetition);
//        var sb = new StringBuffer();
//        sb.append("Critical Section of thread");
//        sb.append(nr + 1);
//        var result = sb.toString();
//        System.out.print(STR."Critical Section of thread \{nr+1}, nr powt. = \{repetition}");
        printSeparator();
    }

    private void notSynchronized() throws InterruptedException {
        for (int i = 0; i < iterationCount; i++) {
            personalAffairs();
            criticalSection(i);
        }
    }


    private void Synchronized() throws InterruptedException {
        for (int i = 0; i < iterationCount; i++) {
            personalAffairs();
            desire[nr] = true;
            while (desire[1 - nr]) {
                if (whosGoing == 1 - nr) {
                    desire[nr] = false;
                    while (whosGoing == 1 - nr) {
                        // Active waiting
                    }
                    desire[nr] = true;
                }
            }
            criticalSection(i);

            whosGoing = 1 - nr;
            desire[nr] = false;

        }
    }

    @Override
    public void run() {
        try {
            if (synchronizedMode) {
                Synchronized();
            } else {
                notSynchronized();
            }
        } catch (InterruptedException e) {
            System.out.println("Error caught: " + e);
        }
    }
}

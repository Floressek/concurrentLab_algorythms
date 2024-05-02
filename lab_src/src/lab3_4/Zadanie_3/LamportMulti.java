package src.lab3_4.Zadanie_3;

import java.util.Random;

public class LamportMulti extends Thread {
    // by default, all elements are initialized to false
    public static final int COUNT = 5;

    private static volatile boolean[] desires = new boolean[COUNT];
    // by default, all elements are initialized to 0, tickets to the bakery
    private static volatile int[] numbers = new int[COUNT];


    private final int nr;
    private final int iterationCount;
    private final char[] symbols = {'+', '-', '#', '$', '*'};
    private final boolean synchronizedMode;
    Random random = new Random();

    public LamportMulti(int nr, int iterationCount, boolean synchronizedMode) {
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
            // Initialize the intention of entering the critical section
            desires[nr] = true;
            numbers[nr] = 1 + getMax();
            desires[nr] = false;

            // Check if other threads wants to enter the critical section
            for (int j = 0; j < desires.length; j++) {
                if (j == nr) {
                    continue;
                }

                while (desires[j]){
                    // Active waiting
                    int x = 0;
                }

                while (numbers[j] != 0 && (numbers[nr] > numbers[j] || (numbers[nr] == numbers[j] && nr > j))) {
                    // Active waiting
                    int y = 0;
                }
            }
            criticalSection(i);

            // Exit the critical section
            numbers[nr] = 0;
        }
    }
    private void _synchronizedAction() throws InterruptedException {
        for (int i = 0; i < iterationCount; i++) {
            personalAffairs();
            // Initialize the intention of entering the critical section
            desires[nr] = true;
            numbers[nr] = 1 + getMax();
            desires[nr] = false;

            // Check if other threads wants to enter the critical section
            for (int j = 0; j < desires.length; j++) {
                if (j == nr) {
                    continue;
                }

                while (!desires[j]){
                    // Active waiting
                    int x = 0;
                }

                while (numbers[j] == 0 || (numbers[nr] < numbers[j] || (numbers[nr] == numbers[j] && nr < j))) {
                    // Active waiting
                    int y = 0;
                }
            }
            criticalSection(i);

            // Exit the critical section
            numbers[nr] = 0;
        }
    }

    private /*synchronized*/ static int getMax() {
        int max = numbers[0];
        for (int curr : numbers) {
            if (curr > max) {
                max = curr;
            }
        }
        return max;
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

package Zadanie_4;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class SemaphoreThread extends Thread {
    private final boolean synchronizedMode;
    private final int iterationCount;
    private static final char[] symbols = {'+', '-', '#', '$', '*', '?', '!', '@'};
    private final int threadNumber;
    private static Semaphore semaphore = new Semaphore(1);

    public static int maxThreadCount() {
        return symbols.length;
    }

    public SemaphoreThread (int threadNumber, int iterationCount, boolean synchronizedMode) {
        this.threadNumber = threadNumber;
        this.iterationCount = iterationCount;
        this.synchronizedMode = synchronizedMode;
    }

    private void personalAffairs() throws InterruptedException {
        Random random = new Random();
        int sleepTime = random.nextInt(10) + 1; // Random time in range 1-10 ms
        Thread.sleep(sleepTime);
    }

    private void criticalSection(int repetition) throws InterruptedException {
        System.out.println("Critical Section of thread " + (threadNumber + 1) + ", nr powt. = " + repetition);
        for (int i = 0; i < 100; i++) {
            System.out.print(symbols[threadNumber]);
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
            try {
                semaphore.acquire();
                criticalSection(i);
            } finally {
                semaphore.release();
            }
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

    interface I1 {
        void test1();
    }

    interface V1 {
        void testV1();
    }

    interface I2 extends I1 {
        static final int a = 1;

//        void test2();
        default int test2() {
            return a;
        }
    }

    class C1 implements I2, V1 {
//        @Override
//        public int test2() {
//            return 1;
//        }

        @Override
        public void test1() {

        }

        @Override
        public void testV1() {

        }
    }

    void test(List<I2> items) {
        var o = new C1();
        var o1 = new C1() {
            @Override
            public void test1() {

            }
        };
//        var t = V1.a;
    }

}

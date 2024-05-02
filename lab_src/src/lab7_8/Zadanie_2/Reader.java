package src.lab7_8.Zadanie_2;

import java.util.Random;

public class Reader implements Runnable {
    private final int id;
    private final int repetition;
    private final LibraryMonitor monitor;
    static int a;
    static int b;
    static int c;
    static int d;

    public Reader(int id, int repetition, LibraryMonitor monitor) {
        this.id = id;
        this.repetition = repetition;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            for (int i = 0; i < repetition; i++) {
                Thread.sleep(random.nextInt(b) + a);
                monitor.startRead(id, i);
                Thread.sleep(random.nextInt(d) + c);
                monitor.stopRead(id, i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

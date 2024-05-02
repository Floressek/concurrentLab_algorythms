package src.lab7_8.Zadanie_2;

import java.util.Random;

public class Reader implements Runnable {
    private final int id;
    private final int repetition;
    private final LibraryMonitor monitor;

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
                Thread.sleep(random.nextInt(11) + 5);
                monitor.startRead(id, i);
                Thread.sleep(random.nextInt(5) + 1);
                monitor.stopRead(id, i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

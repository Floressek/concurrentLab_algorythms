package src.lab7_8.Zadanie_2;

import java.util.Random;

public class Writer implements Runnable{
    private final int id;
    private final int repetition;
    private final LibraryMonitor monitor;

    public Writer (int id, int repetition, LibraryMonitor monitor) {
        this.id = id;
        this.repetition = repetition;
        this.monitor = monitor;
    }

    @Override
    public void run(){
        try {
            Random random = new Random();
            for (int i = 0; i < repetition; i++) {
                Thread.sleep(random.nextInt(11) + 5);
                monitor.startWrite(id, i);
                Thread.sleep(random.nextInt(5) + 1);
                monitor.stopWrite(id, i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

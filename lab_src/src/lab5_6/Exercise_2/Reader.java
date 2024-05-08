package src.lab5_6.Exercise_2;

import java.util.concurrent.ThreadLocalRandom;

public class Reader implements Runnable {
    private final int id;
    private final int repetitions;
    private final ReadingRoom readingRoom;
    static int a;
    static int b;

    public Reader(int id, int repetitions, ReadingRoom readingRoom) {
        this.id = id;
        this.repetitions = repetitions;
        this.readingRoom = readingRoom;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < repetitions; i++) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(a, b));
                readingRoom.startReading(id, i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

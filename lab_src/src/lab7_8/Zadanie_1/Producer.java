package src.lab7_8.Zadanie_1;

import java.util.Random;


public class Producer implements Runnable { // Producer extends Thread
    private final Buffer buffer;
    private final int id;
    private final int repetitions;

    public Producer(Buffer buffer, int id, int repetitions) {
        this.buffer = buffer;
        this.id = id;
        this.repetitions = repetitions;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            for (int i = 0; i < repetitions; i++) {
                Thread.sleep(random.nextInt(10) + 1);
                String data = String.format("Dana=[P-%d, %d, %d]", id, i, random.nextInt(100));
                buffer.insert(data);
            }
            System.out.println(Thread.currentThread().getName() + " => producer, finished");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

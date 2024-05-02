package src.lab7_8.Zadanie_1_v2;

import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buffer;
    private final int producer_id;
    private final int repetitions;
    private final Random random = new Random();
    static int c, d;


    public Producer(Buffer buffer, int producer_id, int repetitions) {
        this.buffer = buffer;
        this.producer_id = producer_id;
        this.repetitions = repetitions;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < repetitions; i++) {
                Thread.sleep(random.nextInt(d) + c); // 1-10 ms
                String item = String.valueOf(random.nextInt(100)); // 0-99
                buffer.insert(item, producer_id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

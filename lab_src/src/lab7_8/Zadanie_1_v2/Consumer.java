package src.lab7_8.Zadanie_1_v2;

import java.util.Random;

public class Consumer implements Runnable {
    private final Buffer buffer;
    private final int consumer_id;
    private final int repetitions;
    private final Random random = new Random();
    static int a, b;

    public Consumer(Buffer buffer, int consumer_id, int repetitions) {
        this.buffer = buffer;
        this.consumer_id = consumer_id;
        this.repetitions = repetitions;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < repetitions; i++) {
                buffer.remove(consumer_id);
//                if (item == null) {
//                    break; // If data is null, exit the loop as production has ended
//                }
                Thread.sleep(random.nextInt(b) + a);
            }
            System.out.println(Thread.currentThread().getName() + " => consumer, finished");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

package src.lab7_8.Zadanie_1;

import java.util.Random;


public class Consumer implements Runnable {
    private final Buffer buffer;
    private final int id;
    private final int repetitions;

    public Consumer(Buffer buffer, int id, int repetitions) {
        this.buffer = buffer;
        this.id = id;
        this.repetitions = repetitions;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            for (int i = 0; i < repetitions; i++) {
                String data = buffer.remove();
                if (data == null) {
                    break; // If data is null, exit the loop as production has ended
                }
                Thread.sleep(random.nextInt(11) + 2);
                System.out.printf("[K-%d, %d] >> %s\n", id, i, data);
            }
            System.out.println(Thread.currentThread().getName() + " => consumer, finished");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

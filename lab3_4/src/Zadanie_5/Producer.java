package Exercise_1;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer extends Thread {
    private final CircularBuffer buffer;
    private final int id;
    private final Random rand = new Random();

    public Producer(CircularBuffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++){
                Thread.sleep(rand.nextInt(10) + 1);
                String data = String.format("Dana=[P-%d, %d, %d]", id, i, rand.nextInt(100));
                buffer.produce(data);
            }
            System.out.println(Producer.class.getName() + " " + id + " finished");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                buffer.producersFinished();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

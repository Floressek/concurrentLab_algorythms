package src.lab3_4.Zadanie_5_lab2;

import java.util.Random;

public class Consumer extends Thread {
    private final CircularBuffer buffer;
    private final int id;
    private final Random rand = new Random();

    public Consumer(CircularBuffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (i < 100) {
                Thread.sleep(rand.nextInt(10) + 1);
                String data = buffer.consume();
                if ("POISON_PILL".equals(data)) {
                    break;
                }
                System.out.printf("[%s, %d] >> %s\n", "K-" + id, i, data);
                i++;
            }
            System.out.println(Consumer.class.getName() + " " + id + " finished");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Random;

public class ProducerConsumer {

    private static final int BUFFER_SIZE = 10;
    private String[] buffer = new String[BUFFER_SIZE];
    private int count = 0, in = 0, out = 0;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    class Producer implements Runnable {
        private int id;
        private int repetitions;

        public Producer(int id, int repetitions) {
            this.id = id;
            this.repetitions = repetitions;
        }

        @Override
        public void run() {
            Random random = new Random();
            try {
                for (int i = 0; i < repetitions; i++) {
                    Thread.sleep(random.nextInt(10) + 1);
                    int item = random.nextInt(100);
                    String data = String.format("Dana=[P-%d, %d, %d]", id, i, item);

                    lock.lock();
                    try {
                        while (count == BUFFER_SIZE) {
                            notFull.await();
                        }
                        buffer[in] = data;
                        in = (in + 1) % BUFFER_SIZE;
                        count++;
                        notEmpty.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class Consumer implements Runnable {
        private int id;
        private int repetitions;

        public Consumer(int id, int repetitions) {
            this.id = id;
            this.repetitions = repetitions;
        }

        @Override
        public void run() {
            Random random = new Random();
            try {
                for (int i = 0; i < repetitions; i++) {
                    lock.lock();
                    try {
                        while (count == 0) {
                            notEmpty.await();
                        }
                        String data = buffer[out];
                        out = (out + 1) % BUFFER_SIZE;
                        count--;
                        notFull.signal();

                        Thread.sleep(random.nextInt(11) + 2);
                        System.out.printf("[K-%d, %d] >> %s\n", id, i, data);
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void startSimulation(int producersCount, int consumersCount, int repetitions) {
        Thread[] producers = new Thread[producersCount];
        Thread[] consumers = new Thread[consumersCount];

        for (int i = 0; i < producersCount; i++) {
            producers[i] = new Thread(new Producer(i + 1, repetitions));
            producers[i].start();
        }
        for (int i = 0; i < consumersCount; i++) {
            consumers[i] = new Thread(new Consumer(i + 1, repetitions));
            consumers[i].start();
        }

        try {
            for (Thread producer : producers) {
                producer.join();
            }
            for (Thread consumer : consumers) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        new ProducerConsumer().startSimulation(4, 5, 100);
    }
}

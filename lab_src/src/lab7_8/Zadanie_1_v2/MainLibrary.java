package src.lab7_8.Zadanie_1_v2;

public class MainLibrary {
    private static final int BUFFER_SIZE = 10;
    private static final Buffer buffer = new Buffer(BUFFER_SIZE);
    private static Thread[] producers;
    private static Thread[] consumers;

    public static void main(String[] args) {
        // Data
        int producersCount = 4;
        int consumersCount = 5;
        int repetitions_consumer = 100;
        int repetitions_producer = 80;
        // Time for consumers
        Consumer.a = 2;
        Consumer.b = 12;
        // Time for producers
        Producer.c = 1;
        Producer.d = 10;

        producers = new Thread[producersCount];
        consumers = new Thread[consumersCount];

        for (int i = 0; i < producersCount; i++) {
            producers[i] = new Thread(new Producer(buffer, i + 1, repetitions_producer));
            producers[i].start();
        }

        for (int i = 0; i < consumersCount; i++) {
            consumers[i] = new Thread(new Consumer(buffer, i + 1, repetitions_consumer));
            consumers[i].start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook is running");
            try {
                for (Thread producer : producers) {
                    if (producer != null) producer.join();
                }
                buffer.setAllProduced(); // Inform the buffer that all production has ended
                for (Thread consumer : consumers) {
                    if (consumer != null) consumer.join();
                }
            } catch (InterruptedException e) {
                System.out.println("Threads interrupted during shutdown.");
            }
        }));

        try {
            for (Thread producer : producers) {
                producer.join();
            }
            buffer.setAllProduced(); // Make sure to call after all producers finish
            for (Thread consumer : consumers) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

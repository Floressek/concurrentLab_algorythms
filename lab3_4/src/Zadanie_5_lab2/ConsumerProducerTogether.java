package Zadanie_5_lab2;

import java.util.Scanner;

import java.util.concurrent.Semaphore;
public class ConsumerProducerTogether {
    public static void main(String[] args) {
        // Number of producers and consumers
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of producers: ");
        int m = scanner.nextInt(); // Number of producers
        System.out.println("Enter the number of consumers: ");
        int n = scanner.nextInt(); // Number of consumers

        // Create a buffer with a size of 10
        CircularBuffer buffer = new CircularBuffer(10, m, n);

        Producer[] producers = new Producer[m];
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(buffer, i + 1);
            producers[i].start();
        }

        Consumer[] consumers = new Consumer[n];
        for (int j = 0; j < consumers.length; j++) {
            consumers[j] = new Consumer(buffer, j + 1);
            consumers[j].start();
        }

        for (Producer producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (Consumer consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("All producers and consumers finished");
        scanner.close();
    }
}

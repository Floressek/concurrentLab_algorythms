package src.lab9_10.Zadanie_1;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class TaskExecution {
    public static void main(String[] args) throws InterruptedException {

        // const. number of threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Synchronization for the end of the threads
        CountDownLatch latch = new CountDownLatch(3);

        // Start time
        long startTime = System.currentTimeMillis();

        // Task 1
        Runnable task1 = new Runnable() {
            public void run() {
                executeTask("Task 1", startTime);
            }
        };

        // Task 2 lambda
        Runnable task2 = () -> executeTask("Task 2", startTime);

        // Task 3 class
        class Task3 implements Runnable {
            public void run() {
                executeTask("Task 3", startTime);
            }
        }

        // Execute task 3
        Runnable task3 = new Task3();

        // Order of execution
        executor.execute(() -> {
            task1.run();
            latch.countDown();
        });
        executor.execute(() -> {
            task2.run();
            latch.countDown();
        });
        executor.execute(() -> {
            task3.run();
            latch.countDown();
        });

        // Wait for all threads to finish
        latch.await();

        // Shutdown executor
        executor.shutdown();

        System.out.println("All tasks completed");
        System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void executeTask(String taskName, long startTime) {
        String threadName = Thread.currentThread().getName();
        long startTaskTime = System.currentTimeMillis();


        // Print start of task
        System.out.printf("%d ms : %s : wej : %s%n", (System.currentTimeMillis() - startTime), taskName, threadName);

        // Sleep for random time 1 -> 6
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(6) + 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Task interrupted: " + taskName);
        }

        // Print end of task
        System.out.printf("%d ms: %s : wyj : %s%n", (System.currentTimeMillis() - startTaskTime), taskName, threadName);

    }
}

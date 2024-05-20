package src.lab9_10.Zadanie_2;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTasks {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        long startTime = System.currentTimeMillis();

        Runnable task1 = () -> executeTask("Task 1", startTime);
        Runnable task2 = () -> executeTask("Task 2", startTime);

        // Zadanie 1 z opóźnieniem 3 sekundy
        executor.schedule(task1, 3, TimeUnit.SECONDS);

        // Zadanie 2 powtarzane co 2 sekundy po zakończeniu poprzedniego wywołania
        //executor.scheduleWithFixedDelay(task2, 0, 2, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(task2, 0, 2, TimeUnit.SECONDS);
        // Zamknięcie ExecutorService po 10 sekundach
        executor.schedule(() -> {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Gwarantuje zakończenie wszystkich bieżących zadań
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            System.out.println("All tasks completed");
            System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + " ms");
        }, 10, TimeUnit.SECONDS);

        // Oczekiwanie na zamknięcie executora przed próbą dodania nowego zadania
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("ExecutorService is shut down" + " " + (System.currentTimeMillis() - startTime) + " ms");

        // Próba zlecenia zadania po zamknięciu executora
        try {
            executor.execute(() -> executeTask("Post-close task", startTime));
        } catch (Exception e) {
            System.out.println("Failed to submit task: ExecutorService is shut down");
        }
    }

    private static void executeTask(String taskName, long startTime) {
        String threadName = Thread.currentThread().getName();
        System.out.printf("%d ms : %s : wej : %s%n", (System.currentTimeMillis() - startTime), taskName, threadName);

        // Sleep for random time between 1 and 6 seconds
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(6) + 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Task interrupted: " + taskName);
        }

        System.out.printf("%d ms : %s : wyj : %s%n", (System.currentTimeMillis() - startTime), taskName, threadName);
    }
}

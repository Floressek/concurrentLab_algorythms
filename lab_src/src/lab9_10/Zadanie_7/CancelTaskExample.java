package src.lab9_10.Zadanie_7;

import java.util.concurrent.*;

public class CancelTaskExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Zadanie 1: Nieskończona pętla wypisująca komunikat co 0,5s
        Runnable task1 = () -> {
            try {
                String taskName = "Task 1";
                String threadName = Thread.currentThread().getName();
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println(taskName + " : " + threadName);
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted.");
            }
        };

        // Zadanie 2: Anuluje wykonanie zadania 1 po 4s
        Runnable task2 = () -> {
            try {
                TimeUnit.SECONDS.sleep(4); // Czekanie 4 sekundy
                System.out.println("Requesting cancellation of task 1");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted.");
            }
        };

        Future<?> future1 = executor.submit(task1);
        Future<?> future2 = executor.submit(task2);

        // Czekanie na wykonanie zadania 2, które anuluje zadanie 1
        future2.get();
        future1.cancel(true); // Anulowanie zadania 1

        // Zamknięcie ExecutorService
        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("Forcing shutdown...");
            executor.shutdownNow();
        }

        System.out.println("Koniec");
    }
}


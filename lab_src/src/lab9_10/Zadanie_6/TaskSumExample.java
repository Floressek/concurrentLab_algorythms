package src.lab9_10.Zadanie_6;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskSumExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Random random = new Random();

        // Lista zadań Callable, które losują wartość i zwracają ją
        List<Callable<Integer>> tasks = Arrays.asList(
                createTask("Task 1", random),
                createTask("Task 2", random),
                createTask("Task 3", random),
                createTask("Task 4", random)
        );

        try {
            // Wywołanie wszystkich zadań i oczekiwanie na ich wyniki
            List<Future<Integer>> results = executor.invokeAll(tasks);
            int sum = 0;

            // Sumowanie wyników i wypisywanie wartości
            for (Future<Integer> result : results) {
                int value = result.get();  // Odbieranie wyniku każdego zadania
                sum += value;
            }

            System.out.println("Suma wyników z zadań: " + sum);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            System.out.println("Koniec");
        }
    }

    private static Callable<Integer> createTask(String taskName, Random random) {
        return () -> {
            int sleepTime = random.nextInt(4) + 2; // Losowy czas uśpienia od 2 do 5 sekund
            TimeUnit.SECONDS.sleep(sleepTime);
            int result = random.nextInt(11); // Losowanie wartości od 0 do 10
            String threadName = Thread.currentThread().getName();
            System.out.println(taskName + " : " + threadName + " : " + result);
            return result;
        };
    }
}


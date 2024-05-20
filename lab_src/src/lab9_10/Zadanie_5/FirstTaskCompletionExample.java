package src.lab9_10.Zadanie_5;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FirstTaskCompletionExample {
    public static void main(String[] args) {
        // Stworzenie puli wątków
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Lista zadań Callable
        List<Callable<String>> tasks = Arrays.asList(
                () -> {
                    String taskName = "Task 1";
                    int sleepTime = new Random().nextInt(4) + 2; // Losowy czas uśpienia od 2 do 5 sekund
                    TimeUnit.SECONDS.sleep(sleepTime);
                    return taskName;
                },
                () -> {
                    String taskName = "Task 2";
                    int sleepTime = new Random().nextInt(4) + 2;
                    TimeUnit.SECONDS.sleep(sleepTime);
                    return taskName;
                },
                () -> {
                    String taskName = "Task 3";
                    int sleepTime = new Random().nextInt(4) + 2;
                    TimeUnit.SECONDS.sleep(sleepTime);
                    return taskName;
                },
                () -> {
                    String taskName = "Task 4";
                    int sleepTime = new Random().nextInt(4) + 2;
                    TimeUnit.SECONDS.sleep(sleepTime);
                    return taskName;
                }
        );

        try {
            // Otrzymywanie wyniku od pierwszego zakończonego zadania
            String result = executor.invokeAny(tasks);
            System.out.println("Pierwsze zakończone zadanie: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Zamknięcie ExecutorService
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }

            // Komunikat końcowy
            System.out.println("Koniec");
        }
    }
}

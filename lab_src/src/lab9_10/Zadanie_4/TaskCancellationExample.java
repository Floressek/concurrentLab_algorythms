package src.lab9_10.Zadanie_4;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskCancellationExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Random random = new Random();

        Callable<Long> task = () -> {
            long start = System.currentTimeMillis();
            int sleepTime = random.nextInt(4) + 2; // Losowy czas uśpienia od 2 do 5 sekund
            TimeUnit.SECONDS.sleep(sleepTime);
            return System.currentTimeMillis() - start;
        };

        Future<Long> future = executor.submit(task);
        long startTime = System.currentTimeMillis();

        // Pętla sprawdzająca status zadania co 0,5s
        while (!future.isDone()) {
            if (System.currentTimeMillis() - startTime > 3500) {
                future.cancel(true); // Anulowanie zadania, jeśli oczekiwanie przekroczy 3,5s
                System.out.println("Zadanie anulowane z powodu przekroczenia czasu oczekiwania.");
                break;
            }
            System.out.println("Czekam na zakończenie zadania...");
            TimeUnit.MILLISECONDS.sleep(500);
        }

        if (!future.isCancelled()) {
            // Pobieranie wyniku, jeśli zadanie nie zostało anulowane
            System.out.println("Czas wykonania zadania: " + future.get() + " ms");
        }

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }

        System.out.println("Koniec");
    }
}


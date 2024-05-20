package src.lab9_10.Zadanie_3;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallableExample {
    public static void main(String[] args) throws Exception {
        // Stworzenie ExecutorService z jednym wątkiem
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Zadanie Callable, które zwraca nazwę wątku po uśpieniu na 2 sekundy
        Callable<String> task = () -> {
            TimeUnit.SECONDS.sleep(2); // Uśpienie na 2 sekundy
            return Thread.currentThread().getName(); // Zwrócenie nazwy wątku
        };

        // Zlecanie zadania i przechowywanie odniesienia do Future
        Future<String> future = executor.submit(task);

        // Sprawdzanie statusu zakończenia zadania co 0,5 sekundy
        while (!future.isDone()) {
            System.out.println("Czekam na zakończenie zadania...");
            TimeUnit.MILLISECONDS.sleep(500); // Oczekiwanie na 0,5 sekundy
        }

        // Po zakończeniu zadania wypisanie wyniku
        System.out.println("Wynik zadania: " + future.get());

        // Zamknięcie ExecutorService
        executor.shutdown();

        // Wypisanie końcowego komunikatu
        System.out.println("Koniec");
    }
}

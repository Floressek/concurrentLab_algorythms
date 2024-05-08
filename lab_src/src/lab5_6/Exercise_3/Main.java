package src.lab5_6.Exercise_3;

public class Main {
    public static void main(String[] args) {
        ReaderWriterSimulator simulator = new ReaderWriterSimulator();
        int readersCount = 5; // Liczba czytelników
        int writersCount = 2; // Liczba pisarzy
        int repetitions = 10; // Liczba powtórzeń dla każdego wątku

        for (int i = 0; i < readersCount; i++) {
            int finalI = i;
            new Thread(() -> simulator.reader(finalI, repetitions)).start();
        }
        for (int i = 0; i < writersCount; i++) {
            int finalI = i;
            new Thread(() -> simulator.writer(finalI, repetitions)).start();
        }
    }
}

package src.lab5_6.Exercise_2_testing;
//
//import java.util.Scanner;
//
//public class ReaderWriterTogether {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter the number of readers: ");
//        int numberOfReaders = scanner.nextInt(); // Number of reader threads
//        System.out.println("Enter the number of writers: ");
//        int numberOfWriters = scanner.nextInt(); // Number of writer threads
//
//        // Create a shared reading room instance
//        ReadingRoom readingRoom = new ReadingRoom();
//
//        // Create arrays to hold threads
//        Reader[] readers = new Reader[numberOfReaders];
//        Writer[] writers = new Writer[numberOfWriters];
//
//        // Initialize and start reader threads
//        for (int i = 0; i < numberOfReaders; i++) {
//            readers[i] = new Reader(readingRoom, i + 1);
//            readers[i].start();
//        }
//
//        // Initialize and start writer threads
//        for (int i = 0; i < numberOfWriters; i++) {
//            writers[i] = new Writer(readingRoom, i + 1);
//            writers[i].start();
//        }
//
//        // Wait for all threads to complete
//        for (int i = 0; i < numberOfReaders; i++) {
//            try {
//                readers[i].join();
//            } catch (InterruptedException e) {
//                System.err.println("Reader " + (i + 1) + " was interrupted.");
//            }
//        }
//
//        for (int i = 0; i < numberOfWriters; i++) {
//            try {
//                writers[i].join();
//            } catch (InterruptedException e) {
//                System.err.println("Writer " + (i + 1) + " was interrupted.");
//            }
//        }
//
//        System.out.println("All readers and writers have finished their tasks.");
//        scanner.close();
//    }
//}

public class ReaderWriterTogether {
    public static void main(String[] args) {
        int numberOfReaders = 4;
        int numberOfWriters = 2;

        ReadingRoom readingRoom = new ReadingRoom();

        // Start readers
        for (int i = 0; i < numberOfReaders; i++) {
            new Reader(readingRoom, i + 1).start();
        }

        // Start writers
        for (int i = 0; i < numberOfWriters; i++) {
            new Writer(readingRoom, i + 1).start();
        }
    }
}


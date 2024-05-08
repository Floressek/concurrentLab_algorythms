package src.lab5_6.Exercise_2;

public class Main {
    public static void main(String[] args) {
        ReadingRoom readingRoom = new ReadingRoom();
        int readersCount = 4;
        int writersCount = 2;
        int repetitions = 10;
        ReadingRoom.c = 1;
        ReadingRoom.d = 6;
        Reader.a = 5;
        Reader.b = 15;
        Writer.a = 5;
        Writer.b = 15;

        for (int i = 0; i < readersCount; i++) {
            new Thread(new Reader(i, repetitions, readingRoom)).start();
        }
        for (int i = 0; i < writersCount; i++) {
            new Thread(new Writer(i, repetitions, readingRoom)).start();
        }
    }
}

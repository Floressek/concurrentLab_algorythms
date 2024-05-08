package src.lab5_6.Exercise_2_testing;
public class Writer extends Thread {
    private final ReadingRoom room;
    private final int id;

    public Writer(ReadingRoom room, int id) {
        this.room = room;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                room.startWrite(id, i);
                Thread.sleep((int) (Math.random() * 15 + 5)); // Own affairs
                room.endWrite(id, i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

//package src.Projekt_trip;
//
//import java.util.concurrent.Semaphore;
//
//public class Room {
//    private final int capacity;
//    private final Semaphore access;
//    private final int roomNumber;
//
//    public Room(int capacity, int roomNumber) {
//        this.capacity = capacity;
//        this.access = new Semaphore(capacity);
//        this.roomNumber = roomNumber;
//    }
//
//    public void enter() throws InterruptedException {
//        access.acquire();
//    }
//
//    public void exit() {
//        access.release();
//    }
//
//    public int getRoomNumber() {
//        return roomNumber;
//    }
//}

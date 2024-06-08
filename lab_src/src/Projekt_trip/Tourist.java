//package src.Projekt_trip;
//
//public class Tourist implements Runnable {
//    private final Room[] route;
//    private final Passage[] passages;
//    private final String name;
//
//    public Tourist(String name, Room[] route, Passage[] passages) {
//        this.name = name;
//        this.route = route;
//        this.passages = passages;
//    }
//
//    @Override
//    public void run() {
//        try {
//            for (int i = 0; i < route.length; i++) {
//                route[i].enter();
//                System.out.println(name + " enters room " + route[i].getRoomNumber());
//
//                // Simulating the tour
//                Thread.sleep(500);
//
//                route[i].exit();
//                System.out.println(name + " leaves room " + route[i].getRoomNumber());
//
//                // Moving to the next room
//                if (i < route.length - 1) {
//                    passages[i].goThrough();
//                }
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.out.println(name + " was interrupted.");
//        }
//    }
//}

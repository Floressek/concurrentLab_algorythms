//package org.example.demo;
//
//public class Tourist implements Runnable {
//    private final Room_d[] route;
//    private final Passage_d[] passageDS;
//    private final String name;
//
//    public Tourist(String name, Room_d[] route, Passage_d[] passageDS) {
//        this.name = name;
//        this.route = route;
//        this.passageDS = passageDS;
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
//                    passageDS[i].goThrough();
//                }
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.out.println(name + " was interrupted.");
//        }
//    }
//}

//package org.example.demo;
//
//import javafx.animation.PathTransition;
//import javafx.animation.SequentialTransition;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Line;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import java.util.concurrent.Semaphore;
//
//// Room class
//class Room_d {
//    private final Semaphore access;
//    private final Rectangle visualRepresentation;
//
//    public Room_d(int capacity, Rectangle visualRepresentation) {
//        this.access = new Semaphore(capacity);
//        this.visualRepresentation = visualRepresentation;
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
//    public Rectangle getVisualRepresentation() {
//        return visualRepresentation;
//    }
//}
//
//// Passage class
//class Passage_d {
//    private final Semaphore access = new Semaphore(1);
//
//    public void use() throws InterruptedException {
//        access.acquire();
//    }
//
//    public void release() {
//        access.release();
//    }
//}
//
//// Tourist class
//class Tourist_d implements Runnable {
//    private final Room_d[] route;
//    private final SequentialTransition transitions;
//
//    public Tourist_d(Room_d[] route, SequentialTransition transitions) {
//        this.route = route;
//        this.transitions = transitions;
//    }
//
//    @Override
//    public void run() {
//        try {
//            // Start by trying to enter the first room
//            route[0].enter();
//
//        // Set up handlers for each transition except the last one
//        for (int i = 0; i < transitions.getChildren().size() - 1; i++) {
//            final int index = i;
//            PathTransition transition = (PathTransition) transitions.getChildren().get(index);
//            transition.setOnFinished(e -> {
//                try {
//                    route[index].exit(); // Exit the current room when the transition finishes
//
//                    if (index + 1 < route.length) {
//                        route[index + 1].enter(); // If there is another room, try to enter it
//                    }
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                }
//            });
//        }
//
//        // Special handling for the last transition to exit the last room
//        if (!transitions.getChildren().isEmpty()) {
//            transitions.getChildren().get(transitions.getChildren().size() - 1).setOnFinished(e -> {
//                route[route.length - 1].exit();
//            });
//        }
//
//            // Play the entire sequence of transitions
//            transitions.playFromStart();
//
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//}
//
//public class DEMO_2 extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        Pane root = new Pane();
//
//        // Define rooms and paths
//        Rectangle visualRoom1 = new Rectangle(50, 110, 100, 70);
//        visualRoom1.setFill(Color.LIGHTBLUE);
//        Room_d room1 = new Room_d(5, visualRoom1);  // Capacity adjusted
//
//        Rectangle visualRoom2 = new Rectangle(200, 110, 140, 100);
//        visualRoom2.setFill(Color.LIGHTGREEN);
//        Room_d room2 = new Room_d(10, visualRoom2); // Double capacity for room 2
//
//        Rectangle visualRoom3 = new Rectangle(400, 110, 100, 70);
//        visualRoom3.setFill(Color.LIGHTPINK);
//        Room_d room3 = new Room_d(5, visualRoom3);
//
//        Rectangle visualRoom4 = new Rectangle(390, 250, 120, 100);
//        visualRoom4.setFill(Color.LIGHTYELLOW);
//        Room_d room4 = new Room_d(5, visualRoom4);
//
//        // Define paths
//        Line path12 = new Line(150, 150, 200, 150);  // Connect room 1 to room 2
//        Line path23 = new Line(340, 150, 400, 150);  // Connect room 2 to room 3
//        Line path34 = new Line(450, 180, 450, 250);  // Connect room 3 to room 4
//
//
//        root.getChildren().addAll(visualRoom1, visualRoom2, visualRoom3, visualRoom4, path12, path23, path34);
//
//        // Launch multiple tourists with varying attributes
//        launchTourists(root, room1, room2, room3, room4);
//
//        Scene scene = new Scene(root, 800, 600);
//        primaryStage.setTitle("Mine Tour Visualization");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    private void launchTourists(Pane root, Room_d... rooms) {
//        for (int i = 0; i < 7; i++) {
//            Circle touristVisualA = new Circle(150, 125, 5, i < 3 ? Color.BLUE : Color.RED);
//            Circle touristVisualB = new Circle(450, 145, 5, i < 3 ? Color.RED : Color.BLUE);
//
//            root.getChildren().addAll(touristVisualA, touristVisualB);
//
//            PathTransition transitionA = createTransition(touristVisualA, rooms);
//            PathTransition transitionB = createTransition(touristVisualB, rooms);
//
//            SequentialTransition transitionsA = new SequentialTransition(transitionA);
//            SequentialTransition transitionsB = new SequentialTransition(transitionB);
//
//            Tourist_d touristA = new Tourist_d(rooms, transitionsA);
//            Tourist_d touristB = new Tourist_d(rooms, transitionsB);
//
//            Thread threadA = new Thread(touristA);
//            Thread threadB = new Thread(touristB);
//
//            threadA.start();
//            threadB.start();
//
//            try {
//                Thread.sleep(1000); // Delay each tourist start by 1 second
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//
//
//    private PathTransition createTransition(Circle touristVisual, Room_d... rooms) {
//        // This method needs to create appropriate PathTransitions based on the rooms and their layout
//        // This is a placeholder for actual transition creation logic
//        return new PathTransition(Duration.seconds(2), new Line(150, 125, 200, 150), touristVisual);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

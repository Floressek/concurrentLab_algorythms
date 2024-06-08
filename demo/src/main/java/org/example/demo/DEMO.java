//package org.example.demo;
//
//import javafx.animation.PathTransition;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Line;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//public class DEMO extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        Pane root = new Pane();
//
//        // Define rooms as rectangles based on the layout
//        Rectangle room1 = new Rectangle(50, 110, 100, 70);
//        room1.setFill(Color.LIGHTGRAY);
//
//        // Second room BIG
//        Rectangle room2 = new Rectangle(200, 110, 140, 100);
//        room2.setFill(Color.LIGHTGRAY);
//
//        // Third room
//        Rectangle room3 = new Rectangle(400, 110, 100, 70);
//        room3.setFill(Color.LIGHTGRAY);
//
//        // Fourth room
//        Rectangle room4 = new Rectangle(390, 250, 120, 100);
//        room4.setFill(Color.LIGHTGRAY);
//
//        // Define paths as lines
//        Line path12 = new Line(150, 150, 200, 150);  // Connect room 1 to room 2
//        Line path23 = new Line(340, 150, 400, 150);  // Connect room 2 to room 3
//        Line path34 = new Line(450, 180, 250, 450);  // Connect room 3 to room 4
//
//        // Create a tourist as a circle
//        Circle tourist = new Circle(130, 125, 5, Color.RED);
//        Circle tourist2 = new Circle(130, 125, 5, Color.BLACK);
//        Circle tourist3 = new Circle(130, 125, 5, Color.BLUE);
//
//        // Add all to root
//        root.getChildren().addAll(room1, room2, room3, room4, path12, path23, path34, tourist, tourist2, tourist3);
//
//        // Create and configure a path transition for the tourist
//        PathTransition transition = new PathTransition();
//        PathTransition transition2 = new PathTransition();
//        PathTransition transition3 = new PathTransition();
//        // what is the node that will be animated?
//        transition.setNode(tourist);
//        transition2.setNode(tourist2);
//        transition3.setNode(tourist3);
//
//        // what is the duration of the transition?
//        transition.setDuration(Duration.seconds(3));
//        transition2.setDuration(Duration.seconds(3));
//        transition3.setDuration(Duration.seconds(3));
//
//        // what is the path of the transition?
//        transition.setPath(path12);
//        transition2.setPath(path23);
//        transition3.setPath(path34);
//
//        // how many times should the transition cycle?
//        transition.setCycleCount(Timeline.INDEFINITE);
//        transition2.setCycleCount(Timeline.INDEFINITE);
//        transition3.setCycleCount(Timeline.INDEFINITE);
//        // should the transition reverse direction on the way back?
//        transition.setAutoReverse(true);
//        transition2.setAutoReverse(true);
//        transition3.setAutoReverse(true);
//
//        // start the transition
//        transition.play();
//        transition2.play();
//        transition3.play();
//
//        // Scene setup
//        Scene scene = new Scene(root, 800, 600);
//        primaryStage.setTitle("Mine Tour Visualization");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
//

package org.example.demo;

import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.concurrent.Semaphore;

// Room class
class Room_d {
    private final Semaphore access;
    private final Rectangle visualRepresentation;

    public Room_d(int capacity, Rectangle visualRepresentation) {
        this.access = new Semaphore(capacity);
        this.visualRepresentation = visualRepresentation;
    }

    public void enter() throws InterruptedException {
        access.acquire();
    }

    public void exit() {
        access.release();
    }

    public Rectangle getVisualRepresentation() {
        return visualRepresentation;
    }
}

// Passage class
class Passage_d {
    private final Semaphore access = new Semaphore(1);

    public void use() throws InterruptedException {
        access.acquire();
    }

    public void release() {
        access.release();
    }
}

// Tourist class
class Tourist_d implements Runnable {
    private final Room_d[] route;
    private final SequentialTransition transitions;

    public Tourist_d(Room_d[] route, SequentialTransition transitions) {
        this.route = route;
        this.transitions = transitions;
    }

    @Override
    public void run() {
        try {
            // Start by trying to enter the first room
            route[0].enter();

            // Set up handlers for each transition
            for (int i = 0; i < transitions.getChildren().size(); i++) {
                final int index = i;
                PathTransition transition = (PathTransition) transitions.getChildren().get(index);
                transition.setOnFinished(e -> {
                    try {
                        // Exit the current room when the transition finishes
                        route[index].exit();

                        // If there is another room, try to enter it
                        if (index + 1 < route.length) {
                            route[index + 1].enter();
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            // Play the entire sequence of transitions
            transitions.playFromStart();

            // Additional handling for when the last transition finishes
            transitions.getChildren().getLast().setOnFinished(e -> {
                // Ensure the last room is exited properly
                route[route.length - 1].exit();
            });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class DEMO extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        Rectangle visualRoom1 = new Rectangle(50, 110, 100, 70);
        visualRoom1.setFill(Color.LIGHTBLUE);
        Room_d room1 = new Room_d(1, visualRoom1);

        Rectangle visualRoom2 = new Rectangle(200, 110, 140, 100);
        visualRoom2.setFill(Color.LIGHTGREEN);
        Room_d room2 = new Room_d(2, visualRoom2); // Double capacity for room 2

        Rectangle visualRoom3 = new Rectangle(400, 110, 100, 70);
        visualRoom3.setFill(Color.LIGHTPINK);
        Room_d room3 = new Room_d(1, visualRoom3);

        Rectangle visualRoom4 = new Rectangle(390, 250, 120, 100);
        visualRoom4.setFill(Color.LIGHTYELLOW);
        Room_d room4 = new Room_d(1, visualRoom4);

        Line path12 = new Line(150, 150, 200, 150);  // Connect room 1 to room 2
        Line path23 = new Line(340, 150, 400, 150);  // Connect room 2 to room 3
        Line path34 = new Line(450, 180, 450, 250);  // Connect room 3 to room 4


        root.getChildren().addAll(path12, path23, path34, visualRoom1, visualRoom2, visualRoom3, visualRoom4);

        Circle touristVisual = new Circle(130, 125, 5, Color.RED);
        root.getChildren().add(touristVisual);

        PathTransition transition1 = new PathTransition(Duration.seconds(2), path12, touristVisual);
        PathTransition transition2 = new PathTransition(Duration.seconds(2), path23, touristVisual);
        PathTransition transition3 = new PathTransition(Duration.seconds(2), path34, touristVisual);

        SequentialTransition transitions = new SequentialTransition(transition1, transition2, transition3);
        Room_d[] rooms = {room1, room2, room3, room4};
        Tourist_d touristD = new Tourist_d(rooms, transitions);

        new Thread(touristD).start();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Mine Tour Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

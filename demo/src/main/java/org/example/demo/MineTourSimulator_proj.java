package org.example.demo;

//package org.example.demo;
//
//public class MineTourSimulator_proj {
//    public static void main(String[] args) {
//        Room[] rooms = {
//                new Room(10, 1),  // Room 1
//                new Room(20, 2),  // Room 2 (double capacity)
//                new Room(10, 3),  // Room 3
//                new Room(10, 4)   // Room 4
//        };
//
//        Passage[] passages = new Passage[3];
//        for (int i = 0; i < passages.length; i++) {
//            passages[i] = new Passage(); // Passage 1, 2, 3 index
//        }
//
//        // Routes
//        Room[] routeA = {rooms[0], rooms[1], rooms[2], rooms[3]};
//        Room[] routeB = {rooms[3], rooms[2], rooms[1], rooms[0]};
//        Passage[] passagesA = {passages[0], passages[1], passages[2]};
//        Passage[] passagesB = {passages[2], passages[1], passages[0]};
//
//        Thread touristA = new Thread(new Tourist("Tourist A", routeA, passagesA));
//        Thread touristB = new Thread(new Tourist("Tourist B", routeB, passagesB));
//        Thread touristC = new Thread(new Tourist("Tourist C", routeA, passagesA));
//        Thread touristD = new Thread(new Tourist("Tourist D", routeB, passagesB));
//        Thread touristE = new Thread(new Tourist("Tourist E", routeA, passagesA));
//
//        touristA.start();
//        touristB.start();
////        touristC.start();
////        touristD.start();
////        touristE.start();
//    }
//}
//

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MineTourSimulator_proj extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Define rooms as rectangles
        Rectangle room1 = new Rectangle(50, 50, 60, 60);
        room1.setFill(Color.LIGHTBLUE);

        Rectangle room2 = new Rectangle(250, 50, 60, 60);
        room2.setFill(Color.LIGHTGREEN);

        Rectangle room3 = new Rectangle(250, 250, 60, 60);
        room3.setFill(Color.LIGHTPINK);

        Rectangle room4 = new Rectangle(50, 250, 60, 60);
        room4.setFill(Color.LIGHTYELLOW);

        // Define paths as lines
        Line path12 = new Line(110, 80, 250, 80);
        Line path23 = new Line(280, 110, 280, 250);
        Line path34 = new Line(250, 280, 110, 280);

        // Create a tourist as a circle
        Circle tourist = new Circle(80, 80, 5, Color.RED);

        // Add all to root
        root.getChildren().addAll(room1, room2, room3, room4, path12, path23, path34, tourist);

        // Create and configure a path transition for the tourist
        PathTransition transition = new PathTransition();
        transition.setNode(tourist);
        transition.setDuration(Duration.seconds(1));
        transition.setPath(path12);
        transition.setCycleCount(Timeline.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();

        // Scene setup
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Mine Tour Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


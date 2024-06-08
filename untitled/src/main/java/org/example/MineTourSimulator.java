package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MineTourSimulator extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Button startButton = new Button("Start Tour");
        Button stopButton = new Button("Stop Tour");

        // Ustawienie przycisków i innych elementów interfejsu
        startButton.setLayoutX(100);
        startButton.setLayoutY(400);
        stopButton.setLayoutX(200);
        stopButton.setLayoutY(400);

        // Dodaj przyciski do pane
        root.getChildren().addAll(startButton, stopButton);

        // Ustawienie sceny i stage
        Scene scene = new Scene(root, 800, 450);
        primaryStage.setTitle("Mine Tour Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

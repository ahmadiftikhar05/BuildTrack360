package com.example.buildtrack360;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Login.fxml"));
            AnchorPane root = loader.load();
            Scene scene=new Scene(root);
            primaryStage.setTitle("BuildTrack360");
            primaryStage.setScene(scene);
            //primaryStage.setMaximized(true);
            //primaryStage.setResizable(false);


            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

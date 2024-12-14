package com.example.buildtrack360;

import com.example.buildtrack360.Test.RolesInput;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Login.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



        public static void main(String[] args) {
launch(args);
        }

}
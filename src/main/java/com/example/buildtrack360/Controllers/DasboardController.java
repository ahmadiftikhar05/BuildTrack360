package com.example.buildtrack360.Controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
public class DasboardController {

@FXML
Label DashboadTitle;
    public void pathButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) DashboadTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/BuildPath.fxml"));
            AnchorPane root = loader.load();
            Scene scene=new Scene(root);
            stage.setTitle("BuildTrack360");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(false);


            stage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardButtonOnClick(ActionEvent actionEvent) {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) DashboadTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Dashboard.fxml"));
            AnchorPane root = loader.load();
            Scene scene=new Scene(root);
            stage.setTitle("BuildTrack360");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(false);


            stage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void projectsButtonOnClick(ActionEvent actionEvent) {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) DashboadTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Projects.fxml"));
            AnchorPane root = loader.load();
            Scene scene=new Scene(root);
            stage.setTitle("BuildTrack360");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(false);


            stage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signupButtonOnClick(ActionEvent actionEvent) {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) DashboadTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Signup.fxml"));
            AnchorPane root = loader.load();
            Scene scene=new Scene(root);
            stage.setTitle("BuildTrack360");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(false);


            stage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.buildtrack360.Controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TaskController {
    @FXML
    private Label tasksLabel;

    public void dashboardButtonOnClick(ActionEvent actionEvent) {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) tasksLabel.getScene().getWindow();

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
            Stage currentStage = (Stage) tasksLabel.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/ProjectManager/DisplayProjectInfo.fxml"));
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
    public void tasksButtonOnClick(ActionEvent actionEvent) {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) tasksLabel.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Tasks.fxml"));
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
    public void pathButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) tasksLabel.getScene().getWindow();

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

}

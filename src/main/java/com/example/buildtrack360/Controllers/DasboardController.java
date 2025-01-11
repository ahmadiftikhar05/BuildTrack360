package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import com.example.buildtrack360.Database.DatabaseConnection;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DasboardController {

@FXML
Label DashboadTitle;
    @FXML
    private TableView<project> projectsummaryTable;

    @FXML
    private TableColumn<project, String> nameColumn;

    @FXML
    private TableColumn<project, String> projectmanagerColumn;

    @FXML
    private TableColumn<project, String> duedateColumn;

    @FXML
    private TableColumn<project, String> statusColumn;

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

    private ObservableList<project> projectList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadProjectData();
    }

    private void loadProjectData() {
        try {
            Connection connection = DatabaseConnection.GetConnection();
            String query = "SELECT Name, ProjectManager, Agreement, CompletePercent FROM projects";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String projectManager = resultSet.getString("ProjectManager");
                String status = resultSet.getString("CompletePercent") + "%";

                projectList.add(new project(name, projectManager,status));
            }

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            projectmanagerColumn.setCellValueFactory(new PropertyValueFactory<>("projectManager"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            projectsummaryTable.setItems(projectList);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import com.example.buildtrack360.Backend.Project;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DasboardController {

    @FXML
    private BarChart<String, Number> stageBarChart;
    @FXML
    private LineChart<String, Number> revenueLineChart;

    @FXML
    public void initialize() {
        loadProjectData();
        loadStageData();
        loadRevenueLineChart();
        revenueLineChart.setMinHeight(400);
    }

    private void loadStageData() {
        try {
            Connection connection = DatabaseConnection.GetConnection();
            String query = "SELECT Stages, COUNT(*) as ProjectCount " +
                    "FROM projects " +
                    "WHERE Stages IS NOT NULL " +
                    "GROUP BY Stages " +
                    "ORDER BY Stages";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Projects per Stage");

            while (resultSet.next()) {
                int stageId = resultSet.getInt("Stages");
                int count = resultSet.getInt("ProjectCount");
                String stageName = getStageName(stageId);
                series.getData().add(new XYChart.Data<>(stageName, count));
            }

            stageBarChart.getData().clear();
            stageBarChart.getData().add(series);
            stageBarChart.setTitle("Projects by Stage");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStageName(int stageId) {
        try {
            Connection connection = DatabaseConnection.GetConnection();
            String query = "SELECT Name FROM buildpathstages WHERE ID = " + stageId;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return resultSet.getString("Name");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Stage " + stageId;
    }
    private void loadRevenueLineChart() {
        try {
            Connection connection = DatabaseConnection.GetConnection();
            String query = "SELECT Name, Amount FROM projects WHERE Amount IS NOT NULL ORDER BY ID";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Project Revenue");

            while (resultSet.next()) {
                String projectName = resultSet.getString("Name");
                double amount = resultSet.getDouble("Amount");
                series.getData().add(new XYChart.Data<>(projectName, amount));
            }

            revenueLineChart.getData().clear();
            revenueLineChart.getData().add(series);
            revenueLineChart.setTitle("Project Revenue Trend");

            // Rotate x-axis labels
            CategoryAxis xAxis = (CategoryAxis) revenueLineChart.getXAxis();
            xAxis.setTickLabelRotation(0);

            // Style the chart
            revenueLineChart.setStyle("-fx-padding: 10px;");
            xAxis.setStyle("-fx-tick-label-font-size: 12px;");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/AddProject.fxml"));
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

   // public void initialize() {
        //loadProjectData();
    //}

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

                Project p = new Project();
                p.setID(projectManager);
                String ManagerName = p.GetProjectManagerName();
                projectList.add(new project(name, ManagerName,status));
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
    @FXML
    private void signoutButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) DashboadTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Login.fxml"));
            AnchorPane root = loader.load();
            Scene scene=new Scene(root);
            stage.setTitle("BuildTrack360");
            stage.setScene(scene);

            stage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


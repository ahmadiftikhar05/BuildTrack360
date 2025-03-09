package com.example.buildtrack360.Controllers.ProjectManager;

import com.example.buildtrack360.Database.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.example.buildtrack360.Database.LoadDatabase;
import com.example.buildtrack360.Backend.Project;

public class dashboardController {
    @FXML
    Label dashboardTitle;
    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, String> projectNameColumn;

    @FXML
    private TableColumn<Project, String> statusColumn;

    private int currentProjectManagerId; // Will store logged-in PM's ID
    private LoadDatabase loadDatabase = new LoadDatabase();
    private ObservableList<Project> projectData = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Project, String> percentageColumn;

    @FXML
    private BarChart<String, Number> projectChart;

    // ... your other existing fields ...

    @FXML
    public void initialize() {
        // Setup existing table columns

        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        statusColumn.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            String status = getProjectStatus(project);
            return new SimpleStringProperty(status);
        });

        percentageColumn.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            double percentage = project.getCompletePercent();
            return new SimpleStringProperty(String.format("%.1f%%", percentage));
        });

        // Load projects and update both table and chart
        loadProjects();
        updateProjectChart();
    }

    private void updateProjectChart() {
        // Clear existing data
        projectChart.getData().clear();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Project Completion");

        // Add data points for each project
        for (Project project : projectData) {
            series.getData().add(new XYChart.Data<>(
                    project.getName(),
                    project.getCompletePercent()
            ));
        }

        // Add series to chart
        projectChart.getData().add(series);

        // Style the bars (optional)
        for (XYChart.Data<String, Number> data : series.getData()) {
            // Add hover effect to show exact percentage
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip(
                    String.format("%s: %.1f%%",
                            data.getXValue(),
                            data.getYValue())
            );
            Tooltip.install(node, tooltip);

            // Optional: Color bars based on completion percentage
            double percentage = data.getYValue().doubleValue();
            String color;
            if (percentage < 33) {
                color = "#ff6b6b"; // Red
            } else if (percentage < 66) {
                color = "#ffd93d"; // Yellow
            } else {
                color = "#6bcb77"; // Green
            }
            node.setStyle("-fx-bar-fill: " + color + ";");
        }
    }

    private void loadProjects() {
        projectData.clear();
        loadDatabase.LoadProject();
        loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.GetHead();

        int currentPMId = SessionManager.getCurrentUserId();

        while (loadDatabase.ProjectsList.tempNode != null) {
            Project project = loadDatabase.ProjectsList.tempNode.data;
            if (project.getProjectManagerID() == currentPMId) {
                projectData.add(project);
            }
            loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.tempNode.next;
        }

        projectTable.setItems(projectData);

        // Update the chart whenever projects are loaded
        updateProjectChart();
    }
//    @FXML
//    public void initialize() {
//        // Setup table columns
//        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
//        statusColumn.setCellValueFactory(cellData -> {
//            Project project = cellData.getValue();
//            String status = getProjectStatus(project);
//            return new SimpleStringProperty(status);
//        });
//
//        // Setup percentage column
//        percentageColumn.setCellValueFactory(cellData -> {
//            Project project = cellData.getValue();
//            double percentage = project.getCompletePercent();
//            return new SimpleStringProperty(String.format("%.1f%%", percentage));
//        });
//
//        // Load projects
//        loadProjects();
//    }

//    private void loadProjects() {
//        projectData.clear();
//        loadDatabase.LoadProject();  // This already loads the percentage from database
//        loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.GetHead();
//
//        int currentPMId = SessionManager.getCurrentUserId();
//
//        while (loadDatabase.ProjectsList.tempNode != null) {
//            Project project = loadDatabase.ProjectsList.tempNode.data;
//            if (project.getProjectManagerID() == currentPMId) {
//                projectData.add(project);
//            }
//            loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.tempNode.next;
//        }
//
//        projectTable.setItems(projectData);
//    }



    private String getProjectStatus(Project project) {
        // Load stages for reference
        loadDatabase.LoadStages();
        loadDatabase.StageList.tempNode = loadDatabase.StageList.GetHead();

        while (loadDatabase.StageList.tempNode != null) {
            if (loadDatabase.StageList.tempNode.data.getID() == project.getStage()) {
                return loadDatabase.StageList.tempNode.data.getName();
            }
            loadDatabase.StageList.tempNode = loadDatabase.StageList.tempNode.next;
        }

        return "Unknown";
    }

    @FXML
    public void teamButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) dashboardTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/ProjectManager/Teams.fxml"));
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
    @FXML
    private void planningButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) dashboardTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/ProjectManager/Planning.fxml"));
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
    @FXML
    private void dashboardButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) dashboardTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/ProjectManager/dashboard.fxml"));
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
    @FXML
    private void taskButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) dashboardTitle.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Tasks/Display.fxml"));
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
    @FXML
    private void signoutButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) dashboardTitle.getScene().getWindow();

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
package com.example.buildtrack360.Controllers.ProjectManager;

import com.example.buildtrack360.Database.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
//        // Get current project manager ID (you'll need to set this when they log in)
//        currentProjectManagerId = getCurrentLoggedInPMId();
//
//        // Load projects
//        loadProjects();
//    }

//    private void loadProjects() {
//        loadDatabase.LoadProject();
//        loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.GetHead();
//
//        while (loadDatabase.ProjectsList.tempNode != null) {
//            Project project = loadDatabase.ProjectsList.tempNode.data;
//            if (project.getProjectManagerID() == currentProjectManagerId) {
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
//    private double getprojectpercent(Project project)
//    {
//        loadDatabase.LoadProject();
//        loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.GetHead();
//
//        while (loadDatabase.ProjectsList.tempNode !=null) {
//            if(loadDatabase.ProjectsList.tempNode.data.getProjectManagerID() == SessionManager.getCurrentUserId())
//            {
//                return loadDatabase.ProjectsList.tempNode.data.getCompletePercent();
//            }
//        }
//      return -1;
//    }
private double getProjectPercentage(Project project) {
    // Fetch the percentage completion from the project
    loadDatabase.LoadProject();
    loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.GetHead();

    while (loadDatabase.ProjectsList.tempNode != null) {
        Project p = loadDatabase.ProjectsList.tempNode.data;
        if (p.getProjectManagerID() == SessionManager.getCurrentUserId() && p.getID() == project.getID()) {
            return p.getCompletePercent(); // Assuming getCompletePercent() method returns the percentage
        }
        loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.tempNode.next;
    }
    return 0; // Return 0% if not found
}
    @FXML
    public void initialize() {
        // Setup table columns
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        statusColumn.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            String status = getProjectStatus(project);
            return new SimpleStringProperty(status);
        });
        percentageColumn.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            String percentage = String.format("%.2f", getProjectPercentage(project)) + "%"; // Formatting percentage to two decimal places
            return new SimpleStringProperty(percentage);
        });

        // Load projects
        loadProjects();
    }

    private void loadProjects() {
        projectData.clear(); // Clear existing data
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



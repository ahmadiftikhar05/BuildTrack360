package com.example.buildtrack360.Controllers.Developer;

import com.example.buildtrack360.Backend.Tasks;
import com.example.buildtrack360.Backend.Users;
import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeveloperTaskController {
    @FXML
    private TableView<TaskViewModel> tasksTable;
    @FXML
    private TableColumn<TaskViewModel, String> taskNameColumn;
    @FXML
    private TableColumn<TaskViewModel, String> descriptionColumn;
    @FXML
    private TableColumn<TaskViewModel, String> assignedToColumn;
    @FXML
    private TableColumn<TaskViewModel, Boolean> statusColumn;

    private String Username = null;
    private LoadDatabase loadDatabase;
    private Users currentUser;
    private ObservableList<TaskViewModel> tasksList = FXCollections.observableArrayList();
    private int RoleID=0;

    public void handlesignout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tasksTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Failed to return to login. Please restart the application.");
        }

    }

    public void handledashboard(ActionEvent actionEvent) {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Developer/DeveloperDashboard.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the username
            DeveloperDashboardController dashboardController = loader.getController();
            dashboardController.initializeUser(Username,RoleID);

            // Get the current stage
            Stage stage = new Stage();
            Stage currentStage = (Stage) tasksTable.getScene().getWindow();
            currentStage.close();

            // Set the new scene
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the FXML file.");
        }
    }

    public static class TaskViewModel {
        private String taskName;
        private String description;
        private String assignedTo;
        private boolean complete;

        public TaskViewModel(String taskName, String description, String assignedTo, boolean complete) {
            this.taskName = taskName;
            this.description = description;
            this.assignedTo = assignedTo;
            this.complete = complete;
        }

        // Getters
        public String getTaskName() { return taskName; }
        public String getDescription() { return description; }
        public String getAssignedTo() { return assignedTo; }
        public boolean isComplete() { return complete; }

        // Setter for complete status
        public void setComplete(boolean complete) { this.complete = complete; }
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        tasksList = FXCollections.observableArrayList();
        tasksTable.setItems(tasksList);
    }

    public void initializeuser(String propusername, int proproleID) {
        this.Username = propusername;
        this.RoleID=proproleID;
        loadDatabase = new LoadDatabase();
        loadUserData();
        if (currentUser != null) {
            loadIncompleteTasks();
        } else {
            showAlert("Error", "Failed to load user data");
        }
    }

    private void setupTableColumns() {
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        assignedToColumn.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

        // Custom cell factory for status column with complete button
        statusColumn.setCellFactory(column -> new TableCell<TaskViewModel, Boolean>() {
            private final Button completeButton = new Button("Complete");

            @Override
            protected void updateItem(Boolean complete, boolean empty) {
                super.updateItem(complete, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                TaskViewModel task = getTableRow().getItem();
                if (!task.isComplete()) {
                    completeButton.setOnAction(event -> handleCompleteTask(task));
                    setGraphic(completeButton);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText("Completed");
                }
            }
        });
    }

    private void loadUserData() {
        if(RoleID==4) {
            loadDatabase.LoadUsers("TeamLead");
        }
        else if(RoleID==5)
        {loadDatabase.LoadUsers("Developer");}
        else{
            showError("No Role","Not Getting any role contact support team");
        }
        loadDatabase.UsersList.tempNode = loadDatabase.UsersList.GetHead();

        while (loadDatabase.UsersList.tempNode != null) {
            if (loadDatabase.UsersList.tempNode.data.GetUsername().equals(Username)) {
                currentUser = loadDatabase.UsersList.tempNode.data;
                break;
            }
            loadDatabase.UsersList.tempNode = loadDatabase.UsersList.tempNode.next;
        }
    }

    private void loadIncompleteTasks() {
        tasksList.clear();
        DatabaseConnection connection = new DatabaseConnection();
        String query =
                "SELECT t.*, m.Name as ModuleName, p.Name as ProjectName " +
                        "FROM tasks t " +
                        "JOIN modules m ON t.Module = m.ID " +
                        "JOIN projectstructure ps ON m.Structure = ps.ID " +
                        "JOIN projects p ON ps.ProjectID = p.ID " +
                        "WHERE t.UserID = ? AND t.Complete = 0";

        try (Connection con = connection.GetConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, currentUser.GetID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasksList.add(new TaskViewModel(
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getString("ProjectName") + " - " + rs.getString("ModuleName"),
                        rs.getBoolean("Complete")
                ));
            }

            tasksTable.setItems(tasksList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load tasks: " + e.getMessage());
        }
    }

    private void handleCompleteTask(TaskViewModel task) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "UPDATE tasks SET Complete = 1 WHERE Name = ?";

        try (Connection con = connection.GetConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, task.getTaskName());
            int result = stmt.executeUpdate();

            if (result > 0) {
                task.setComplete(true);
                // Remove the completed task from the table
                tasksList.remove(task);
                showAlert("Success", "Task marked as complete!");
            } else {
                showAlert("Error", "Failed to update task status");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to complete task: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
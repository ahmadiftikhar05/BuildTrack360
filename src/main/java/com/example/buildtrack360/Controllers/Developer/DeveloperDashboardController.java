package com.example.buildtrack360.Controllers.Developer;

import com.example.buildtrack360.Backend.*;
import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeveloperDashboardController {
    @FXML private TableView<TaskViewModel> tasksTable;
    @FXML private TableColumn<TaskViewModel, String> taskNameColumn;
    @FXML private TableColumn<TaskViewModel, String> projectColumn;
    @FXML private TableColumn<TaskViewModel, String> statusColumn;
    @FXML private PieChart taskStatusChart;
    @FXML private Label totalTasksLabel;
    @FXML private Label completedTasksLabel;
    @FXML private Label pendingTasksLabel;

    private LoadDatabase loadDatabase;
    private Users currentUser;
    private ObservableList<TaskViewModel> tasksList = FXCollections.observableArrayList();
    private String username = null;
    private int RoleID=0;

    public static class TaskViewModel {
        private String taskName;
        private String project;
        private String status;
        private String projectManager;

        public TaskViewModel(String taskName, String project, String status, String projectManager) {
            this.taskName = taskName;
            this.project = project;
            this.status = status;
            this.projectManager = projectManager;
        }

        // Getters
        public String getTaskName() { return taskName; }
        public String getProject() { return project; }
        public String getStatus() { return status; }
        public String getProjectManager() { return projectManager; }
    }

    public void initializeUser(String propUsername, int propRoleID) {
        this.username = propUsername;
        this.RoleID=propRoleID;
        // Now that we have the username, we can properly initialize everything
        initializeData();
    }

    @FXML
    public void initialize() {
        // Only set up the UI components here
        setupTableColumns();

        // Initialize empty lists/charts
        tasksList = FXCollections.observableArrayList();
        tasksTable.setItems(tasksList);

        // Initialize empty pie chart
        ObservableList<PieChart.Data> emptyChartData = FXCollections.observableArrayList(
                new PieChart.Data("No Data", 1)
        );
        taskStatusChart.setData(emptyChartData);
        taskStatusChart.setTitle("Task Status Distribution");
    }

    private void initializeData() {
        loadDatabase = new LoadDatabase();
        loadUserData();
        if (currentUser != null) {

            loadTasksData();
            updateDashboardMetrics();
        } else {
            System.err.println("Failed to load user data for username: " + username);
            showError("Error", "Failed to load user data. Please try logging in again.");
        }
    }

    private void setupTableColumns() {
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("project"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add column resize policies
        tasksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadUserData() {
        if (username == null) {
            System.err.println("Username is null");
            return;
        }
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
            if (loadDatabase.UsersList.tempNode.data.GetUsername().equals(username)) {
                currentUser = loadDatabase.UsersList.tempNode.data;
                break;
            }
            loadDatabase.UsersList.tempNode = loadDatabase.UsersList.tempNode.next;
        }
    }

    private void loadTasksData() {
        if (currentUser == null) {
            System.err.println("Cannot load tasks: currentUser is null");
            return;
        }

        tasksList.clear();

        DatabaseConnection connection = new DatabaseConnection();
        String query =
                "SELECT t.*, m.Name as ModuleName, p.Name as ProjectName, u.Name as ManagerName " +
                        "FROM tasks t " +
                        "JOIN modules m ON t.Module = m.ID " +
                        "JOIN projectstructure ps ON m.Structure = ps.ID " +
                        "JOIN projects p ON ps.ProjectID = p.ID " +
                        "LEFT JOIN users u ON p.ProjectManager = u.ID " +
                        "WHERE t.UserID = ?";

        try (Connection con = connection.GetConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, currentUser.GetID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasksList.add(new TaskViewModel(
                        rs.getString("Name"),
                        rs.getString("ProjectName"),
                        rs.getBoolean("Complete") ? "Completed" : "Pending",
                        rs.getString("ManagerName") != null ? rs.getString("ManagerName") : "Unassigned"
                ));
            }

            tasksTable.setItems(tasksList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading tasks: " + e.getMessage());
            showError("Database Error", "Failed to load tasks. Please try again later.");
        }
    }

    private void updateDashboardMetrics() {
        int totalTasks = tasksList.size();
        int completedTasks = (int) tasksList.stream()
                .filter(task -> task.getStatus().equals("Completed"))
                .count();
        int pendingTasks = totalTasks - completedTasks;

        // Update labels
        totalTasksLabel.setText("Total Tasks: " + totalTasks);
        completedTasksLabel.setText("Completed Tasks: " + completedTasks);
        pendingTasksLabel.setText("Pending Tasks: " + pendingTasks);

        // Update pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Completed", completedTasks),
                new PieChart.Data("Pending", pendingTasks)
        );
        taskStatusChart.setData(pieChartData);

        // Add percentage labels to pie chart
        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        javafx.beans.binding.Bindings.concat(
                                data.getName(), " ",
                                data.pieValueProperty().multiply(100.0 / totalTasks).intValue(), "%"
                        )
                )
        );
    }

    @FXML
    private void dashboardButtonOnClick() {
        loadTasksData();
        updateDashboardMetrics();
    }

    @FXML
    private void tasksButtonOnClick() {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Developer/Tasks.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the username
            DeveloperTaskController taskController = loader.getController();
            taskController.initializeuser(username,RoleID);

            // Get the current stage
            Stage stage = new Stage();
            Stage currentStage = (Stage) pendingTasksLabel.getScene().getWindow();
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

    @FXML
    private void signoutButtonOnClick() {
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
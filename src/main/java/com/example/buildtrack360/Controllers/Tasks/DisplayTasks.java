package com.example.buildtrack360.Controllers.Tasks;

import com.example.buildtrack360.Backend.*;
import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class DisplayTasks {
    @FXML
    private ComboBox<Project> projectComboBox;
    @FXML
    private ComboBox<Modules> moduleComboBox;
    @FXML
    private TableView<Tasks> tasksTable;
    @FXML
    private TableColumn<Tasks, String> taskNameColumn;
    @FXML
    private TableColumn<Tasks, String> descriptionColumn;
    @FXML
    private TableColumn<Tasks, String> assignedToColumn;
    @FXML
    private TableColumn<Tasks, Boolean> statusColumn;  // Changed to Boolean type

    private LoadDatabase loadDatabase;
    private ObservableList<Tasks> tasksList;
    private Users currentUser;

    public void initialize() {
        loadDatabase = new LoadDatabase();
        tasksList = FXCollections.observableArrayList();

        // Initialize columns
        setupTableColumns();

        // Setup ComboBoxes
        setupComboBoxes();

        // Add listeners
        addComboBoxListeners();
        tasksTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double click to open details
                Tasks selectedTask = tasksTable.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    openTaskDetails(selectedTask);
                }
            }
        });
    }

    private void setupTableColumns() {
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));

        // Custom cell factory for assigned user (converts UserID to name)
        assignedToColumn.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getUserID();
            String userName = getUserName(userId);
            return javafx.beans.binding.Bindings.createStringBinding(() -> userName);
        });

        // Fixed status column implementation
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("Complete"));
        statusColumn.setCellFactory(column -> new TableCell<Tasks, Boolean>() {
            private final Button completeButton = new Button("Complete");

            @Override
            protected void updateItem(Boolean complete, boolean empty) {
                super.updateItem(complete, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                Tasks task = getTableRow().getItem();

                if (complete != null && complete) {
                    setText("Completed");
                    setStyle("-fx-text-fill: green;");
                    setGraphic(null);
                } else {
                    setText(null);
                    completeButton.setOnAction(event -> handleCompleteTask(task));
                    setGraphic(completeButton);
                }
            }
        });
    }

    private void setupComboBoxes() {
        // Load projects
        loadDatabase.LoadProject();
        ObservableList<Project> projects = FXCollections.observableArrayList();
        loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.GetHead();
        while (loadDatabase.ProjectsList.tempNode != null) {
            projects.add(loadDatabase.ProjectsList.tempNode.data);
            loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.tempNode.next;
        }
        projectComboBox.setItems(projects);

        // Setup display converters
        projectComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Project project) {
                return project != null ? project.getName() : "";
            }

            @Override
            public Project fromString(String string) {
                return null;
            }
        });

        moduleComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Modules module) {
                return module != null ? module.GetName() : "";
            }

            @Override
            public Modules fromString(String string) {
                return null;
            }
        });
    }

    private void addComboBoxListeners() {
        projectComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadModulesForProject(newVal.getID());
            }
        });

        moduleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadTasksForModule(newVal.GetID());
            }
        });
    }

    private void loadModulesForProject(int projectId) {
        // Clear existing modules
        moduleComboBox.getItems().clear();

        // Load modules for the selected project
        loadDatabase.LoadModules(projectId);
        ObservableList<Modules> modules = FXCollections.observableArrayList();
        loadDatabase.ModulesList.tempNode = loadDatabase.ModulesList.GetHead();
        while (loadDatabase.ModulesList.tempNode != null) {
            modules.add(loadDatabase.ModulesList.tempNode.data);
            loadDatabase.ModulesList.tempNode = loadDatabase.ModulesList.tempNode.next;
        }
        moduleComboBox.setItems(modules);
    }

    private void loadTasksForModule(int moduleId) {
        // Clear existing tasks
        tasksList.clear();

        // Clear the existing tasks in loadDatabase before loading new ones
        loadDatabase.TasksList = new LinkedList<>(); // Reset the linked list

        // Load tasks for the selected project
        loadDatabase.LoadTasksByProject(projectComboBox.getValue().getName());

        // Filter tasks for selected module
        loadDatabase.TasksList.tempNode = loadDatabase.TasksList.GetHead();
        while (loadDatabase.TasksList.tempNode != null) {
            if (loadDatabase.TasksList.tempNode.data.getModuleID() == moduleId) {
                tasksList.add(loadDatabase.TasksList.tempNode.data);
            }
            loadDatabase.TasksList.tempNode = loadDatabase.TasksList.tempNode.next;
        }
        tasksTable.setItems(tasksList);
    }

    private String getUserName(int userId) {
        // Load user information if not already loaded
        loadDatabase.LoadUsers("Developer");
        loadDatabase.UsersList.tempNode = loadDatabase.UsersList.GetHead();
        while (loadDatabase.UsersList.tempNode != null) {
            if (loadDatabase.UsersList.tempNode.data.GetID() == userId) {
                return loadDatabase.UsersList.tempNode.data.GetName();
            }
            loadDatabase.UsersList.tempNode = loadDatabase.UsersList.tempNode.next;
        }
        return "Unknown User";
    }

    @FXML
    private void handleAddTask() {
        if (moduleComboBox.getValue() == null) {
            showAlert("Error", "Please select a module first.");
            return;
        }

        try {
            Stage stage = new Stage();
            Stage currentStage = (Stage) tasksTable.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Tasks/AddDeleteTask.fxml"));
            AnchorPane root = loader.load();


            Scene scene = new Scene(root);
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

    private int getModuleIdFromName(String moduleName) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "SELECT ID FROM modules WHERE Name = ?";

        try (Connection conn = connection.GetConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, moduleName);
            var resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @FXML
    private void handleDeleteTask() {
        Tasks selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("Error", "Please select a task to delete.");
            return;
        }

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this task?",
                ButtonType.YES, ButtonType.NO).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            // Implement task deletion logic here
            deleteTask(selectedTask);
        }
    }

    private void deleteTask(Tasks task) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "DELETE FROM tasks WHERE Name=?";

        try (Connection con = connection.GetConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, task.getName());
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                tasksList.remove(task);
                showAlert("Success", "Task deleted successfully!");
            } else {
                showAlert("Error", "Failed to delete task.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void handleCompleteTask(Tasks task) {

        DatabaseConnection connection = new DatabaseConnection();
        String query = "UPDATE tasks SET Complete = 1 WHERE Name = ?";

        try (Connection con = connection.GetConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, task.getName());
            int result = stmt.executeUpdate();

            if (result > 0) {
                task.setComplete(true);  // Changed from task.Complete = true

                // Refresh just this row instead of reloading all tasks
                int index = tasksList.indexOf(task);
                if (index >= 0) {
                    tasksList.set(index, task);  // This triggers the UI update
                }

                showAlert("Success", "Task marked as complete!");
            } else {
                showAlert("Error", "Failed to update task status in database.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to update task status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to set current user (call this when initializing the view)
    public void setCurrentUser(Users user) {
        this.currentUser = user;
    }
    private void openTaskDetails(Tasks task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Tasks/SingleDisplay.fxml"));
            Parent root = loader.load();

            SingleDisplayController controller = loader.getController();
            controller.setTaskData(task);

            // Get project and module names for the task
            String projectName = projectComboBox.getValue().getName();
            String moduleName = moduleComboBox.getValue().GetName();
            controller.setProjectAndModule(projectName, moduleName);

            Stage stage = new Stage();
            stage.setTitle("Task Details");
            stage.initModality(Modality.APPLICATION_MODAL); // Makes the window modal
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not open task details: " + e.getMessage());
        }
    }
    @FXML
    public void teamButtonOnClick(ActionEvent actionEvent)
    {
        try {
            javafx.stage.Stage stage=new javafx.stage.Stage();
            javafx.stage.Stage currentStage = (javafx.stage.Stage) projectComboBox.getScene().getWindow();

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
            javafx.stage.Stage stage=new javafx.stage.Stage();
            javafx.stage.Stage currentStage = (javafx.stage.Stage) projectComboBox.getScene().getWindow();

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
            javafx.stage.Stage stage=new javafx.stage.Stage();
            javafx.stage.Stage currentStage = (javafx.stage.Stage) projectComboBox.getScene().getWindow();

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
            javafx.stage.Stage stage=new javafx.stage.Stage();
            javafx.stage.Stage currentStage = (Stage) projectComboBox.getScene().getWindow();

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

    public void dashboardButtonOnClick(javafx.event.ActionEvent actionEvent) {
    }

    public void teamButtonOnClick(javafx.event.ActionEvent actionEvent) {
    }

    public void planningButtonOnClick(javafx.event.ActionEvent actionEvent) {
    }

    public void taskButtonOnClick(javafx.event.ActionEvent actionEvent) {
    }
}
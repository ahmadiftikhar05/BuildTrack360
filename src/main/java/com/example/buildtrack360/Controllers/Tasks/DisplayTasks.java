package com.example.buildtrack360.Controllers.Tasks;
import com.example.buildtrack360.Backend.*;
import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplayTasks implements Initializable {
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
    private TableColumn<Tasks, Button> statusColumn;

    private LoadDatabase loadDatabase;
    private ObservableList<Tasks> tasksList;
    private Users currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDatabase = new LoadDatabase();
        tasksList = FXCollections.observableArrayList();

        setupTableColumns();
        setupComboBoxes();
        addComboBoxListeners();
    }

    private void setupTableColumns() {
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));

        statusColumn.setCellFactory(column -> new TableCell<>() {
            final Button completeButton = new Button("Complete");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Tasks task = getTableView().getItems().get(getIndex());
                    if (task.Complete == true) {
                        setText("Completed");
                        setStyle("-fx-text-fill: green;");
                    } else {
                        completeButton.setOnAction(event -> handleCompleteTask(task));
                        setGraphic(completeButton);
                    }
                }
            }
        });
    }

    private void setupComboBoxes() {
        loadDatabase.LoadProject();
        ObservableList<Project> projects = FXCollections.observableArrayList();
        loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.GetHead();
        while (loadDatabase.ProjectsList.tempNode != null) {
            projects.add(loadDatabase.ProjectsList.tempNode.data);
            loadDatabase.ProjectsList.tempNode = loadDatabase.ProjectsList.tempNode.next;
        }
        projectComboBox.setItems(projects);

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
                // Now loading modules directly with project ID
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
        moduleComboBox.getItems().clear();
        loadDatabase.LoadModules(projectId); // Modified to use projectId directly
        ObservableList<Modules> modules = FXCollections.observableArrayList();
        loadDatabase.ModulesList.tempNode = loadDatabase.ModulesList.GetHead();
        while (loadDatabase.ModulesList.tempNode != null) {
            modules.add(loadDatabase.ModulesList.tempNode.data);
            loadDatabase.ModulesList.tempNode = loadDatabase.ModulesList.tempNode.next;
        }
        moduleComboBox.setItems(modules);
    }

    private void loadTasksForModule(int moduleId) {
        tasksList.clear();
        loadDatabase.LoadTasksByProject(projectComboBox.getValue().getName());

        loadDatabase.TasksList.tempNode = loadDatabase.TasksList.GetHead();
        while (loadDatabase.TasksList.tempNode != null) {
            if (loadDatabase.TasksList.tempNode.data.getModuleID() == moduleId) {
                tasksList.add(loadDatabase.TasksList.tempNode.data);
            }
            loadDatabase.TasksList.tempNode = loadDatabase.TasksList.tempNode.next;
        }
        tasksTable.setItems(tasksList);
    }

    @FXML
    private void handleAddTask() {
        if (moduleComboBox.getValue() == null) {
            showAlert("Error", "Please select a module first.");
            return;
        }
        // Add task creation dialog/form implementation here
    }

    @FXML
    private void handleEditTask() {
        Tasks selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("Error", "Please select a task to edit.");
            return;
        }
        // Add task editing dialog/form implementation here
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
        }
    }

    private void handleCompleteTask(Tasks task) {
        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                "Mark this task as complete?",
                ButtonType.YES, ButtonType.NO).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            task.Complete = true;

            DatabaseConnection connection = new DatabaseConnection();
            String query = "UPDATE tasks SET Complete = 1 WHERE ID = ?";

            try (Connection con = connection.GetConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, task.getID());
                stmt.executeUpdate();

                loadTasksForModule(moduleComboBox.getValue().GetID());
                showAlert("Success", "Task marked as complete!");
            } catch (SQLException e) {
                showAlert("Error", "Failed to update task status: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setCurrentUser(Users user) {
        this.currentUser = user;
    }
}
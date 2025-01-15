package com.example.buildtrack360.Controllers.Tasks;

import com.example.buildtrack360.Backend.Tasks;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class SingleDisplayController {
    @FXML private Label taskNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label projectLabel;
    @FXML private Label moduleLabel;
    @FXML private Label assignedToLabel;
    @FXML private Label statusLabel;
    @FXML private Label startDateLabel;
    @FXML private Label dueDateLabel;
    @FXML private Button editButton;
    @FXML private Button closeButton;

    private Tasks currentTask;
    private LoadDatabase loadDatabase;

    @FXML
    private void initialize() {
        loadDatabase = new LoadDatabase();
    }

    public void setTaskData(Tasks task) {
        this.currentTask = task;

        // Set the labels with task data
        taskNameLabel.setText(task.getName());
        descriptionLabel.setText(task.getDescription());

        // Get assigned user name
        loadDatabase.LoadUsers("Developer");
        String assignedUserName = "Unknown User";
        loadDatabase.UsersList.tempNode = loadDatabase.UsersList.GetHead();
        while (loadDatabase.UsersList.tempNode != null) {
            if (loadDatabase.UsersList.tempNode.data.GetID() == task.getUserID()) {
                assignedUserName = loadDatabase.UsersList.tempNode.data.GetName();
                break;
            }
            loadDatabase.UsersList.tempNode = loadDatabase.UsersList.tempNode.next;
        }
        assignedToLabel.setText(assignedUserName);

        // Set status
        statusLabel.setText(task.Complete ? "Completed" : "In Progress");
        if (task.Complete) {
            statusLabel.setStyle("-fx-text-fill: green;");
        }


    }

    public void setProjectAndModule(String projectName, String moduleName) {
        projectLabel.setText(projectName);
        moduleLabel.setText(moduleName);
    }



    @FXML
    private void handleClose() {
        // Get the current stage and close it
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
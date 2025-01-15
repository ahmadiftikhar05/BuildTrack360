package com.example.buildtrack360.Controllers.Tasks;

import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AddDeleteTaskController {

    //Declaring FXML Elements
    @FXML
    TextField NameTextField;
    @FXML
    TextArea DescriptionTextField;
    @FXML
    ComboBox<String> TeamCombobox;
    @FXML
    ComboBox<String> MemberCombobox;
    private int moduleId; // Add this field

    // Getter and setter for moduleId
    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getModuleId() {
        return moduleId;
    }

    //When FXMl is displayed initialize function will be called
    public void initialize(){
        ObservableList<String> Team = FXCollections.observableArrayList();

        //Getting Team Data and populating in ComboBox
        LoadDatabase Load=new LoadDatabase();
        Load.LoadTeams();
        Load.TeamList.tempNode=Load.TeamList.GetHead();
        while(Load.TeamList.tempNode!=null){
            Team.add(Load.TeamList.tempNode.data.GetName());
            Load.TeamList.tempNode=Load.TeamList.tempNode.next;
        }

        //Setting TeamComboBox with Team Data
        TeamCombobox.setItems(Team);

        //Function is called when ComboBox item is selected
        TeamCombobox.setOnAction(event -> {
            String selectedName = TeamCombobox.getValue();
            if (selectedName != null) {
                ObservableList<String> Member = FXCollections.observableArrayList();

                Load.TeamList.tempNode=Load.TeamList.GetHead();
                while(Load.TeamList.tempNode!=null){
                    if(Load.TeamList.tempNode.data.GetName().equals(TeamCombobox.getValue()))
                    {break;}
                    Load.TeamList.tempNode=Load.TeamList.tempNode.next;
                }

                LoadDatabase LoadMembers=new LoadDatabase();
                LoadMembers.LoadMembers(Load.TeamList.tempNode.data.GetID());
                LoadMembers.LoadUsers("Developer");
                LoadMembers.UsersList.tempNode=LoadMembers.UsersList.GetHead();
                while(LoadMembers.UsersList.tempNode!=null){
                    LoadMembers.MemberList.tempNode=LoadMembers.MemberList.GetHead();
                    while (LoadMembers.MemberList.tempNode!=null){
                        if(LoadMembers.MemberList.tempNode.data.GetUserID()==LoadMembers.UsersList.tempNode.data.GetID())
                        {
                            Member.add(LoadMembers.UsersList.tempNode.data.GetName());
                        }
                        LoadMembers.MemberList.tempNode=LoadMembers.MemberList.tempNode.next;
                    }
                    LoadMembers.UsersList.tempNode=LoadMembers.UsersList.tempNode.next;
                }

                MemberCombobox.setItems(Member);

            }
        });
    }

    public void handleassignbutton(ActionEvent actionEvent) {
        // Validate input fields
        if (NameTextField.getText().isEmpty() ||
                DescriptionTextField.getText().isEmpty() ||
                TeamCombobox.getValue() == null ||
                MemberCombobox.getValue() == null) {

            showAlert("Error", "All fields are required!");
            return;
        }

        // Get selected member's UserID
        int userId = getUserIdByName(MemberCombobox.getValue());

        // Add task to database with moduleId
        if (addTask(NameTextField.getText(), DescriptionTextField.getText(), userId, moduleId)) {
            showAlert("Success", "Task assigned successfully!");
            clearFields();
        } else {
            showAlert("Error", "Failed to assign task!");
        }
    }

    private int getUserIdByName(String name) {
        LoadDatabase load = new LoadDatabase();
        load.LoadUsers("Developer");
        load.UsersList.tempNode = load.UsersList.GetHead();

        while (load.UsersList.tempNode != null) {
            if (load.UsersList.tempNode.data.GetName().equals(name)) {
                return load.UsersList.tempNode.data.GetID();
            }
            load.UsersList.tempNode = load.UsersList.tempNode.next;
        }
        return -1;
    }

    private boolean addTask(String name, String description, int userId, int moduleId) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "INSERT INTO tasks (Name, Description, UserID, Module, Complete) VALUES (?, ?, ?, ?, 0)";

        try (Connection conn = connection.GetConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, moduleId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        NameTextField.clear();
        DescriptionTextField.clear();
        TeamCombobox.setValue(null);
        MemberCombobox.setValue(null);
    }

    public void HandleBackButton(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Stage currentStage = (Stage) MemberCombobox.getScene().getWindow();

            // Load the FXML file from the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Tasks/Display.fxml"));
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

    @FXML
    public void teamButtonOnClick(ActionEvent actionEvent)
    {
        try {
            Stage stage=new Stage();
            Stage currentStage = (Stage) NameTextField.getScene().getWindow();

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
            Stage currentStage = (Stage) NameTextField.getScene().getWindow();

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
            Stage currentStage = (Stage) NameTextField.getScene().getWindow();

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
            Stage currentStage = (Stage) NameTextField.getScene().getWindow();

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
}


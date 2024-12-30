package com.example.buildtrack360.Controllers.ProjectManager;

import com.example.buildtrack360.Backend.Teams;
import com.example.buildtrack360.Backend.Users;
import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeamsController {
    @FXML
    TextField NameCombobox;
    @FXML
    VBox MemberComboboxVBox;
    private LinkedList<Members> TeamMembersList= new LinkedList<Members>();
    @FXML
    public void initialize(){
    createNewMemberRow();
    }

    public void handlecreateteambutton(ActionEvent actionEvent) {
        boolean NewMemberComboboxFlag=true;
        TeamMembersList.tempNode=TeamMembersList.GetHead();
        while(TeamMembersList.tempNode!=null){
            if(TeamMembersList.tempNode.data.Name.isEmpty()){
                NewMemberComboboxFlag=false;
                break;
            }
            TeamMembersList.tempNode=TeamMembersList.tempNode.next;
        }


        if(!(NameCombobox.getText().isEmpty())&& NewMemberComboboxFlag) {
            //Loading Users
            LoadDatabase Load = new LoadDatabase();
            Load.LoadUsers("Developer");
            Load.UsersList.tempNode = Load.UsersList.GetHead();
            //Creating Team Object to store
            Teams team = new Teams(NameCombobox.getText());
            while (Load.UsersList.tempNode != null) {
                TeamMembersList.tempNode = TeamMembersList.GetHead();
                while (TeamMembersList.tempNode != null) {
                    if (TeamMembersList.tempNode.data.Name.equals(Load.UsersList.tempNode.data.GetName())) {
                        team.TeamMembersList.InsertData(Load.UsersList.tempNode.data);
                    }
                    TeamMembersList.tempNode = TeamMembersList.tempNode.next;
                }
                Load.UsersList.tempNode = Load.UsersList.tempNode.next;
            }

            int TeamID = 0;
            DatabaseConnection connection = new DatabaseConnection();
            try (Connection con = connection.GetConnection();
                 PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO teams (Name) VALUES(?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
                preparedStatement.setString(1, NameCombobox.getText());
                preparedStatement.executeUpdate();
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    // Get the generated team ID (auto-generated primary key)
                    try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            TeamID = generatedKeys.getInt(1);  // Assign the generated team ID
                            System.out.println("Team added successfully with ID: " + TeamID);
                        }
                    }


                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            team.TeamMembersList.tempNode = team.TeamMembersList.GetHead();
            while (team.TeamMembersList.tempNode != null) {
                try (Connection con = connection.GetConnection();
                     PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO teammembers (UserID,Team) VALUES(?,?)");) {
                    preparedStatement.setInt(1, team.TeamMembersList.tempNode.data.GetID());
                    preparedStatement.setInt(2, TeamID);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                team.TeamMembersList.tempNode = team.TeamMembersList.tempNode.next;
            }
        }
        else{
            showAlert("Error", "All Members are not selected or Name is not entered.");
        }
    }

    public void handleaddmemberrowbutton(ActionEvent actionEvent) {
    createNewMemberRow();
    }

    private void createNewMemberRow() {
        Label newMemberLabel = new Label("New Member");
        AnchorPane.setLeftAnchor(newMemberLabel, 10.0);

        ComboBox<String> newMemberComboBox = new ComboBox<>();
        newMemberComboBox.setPrefWidth(185);

        // Here, add the values to the ComboBox (you can modify the list based on your data source)
        ObservableList<String> UsersList= FXCollections.observableArrayList();
        LoadDatabase Load=new LoadDatabase();
        Load.LoadUsers("Developer");
        Load.UsersList.tempNode=Load.UsersList.GetHead();
        while(Load.UsersList.tempNode!=null){
            UsersList.add(Load.UsersList.tempNode.data.GetName());
            Load.UsersList.tempNode=Load.UsersList.tempNode.next;
        }
        newMemberComboBox.setItems(UsersList);

        // Add listener to ComboBox to update the TeamMembersList when a member is selected
        newMemberComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                // Add the selected member to the TeamMembersList
                Members member = new Members(newValue);
                TeamMembersList.InsertData(member);
                System.out.println(member.Name);
            }
        });

        // Add the components to the VBox
        MemberComboboxVBox.getChildren().add(newMemberLabel);
        MemberComboboxVBox.getChildren().add(newMemberComboBox);
    }


    static public class Members{
        private final String Name;

        public Members(String moduleName) {
            this.Name = moduleName;
        }

        // Getter for moduleName
        public String getModuleName() {
            return Name;
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

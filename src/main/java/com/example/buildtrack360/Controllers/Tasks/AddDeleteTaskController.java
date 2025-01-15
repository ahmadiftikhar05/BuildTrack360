package com.example.buildtrack360.Controllers.Tasks;

import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    //Function will be called when Assign Button is Clicked
    public void handleassignbutton(ActionEvent actionEvent) {
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

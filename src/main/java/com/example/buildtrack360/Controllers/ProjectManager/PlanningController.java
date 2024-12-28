package com.example.buildtrack360.Controllers.ProjectManager;

import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class PlanningController {
    @FXML
    ComboBox<String> ProjectCombobox;
    @FXML
    ComboBox<String> TeamLeadCombobox;
    @FXML
    public void initialize(){
        //List for populating projects
        ObservableList<String> observableprojectsList = FXCollections.observableArrayList();
        LoadDatabase Load=new LoadDatabase();
        Load.LoadProject();
        Load.ProjectsList.tempNode=Load.ProjectsList.GetHead();
        while(Load.ProjectsList.tempNode!=null){
            //Getting InProgress as a check so that only InProgress
            if(Load.ProjectsList.tempNode.data.getStageName().equals("InProgress")){
                //Adding project to observable list which are in progress
                observableprojectsList.add(Load.ProjectsList.tempNode.data.getName());
            }
            Load.ProjectsList.tempNode=Load.ProjectsList.tempNode.next;
        }
        //Adding projects to Combobox
        ProjectCombobox.setItems(observableprojectsList);


        //Populating TeamLead Combobox
        ObservableList<String> observableteamleadList = FXCollections.observableArrayList();
        Load.LoadUsers("TeamLead");
        Load.UsersList.tempNode=Load.UsersList.GetHead();
        while(Load.UsersList.tempNode!=null){
                //Adding project to observable list which are in progress
            observableprojectsList.add(Load.UsersList.tempNode.data.GetName());
            Load.UsersList.tempNode=Load.UsersList.tempNode.next;
        }
        //Adding teamleads to Combobox
        TeamLeadCombobox.setItems(observableteamleadList);
    }
}

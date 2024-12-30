package com.example.buildtrack360.Controllers.ProjectManager;

import com.example.buildtrack360.Backend.Project.Project;
import com.example.buildtrack360.Backend.ProjectStructure;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import javafx.scene.input.MouseEvent;

public class PlanningController {
    // Define the TableView and TableColumns
    @FXML
    private TableView<Module> moduleTable;
    @FXML
    private TableColumn<Module, String> moduleNameColumn;
    @FXML
    private TableColumn<Module,String> moduleSrColumn;
    @FXML
    private Button addModuleButton;

    // ObservableList for holding modules
    private ObservableList<Module> modules;
    private ObservableList<ProjectStructure> projectstrutures;


    @FXML
    ComboBox<String> ProjectCombobox;
    @FXML
    ComboBox<String> TeamLeadCombobox;
    @FXML
    ComboBox<String> TeamCombobox;
    @FXML
    public void initialize(){
        // Initialize the ObservableList for storing modules
        modules = FXCollections.observableArrayList();
        moduleTable.setEditable(true);

        // Configure the moduleNameColumn to show and edit module names
        moduleNameColumn.setCellValueFactory(cellData -> cellData.getValue().moduleNameProperty());

        // Make the module name column editable using TextFieldTableCell
        moduleNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        moduleNameColumn.setOnEditCommit(event -> {
            // Get the edited value
            String newValue = event.getNewValue();
            // Get the row that was edited
            Module module = event.getRowValue();
            // Update the module name in the model
            module.setModuleName(newValue);
        });


        // Set the table items
        moduleTable.setItems(modules);

        // Optional: Add an initial module to the table for testing
        modules.add(new Module("New Module"));




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
            observableteamleadList.add(Load.UsersList.tempNode.data.GetName());
            Load.UsersList.tempNode=Load.UsersList.tempNode.next;
        }
        //Adding teamleads to Combobox
        TeamLeadCombobox.setItems(observableteamleadList);
    }

    public void handlesavebutton(ActionEvent actionEvent) {
        int ProjectID=0,TeamLeadID=0;
        LoadDatabase Load=new LoadDatabase();

        //Loading Projects
        Load.LoadProject();
        Load.ProjectsList.tempNode=Load.ProjectsList.GetHead();
        //Getting Projects ID
        while(Load.ProjectsList.tempNode!=null)
        {
            if(Load.ProjectsList.tempNode.data.getName().equals(ProjectCombobox.getValue())){
                ProjectID=Load.ProjectsList.tempNode.data.getID();
            }
            Load.ProjectsList.tempNode=Load.ProjectsList.tempNode.next;
        }

        //Loading TeamLead
        Load.LoadUsers("TeamLead");
        Load.UsersList.tempNode=Load.UsersList.GetHead();
        //Getting TeamLead ID
        while(Load.UsersList.tempNode!=null){
            if(Load.UsersList.tempNode.data.GetName().equals(TeamLeadCombobox.getValue())) {
                TeamLeadID=Load.UsersList.tempNode.data.GetID();
            }
            Load.UsersList.tempNode=Load.UsersList.tempNode.next;
        }

        LoadDatabase Load1=new LoadDatabase();
        Load1.LoadTeams(ProjectID);

        //Loading Team
        //There should be multiple Teams for a project;
        //ProjectStructure projectStructure=new ProjectStructure(ProjectID,TeamLeadID,);

    }


    public static class Module {
        private final SimpleStringProperty moduleName;

        public Module(String moduleName) {
            this.moduleName = new SimpleStringProperty(moduleName);
        }

        // Getter for moduleName
        public String getModuleName() {
            return moduleName.get();
        }

        // Setter for moduleName
        public void setModuleName(String moduleName) {
            this.moduleName.set(moduleName);
        }

        // Property for moduleName (for binding)
        public SimpleStringProperty moduleNameProperty() {
            return moduleName;
        }
    }
    @FXML
    private void handleAddModule() {
        Module newModule = new Module("New Module");
        modules.add(newModule);
    }
}

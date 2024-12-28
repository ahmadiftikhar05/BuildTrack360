package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import com.example.buildtrack360.Backend.Project.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ViewandEditProjectController {

    @FXML
    TextField NameTextField;
    @FXML
    TextField AmountTextField;
    @FXML
    Hyperlink AgreementHyperLink;
    @FXML
    ComboBox<String> CustomerCombobox;
    @FXML
    ComboBox<String> StageCombobox;
    @FXML
    Label ProjectManagerLabel;
    @FXML
    ComboBox<String> ProjectManagerCombobox;

    private Project project;

    @FXML
    void initialize(){
        updateProjectManagerVisibility();
        StageCombobox.setOnAction(event -> updateProjectManagerVisibility());

        // Check initial state of statusComboBox and set visibility accordingly
        updateProjectManagerVisibility();
    }

    void setProject(Project propproject){

        project=propproject;
        if(project.getStageName().equals("InProgress")){
            ProjectManagerCombobox.setVisible(true);
            ProjectManagerLabel.setVisible(true);

        }
        NameTextField.setText(propproject.getName());
        AmountTextField.setText(String.valueOf(propproject.getAmount()));
        AgreementHyperLink.setText(propproject.getAgreement());
        String urlpath=propproject.getAgreement();

       // Path path= (Path) Paths.get(urlpath);
       // if (path != null ) {
       //     AgreementHyperLink.setOnAction(event -> openFile(path));
       // } else {
       //     AgreementHyperLink.setOnAction(event -> {
       //         System.out.println("No agreement file available.");
       //     });
      //  }

        //Setting Customer ComboxBox Data
        LoadDatabase Load=new LoadDatabase();
        Load.LoadCustomers();
        Load.CustomersList.tempNode=Load.CustomersList.GetHead();
        ObservableList<String> observablecustomerList = FXCollections.observableArrayList();

        while(Load.CustomersList.tempNode!=null){
            observablecustomerList.add(Load.CustomersList.tempNode.data.getName());
            if(project.getCustomerName().equals(Load.CustomersList.tempNode.data.getName())){
                //Setting Comboxbox Default value to preview Customer Name
                CustomerCombobox.getSelectionModel().select(project.getCustomerName());
            }
            Load.CustomersList.tempNode=Load.CustomersList.tempNode.next;
        }
        CustomerCombobox.setItems(observablecustomerList);

        //Loading Stages data in ComboBox
        Load.LoadStages();
        ObservableList<String> observablestageList = FXCollections.observableArrayList();
        Load.StageList.tempNode=Load.StageList.GetHead();
        while(Load.StageList.tempNode!=null){
            observablestageList.add(Load.StageList.tempNode.data.getName());
            if(project.getStageName().equals(Load.StageList.tempNode.data.getName())){
                //Setting Comboxbox Default value to preview Stages
                StageCombobox.getSelectionModel().select(project.getStageName());
            }
            Load.StageList.tempNode=Load.StageList.tempNode.next;
        }
        StageCombobox.setItems(observablestageList);

        //Loading ProjectManager Data in ProjectManager ComboBox
        Load.LoadUsers("ProjectManager");

        ObservableList<String> observableprojectmanagersList = FXCollections.observableArrayList();
        Load.UsersList.tempNode = Load.UsersList.GetHead();
        System.out.println(project.getProjectManagerID());
        while (Load.UsersList.tempNode != null) {
            if(Load.UsersList.tempNode.data.GetRoleName().equals("ProjectManager")) {
                observableprojectmanagersList.add(Load.UsersList.tempNode.data.GetName());
            }
            Load.UsersList.tempNode = Load.UsersList.tempNode.next;
        }
        //Setting Comboxbox Default value to preview Project Manager
        String ProjectManagerNameTemp=project.GetProjectManagerName();
        ProjectManagerCombobox.getSelectionModel().select(ProjectManagerNameTemp);
            ProjectManagerCombobox.setItems(observableprojectmanagersList);

    }


    public void handlebackbutton(ActionEvent actionEvent) {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/BuildPath.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = new Stage();
            Stage currentStage = (Stage) CustomerCombobox.getScene().getWindow();
            currentStage.close();
            stage.setMaximized(true);  // Maximize the window
            stage.setResizable(false);  // Make the window non-resizable
            // Set the new scene
            stage.setTitle("BuildPath360");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the FXML file.");
        }
    }

    public void handleeditbutton(ActionEvent actionEvent) {
        //Checking if any edit is made or not
        if(!(project.getName().equals(NameTextField.getText()))||!(String.valueOf(project.getAmount()).equals(AmountTextField.getText()))||!(CustomerCombobox.getValue().equals(project.getCustomerName()))||!(StageCombobox.getValue().equals(project.getStageName()))||ProjectManagerCombobox.getValue()!=null){
            //Getting CustomerID from Customer Combobox
            int CustomerID=0;
            System.out.println(CustomerCombobox.getValue());
            LoadDatabase Load =new LoadDatabase();
            Load.LoadCustomers();
            Load.CustomersList.tempNode=Load.CustomersList.GetHead();
            while(Load.CustomersList.tempNode!=null){
                if(CustomerCombobox.getValue().equals(Load.CustomersList.tempNode.data.getName())){
                    CustomerID=Load.CustomersList.tempNode.data.getID();
                    break;
                }
                System.out.println(Load.CustomersList.tempNode.data.getName());
                Load.CustomersList.tempNode=Load.CustomersList.tempNode.next;
            }
            if(CustomerID==0){
                showAlert("Error","Error in getting Customer ID");
                return;
            }

            //Getting StageID
            int StageID=0;
            Load.LoadStages();
            Load.StageList.tempNode=Load.StageList.GetHead();
            while(Load.StageList.tempNode!=null){
                if(StageCombobox.getValue().equals(Load.StageList.tempNode.data.getName())){
                    StageID=Load.StageList.tempNode.data.getID();
                    break;
                }
                Load.StageList.tempNode=Load.StageList.tempNode.next;
            }
            if(StageID==0){
                showAlert("Error","Error in getting Stage ID");
                return;
            }

            //Getting ProjectManagerID
            int ProjectManagerID=0;
            if(StageCombobox.getValue().equals("InProgress")){
                Load.LoadUsers("ProjectManager");
                Load.UsersList.tempNode=Load.UsersList.GetHead();
                while(Load.UsersList.tempNode!=null){
                    if(ProjectManagerCombobox.getValue().equals(Load.UsersList.tempNode.data.GetName())){
                        ProjectManagerID=Load.UsersList.tempNode.data.GetID();
                        break;
                    }
                    Load.UsersList.tempNode=Load.UsersList.tempNode.next;
                }
            }

            //Editing project data in DB
            DatabaseConnection con=new DatabaseConnection();
            if(StageCombobox.getValue().equals("InProgress")){

                try (
                        Connection connection = con.GetConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE projects SET Name = ?, Customer=?, Amount=?, Stages=?,ProjectManager=? WHERE ID = ?"))
                {
                    preparedStatement.setString(1, NameTextField.getText());
                    preparedStatement.setInt(2, CustomerID);
                    preparedStatement.setInt(3, Integer.parseInt(AmountTextField.getText()));
                    preparedStatement.setInt(4, StageID);
                     // Set the project ID (the one which is searched)
                    preparedStatement.setInt(5,ProjectManagerID);
                    preparedStatement.setInt(6, project.getID());

                    // Execute the update query
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        String checkProjectManagerQuery = "SELECT * FROM projectmanagerprojects WHERE Project = ?";

                        try (PreparedStatement checkStmt = connection.prepareStatement(checkProjectManagerQuery)) {
                            checkStmt.setInt(1, project.getID()); // Check if the project already has an entry for the manager
                            ResultSet resultSet = checkStmt.executeQuery();

                            if (resultSet.next()) {
                                // If the entry exists, update the manager_id for this project
                                String updateProjectManagerQuery = "UPDATE projectmanagerprojects SET ProjectManager = ? WHERE Project = ?";
                                try (PreparedStatement updateStmt = connection.prepareStatement(updateProjectManagerQuery)) {
                                    updateStmt.setInt(1, ProjectManagerID);
                                    updateStmt.setInt(2, project.getID());
                                    updateStmt.executeUpdate();
                                    System.out.println("Manager ID updated in projectmanagerprojects table.");
                                }
                            }
                            else {
                                // If no entry exists, insert a new relationship
                                String insertProjectManagerQuery = "INSERT INTO projectmanagerprojects (ProjectManager, Project) VALUES (?, ?)";
                                try (PreparedStatement insertStmt = connection.prepareStatement(insertProjectManagerQuery)) {
                                    insertStmt.setInt(1, ProjectManagerID);
                                    insertStmt.setInt(2, project.getID());
                                    insertStmt.executeUpdate();
                                    System.out.println("Manager ID inserted into projectmanagerprojects table.");
                                }
                            }
                        }
                        showAlert("Successful", "Successfully Updated the project data");

                    } else {
                        showAlert("Error", "No project found with the given ID.");
                    }
                } catch (SQLException e) {
                    System.out.println("Something went wrong in ViewandEditprojectController in Updating project data");
                }
            }
            else {
                try (
                        Connection connection = con.GetConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE projects SET Name = ?, Customer=?, Amount=?, Stages=? WHERE ID = ?")) {
                    preparedStatement.setString(1, NameTextField.getText());
                    preparedStatement.setInt(2, CustomerID);
                    preparedStatement.setInt(3, Integer.parseInt(AmountTextField.getText()));
                    preparedStatement.setInt(4, StageID);
                    preparedStatement.setInt(5, project.getID());  // Set the project ID (the one which is searched)

                    // Execute the update query
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        showAlert("Successful", "Successfully Updated the project data");
                    } else {
                        showAlert("Error", "No project found with the given ID.");
                    }
                } catch (SQLException e) {
                    System.out.println("Something went wrong in ViewandEditprojectController in Updating project data");
                }
            }
        }
        else{
            showAlert("Info", "No changes made");
        }
    }

    //Display for Project Manager when Stage is Inprogress
    private void updateProjectManagerVisibility() {
        if ("InProgress".equals(StageCombobox.getValue())) {
            ProjectManagerCombobox.setVisible(true);
            ProjectManagerLabel.setVisible(true);// Show the project manager ComboBox
        } else {
            ProjectManagerLabel.setVisible(false);
            ProjectManagerCombobox.setVisible(false);// Hide the project manager ComboBox
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

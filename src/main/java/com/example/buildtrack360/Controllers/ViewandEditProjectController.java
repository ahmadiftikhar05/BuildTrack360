package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import com.example.buildtrack360.Project.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    private Project project;

    @FXML
    void initialize(){}

    void setProject(Project propproject){
        project=propproject;
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
        LoadDatabase Load=new LoadDatabase();
        Load.LoadCustomers();
        Load.CustomersList.tempNode=Load.CustomersList.GetHead();
        ObservableList<String> observablecustomerList = FXCollections.observableArrayList();

        while(Load.CustomersList.tempNode!=null){
            observablecustomerList.add(Load.CustomersList.tempNode.data.getName());
            if(project.getCustomerName().equals(Load.CustomersList.tempNode.data.getName())){
                CustomerCombobox.getSelectionModel().select(project.getCustomerName());
            }
            Load.CustomersList.tempNode=Load.CustomersList.tempNode.next;
        }
        CustomerCombobox.setItems(observablecustomerList);

        //Setting Stages data in combobox
        Load.LoadStages();
        ObservableList<String> observablestageList = FXCollections.observableArrayList();
        Load.StageList.tempNode=Load.StageList.GetHead();
        while(Load.StageList.tempNode!=null){
            observablestageList.add(Load.StageList.tempNode.data.getName());
            if(project.getStageName().equals(Load.StageList.tempNode.data.getName())){
                StageCombobox.getSelectionModel().select(project.getStageName());
            }
            Load.StageList.tempNode=Load.StageList.tempNode.next;
        }
        StageCombobox.setItems(observablestageList);

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
        if(!(project.getName().equals(NameTextField.getText()))||!(String.valueOf(project.getAmount()).equals(AmountTextField.getText()))||!(CustomerCombobox.getValue().equals(project.getCustomerName()))||!(StageCombobox.getValue().equals(project.getStageName()))){
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

            //Editing project data in DB
            DatabaseConnection con=new DatabaseConnection();
            try(
            Connection connection=con.GetConnection();
            PreparedStatement preparedStatement= connection.prepareStatement("UPDATE projects SET Name = ?, Customer=?, Amount=?, Stages=? WHERE ID = ?"))
            {
                preparedStatement.setString(1, NameTextField.getText());
                preparedStatement.setInt(2,CustomerID);
                preparedStatement.setInt(3,Integer.parseInt(AmountTextField.getText()));
                preparedStatement.setInt(4,StageID);

                preparedStatement.setInt(5, project.getID());  // Set the project ID (the one you're updating)

                // Execute the update query
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    showAlert("Successful","Successfully Updated the project data");
                } else {
                    showAlert("Error","No project found with the given ID.");
                }
            } catch (SQLException e) {
                System.out.println("Something went wrong in ViewandEditprojectController in Updating project data");
            }
        }
        else{
            showAlert("Info", "No changes made");
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

package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import com.example.buildtrack360.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddProjectController {
    @FXML
    private TextField filePathField;
    @FXML
    private TextField NameTextField;
     @FXML
    private TextField AmountTextField;
     @FXML
    private ComboBox<String> CustomerCombobox;
     @FXML
     private Label WarningLabel;
    @FXML
    public void initialize() {
        try {
            populateCustomerCombobox();
        }catch (Exception e){
            System.out.println("Something is Wrong");
        }       System.out.println("Initialization complete"); // Debug statement
    }

    void populateCustomerCombobox(){
        DatabaseConnection con = new DatabaseConnection();
        ObservableList<String> CustomerList = FXCollections.observableArrayList();

        try (Connection connection = con.GetConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM customers");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String customerName = resultSet.getString("Name");
                CustomerList.add(customerName);
                System.out.println(customerName);
            }

           CustomerCombobox.setItems(CustomerList);

        } catch (SQLException e) {
            showAlert("Error", "Failed to load roles: " + e.getMessage());
            e.printStackTrace();
        }
        catch(Error e){
            System.out.println("Something went wrong");
        }
    }


    public void handleuploadagreement() {
        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set file extension filters (optional)
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("Word Files", "*.docx", "*.doc");
        fileChooser.getExtensionFilters().add(pdfFilter);
        fileChooser.getExtensionFilters().add(docxFilter);

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Check if a file was selected
        if (selectedFile != null) {
            // Get and display the absolute file path
            String filePath = selectedFile.getAbsolutePath();
            filePathField.setText(filePath);  // Display file path in TextField
        }
    }

    public void handleAddProjectButton(ActionEvent actionEvent) {

        if (!NameTextField.getText().isEmpty()||AmountTextField.getText().isEmpty()||CustomerCombobox.getValue()!=null||filePathField.getText().isEmpty()) {
            int Amount=0;
            try {
                Amount = Integer.parseInt(AmountTextField.getText());
                LoadDatabase Load=new LoadDatabase();
                Load.LoadCustomers();
                Load.CustomersList.tempNode=Load.CustomersList.GetHead();

                while(Load.CustomersList.tempNode != null) {
                    if (Load.CustomersList.tempNode.data.getName().equals(CustomerCombobox.getValue())) {
                        System.out.println("From DB: "+Load.CustomersList.tempNode.data.getName());
                        break;
                    }
                    else{
                        Load.CustomersList.tempNode=Load.CustomersList.tempNode.next;
                    }
                }

                int CustomerID= Load.CustomersList.tempNode != null ? Load.CustomersList.tempNode.data.getID() : 1;

                Project project = new Project(NameTextField.getText(),CustomerID,Amount,filePathField.getText());
                project.AddProject(NameTextField.getText(),CustomerID,Amount,filePathField.getText());
                showAlert("Success","Added a New Project");
            }
            catch(NumberFormatException e){
                System.out.println("Incorrect Amount");
                WarningLabel.setText("Incorrect Amount");
                WarningLabel.setVisible(true);
            }


        } else {
            if(NameTextField.getText().isEmpty()&&filePathField.getText().isEmpty()&&AmountTextField.getText().isEmpty()&&CustomerCombobox.getValue()==null){
                WarningLabel.setText("All Fields are Empty");
                WarningLabel.setVisible(true);
            }
            else if(NameTextField.getText().isEmpty()){
                WarningLabel.setText("Name is Empty");
                WarningLabel.setVisible(true);
            }
            else if(AmountTextField.getText().isEmpty()){
                WarningLabel.setText("Amount is Empty");
                WarningLabel.setVisible(true);
            } else if (filePathField.getText().isEmpty()) {
                WarningLabel.setText("File Path is Empty");
                WarningLabel.setVisible(true);
            } else if (CustomerCombobox.getValue()==null) {
                WarningLabel.setText("Customer not chosen");
                WarningLabel.setVisible(true);
            }
            else{
                WarningLabel.setText("Some Fields are Empty");
                WarningLabel.setVisible(true);
            }
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

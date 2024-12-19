package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignupController {


    public ComboBox EmployeeRoleComboBox;
    public TextField EmployeeUsernameField;

    public PasswordField EmployeePasswordFeild;
    public TextField EmployeePhoneNumberField;
    //Declaration of Elements in Forms
    @FXML
    private Label AddStudentLabel;
    @FXML
    private Label AddTeacherLabel;
    @FXML
    private Label AddAdminLabel1;
    @FXML
    private TextField EmployeeNameField;
    @FXML
    private TextField EmployeeEmailField;
    @FXML
    private PasswordField EmployeePasswordField;


    @FXML
    public void initialize(){
        populateEmployeeRoleCombobox();
    }
    void populateEmployeeRoleCombobox(){
        DatabaseConnection con = new DatabaseConnection();
        ObservableList<String> roleList = FXCollections.observableArrayList();

        try (Connection connection = con.GetConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM roles");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String roleName = resultSet.getString("Name");
                roleList.add(roleName);
            }

            EmployeeRoleComboBox.setItems(roleList);

        } catch (SQLException e) {
            showAlert("Error", "Failed to load roles: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //On Signup Click
    @FXML
    public void handleSignupButtonClick(ActionEvent actionEvent) {

        DatabaseConnection con =new DatabaseConnection();
        System.out.println(EmployeeRoleComboBox.getValue());
            //Check for any empty textbox or Combo boxes
        if (!EmployeeNameField.getText().isEmpty() && !EmployeeEmailField.getText().isEmpty() && !EmployeeUsernameField.getText().isEmpty() && !EmployeePasswordField.getText().isEmpty()) {
            //Getting Role from Combo box as It gives Role Names
            LoadDatabase Load=new LoadDatabase();
            Load.LoadRoles();
            Load.RolesList.tempNode=Load.RolesList.GetHead();


            while(Load.RolesList.tempNode != null) {
                if (Load.RolesList.tempNode.data.GetName().equals(EmployeeRoleComboBox.getValue())) {
                    System.out.println("From DB: "+Load.RolesList.tempNode.data.GetName());
                    break;
                }
                else{
                    Load.RolesList.tempNode=Load.RolesList.tempNode.next;
                }
            }

            //Adding New Employee to Database
            int RoleID= Load.RolesList.tempNode != null ? Load.RolesList.tempNode.data.GetID() : 1;
                try(
                        Connection connection= con.GetConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (Name, Email,Role, PhoneNumber,  Username, Password) VALUES (?, ?, ?, ?,?,?)" );
                )
                {
                    preparedStatement.setString(1, EmployeeNameField.getText());
                    preparedStatement.setString(2, EmployeeEmailField.getText());
                    preparedStatement.setInt(3, RoleID);
                    preparedStatement.setString(4, EmployeePhoneNumberField.getText());
                    preparedStatement.setString(5, EmployeeUsernameField.getText());
                    preparedStatement.setString(6, EmployeePasswordField.getText());
                    preparedStatement.executeUpdate();
                    showAlert("Success", "Successfully Added New Employee");

                } catch (SQLException e) {
                    showAlert("Failed To Add new Employee", e.getMessage());
                    throw new RuntimeException(e);
                }
        } else {
            //if(EmployeeNameField.getText().isEmpty()){TeacherNameWarning.setText("Name is Empty");TeacherNameWarning.setVisible(true);}
            //else if(TeacherEmailTextbox.getText().isEmpty()){TeacherEmailTextbox.setText("Email is Empty");TeacherEmailWarning.setVisible(true);}
            //else if(TeacherUsernameTextbox.getText().isEmpty()){TeacherUsernameTextbox.setText("Username is Empty");TeacherUsernameWarning.setVisible(true);}
            //else if(TeacherPasswordPasswordbox.getText().isEmpty()){TeacherPasswordPasswordbox.setText("Password is Empty");TeacherPasswordWarning.setVisible(true);}
            //else if(TeacherRePasswordPasswordbox.getText().isEmpty()){TeacherRePasswordPasswordbox.setText("Repassword is Empty");TeacherRePasswordWarning.setVisible(true);}
        }
    }

    //On Dashboard Click back to Dashboard
   // public void DashboardButtonOnClick(ActionEvent actionEvent) {
    //    try {
    //        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildpath360/AdminDashboard.fxml"));
    //        Parent dashboardParent = loader.load();
           // AdminDashboardController adminController = loader.getController();

//            Scene dashboardScene = new Scene(dashboardParent);
//            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//            window.setScene(dashboardScene);
//            window.show();
//        } catch (IOException e) {
//            e.printStackTrace();
   //     }
 //   }

    //Alert Function
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

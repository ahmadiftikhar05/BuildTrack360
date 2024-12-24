package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Label WarningPassword;

    @FXML
    private Label WarningUsername;

    @FXML
    private Label WarningRole;

    @FXML
    private Label WarningIncorrect;

    @FXML
    private TextField UsernameTextbox;

    @FXML
    private TextField PasswordTextbox;

    @FXML
    private Button SigninButton;

    DatabaseConnection con =new DatabaseConnection();

    @FXML
    void SigninButtonOnClick(ActionEvent event) throws IOException {

        String Username = UsernameTextbox.getText();
        String Password = PasswordTextbox.getText();
        if(Username.isEmpty() || Password.isEmpty() ) {
            if(Username.isEmpty()) {
                WarningUsername.setVisible(true);
                WarningUsername.setText("Username is Empty");
            }
            if(Password.isEmpty()){
                WarningPassword.setVisible(true);
                WarningPassword.setText("Password is Empty");
            }
        }
        else{
            WarningUsername.setVisible(false);
            WarningPassword.setVisible(false);
            WarningRole.setVisible(false);

                if(ValidateData(Username, Password)) {
                    System.out.println("Login Sucess");

                    try {
                        // Load the FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/BuildPath.fxml"));
                        Parent root = loader.load(); // Make sure BuildPath.fxml exists

                        // Get the current stage
                        Stage stage = new Stage();
                        Stage currentStage = (Stage) SigninButton.getScene().getWindow();
                        currentStage.close();
                        // Set the new scene
                        stage.setTitle("Dashboard");
                        stage.setScene(new Scene(root));

                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Failed to load the FXML file.");
                    }
                }
                else {
                    WarningIncorrect.setVisible(true);
                    WarningIncorrect.setText("Incorrect Password or Username");
                }
        }
    }
    boolean ValidateData(String Username, String Password) {

            try (
                    Connection connection = con.GetConnection();
                    PreparedStatement preparedStatement =
                            connection.prepareStatement("SELECT * FROM users WHERE Username = ? AND Password = ?")
            ) {
                preparedStatement.setString(1, Username);
                preparedStatement.setString(2, Password);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadDashboard(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent dashboardParent = loader.load();
            if (fxmlPath.contains("/com/example/BuildTrack360/BuildPath.fxml")) {
                BuildPathController BuildPath = loader.getController();
     }

            Scene dashboardScene = new Scene(dashboardParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(dashboardScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

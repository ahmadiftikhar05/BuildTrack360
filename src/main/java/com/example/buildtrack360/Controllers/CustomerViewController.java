package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Backend.Customers;
import com.example.buildtrack360.Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerViewController {
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField priorityField;

    @FXML private TableView<Customers> customersTable;
    @FXML private TableColumn<Customers, Integer> idColumn;
    @FXML private TableColumn<Customers, String> nameColumn;
    @FXML private TableColumn<Customers, String> phoneColumn;
    @FXML private TableColumn<Customers, String> emailColumn;
    @FXML private TableColumn<Customers, Integer> priorityColumn;

    private DatabaseConnection databaseConnection;
    private ObservableList<Customers> customersList = FXCollections.observableArrayList();

    public void initialize() {
        databaseConnection = new DatabaseConnection();
        setupTableColumns();
        loadCustomersData();
        customersTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void setupTableColumns() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("Priority"));

        // Add debug prints to verify the data
        customersTable.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Customers> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Customers customer : c.getAddedSubList()) {
                        System.out.println("Customer added: " +
                                "ID=" + customer.getID() +
                                ", Name=" + customer.getName() +
                                ", Phone=" + customer.getPhoneNumber() +
                                ", Email=" + customer.getEmail() +
                                ", Priority=" + customer.getPriority());
                    }
                }
            }
        });
    }
    private void loadCustomersData() {
        customersList.clear();
        try (Connection conn = databaseConnection.GetConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers ");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customersList.add(new Customers(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getInt("Priority")
                ));
            }

            customersTable.setItems(customersList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading customers: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddCustomer() {
        if (!validateInputs()) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            int priority = Integer.parseInt(priorityField.getText().trim());

            Customers newCustomer = new Customers(name, phone, email, priority);
            newCustomer.AddCustomer();

            clearFields();
            loadCustomersData();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Priority must be a valid number");
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add customer: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCustomer() {
        Customers selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a customer to delete");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Customer");
        confirmDialog.setContentText("Are you sure you want to delete customer: " + selectedCustomer.getName() + "?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Customers customer = new Customers();
                customer.DeleteCustomer(selectedCustomer.getName());

                loadCustomersData();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully");
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error returning to dashboard: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String priority = priorityField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || priority.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required");
            return false;
        }

        if (name.length() > 45) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Name must be less than 45 characters");
            return false;
        }

        if (phone.length() > 45) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Phone number must be less than 45 characters");
            return false;
        }

        if (email.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Email must be less than 100 characters");
            return false;
        }

        try {
            int priorityNum = Integer.parseInt(priority);
            if (priorityNum < 1) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Priority must be a positive number");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Priority must be a valid number");
            return false;
        }

        return true;
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        priorityField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
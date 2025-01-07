package com.example.buildtrack360.Backend;

import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.*;

public class Modules {
    private int ID;
    private String Name;
    private int Structure;
    private int Complete;

    // Default constructor
    public Modules() {
    }

    // Full constructor
    public Modules(int ID, String name, int structure, int complete) {
        this.ID = ID;
        this.Name = name;
        this.Structure = structure;
        this.Complete = complete;
    }

    // Database operations
    public void InsertModule(int structure, String name) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "INSERT INTO modules (Structure, Name, Complete) VALUES (?, ?, 0)";

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, structure);
            preparedStatement.setString(2, name);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.ID = generatedKeys.getInt(1);
                        this.Name = name;
                        this.Structure = structure;
                        this.Complete = 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting module: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void UpdateModule(String name) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "UPDATE modules SET Name = ? WHERE ID = ?";

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, this.ID);

            preparedStatement.executeUpdate();
            this.Name = name;

        } catch (SQLException e) {
            System.err.println("Error updating module: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void DeleteModule() {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "DELETE FROM modules WHERE ID = ?";

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setInt(1, this.ID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting module: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void UpdateComplete(int complete) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "UPDATE modules SET Complete = ? WHERE ID = ?";

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setInt(1, complete);
            preparedStatement.setInt(2, this.ID);

            preparedStatement.executeUpdate();
            this.Complete = complete;

        } catch (SQLException e) {
            System.err.println("Error updating module completion: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Getters
    public int GetID() {
        return ID;
    }

    public String GetName() {
        return Name;
    }

    public int GetStructure() {
        return Structure;
    }

    public int GetComplete() {
        return Complete;
    }

    // Setters
    public void SetID(int ID) {
        this.ID = ID;
    }

    public void SetName(String name) {
        this.Name = name;
    }

    public void SetStructure(int structure) {
        this.Structure = structure;
    }

    public void SetComplete(int complete) {
        this.Complete = complete;
    }
}

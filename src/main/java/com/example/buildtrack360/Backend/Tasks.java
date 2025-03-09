package com.example.buildtrack360.Backend;

import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Tasks {
    private int ID;
    private int ModuleID;
    private int UserID;
    private String Name;
    private String Description;
    public Boolean Complete;

    // Default constructor
    public Tasks() {
    }

    // Constructor for Complete Load from Database
    public Tasks(int propID, String propName, String propDescription, int propModuleID, int propUserID) {
        ID = propID;
        Name = propName;
        Description = propDescription;
        ModuleID = propModuleID;
        UserID = propUserID;
        Complete = false;  // Initialize Complete field
    }

    // Constructor for Controller Frontend as ID is auto increment
    public Tasks(String propName, String propDescription, int propModuleID, int propUserID) {
        Name = propName;
        Description = propDescription;
        ModuleID = propModuleID;
        UserID = propUserID;
        Complete = false;  // Initialize Complete field
    }

    // Full constructor including Complete field
    public Tasks(int ID, String name, String description, int module, boolean complete) {
        this.ID = ID;
        this.Name = name;
        this.Description = description;
        this.ModuleID = module;
        this.Complete = complete;
    }

    public Tasks(int taskID, String taskName, String description, int moduleID, int userID, boolean complete) {
        this.ID = ID;
        this.Name = taskName;
        this.Description = description;
        this.ModuleID = moduleID;
        this.Complete = complete;
        this.UserID=userID;
    }

    // Getters with public access modifiers to match PropertyValueFactory usage
    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public int getModuleID() {
        return ModuleID;
    }

    public int getUserID() {
        return UserID;
    }

    public Boolean getComplete() {
        return Complete;
    }

    // Setters for properties that might need to be updated
    public void setName(String name) {
        this.Name = name;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setComplete(Boolean complete) {
        this.Complete = complete;
    }

    public boolean AddTasks() {
        boolean TaskAddSuccessFlag = false;
        DatabaseConnection connection = new DatabaseConnection();

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "INSERT INTO tasks (Name, Description, Module, UserID, Complete) VALUES (?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, Name);
            preparedStatement.setString(2, Description);
            preparedStatement.setInt(3, ModuleID);
            preparedStatement.setInt(4, UserID);
            preparedStatement.setBoolean(5, Complete != null ? Complete : false);

            int affected = preparedStatement.executeUpdate();
            if (affected > 0) {
                TaskAddSuccessFlag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return TaskAddSuccessFlag;
    }
}
package com.example.buildtrack360.Backend;

import com.example.buildtrack360.DSA.Queue;
import com.example.buildtrack360.Database.DatabaseConnection;
import com.example.buildtrack360.Database.LoadDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectStructure {
    private int ID;
    private int ProjectID;
    private int TeamLeadID;
    private int TeamID;
    public Queue<String> ModulesList=new Queue<String>(20);
    public ProjectStructure(int PropID, int PropProjectID,int PropTeamLeadID,int PropTeamID){
        ID=PropID;
        ProjectID=PropProjectID;
        TeamLeadID=PropTeamLeadID;
        TeamID=PropTeamID;
    }

    public ProjectStructure( int PropProjectID,int PropTeamLeadID,int PropTeamID){
        ProjectID=PropProjectID;
        TeamLeadID=PropTeamLeadID;
        TeamID=PropTeamID;
    }
    public void AddProjectStructure() {
        DatabaseConnection connection = new DatabaseConnection();

        // Step 1: Insert the project structure into the database
        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "INSERT INTO projectstructure (ProjectID, TeamLeadID, TeamID) VALUES(?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, ProjectID);
            preparedStatement.setInt(2, TeamLeadID);
            preparedStatement.setInt(3, TeamID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Step 2: Retrieve the generated structure ID
        int structureID = 0;
        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "SELECT ID FROM projectstructure WHERE TeamLeadID=?")) {
            preparedStatement.setInt(1, TeamLeadID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                structureID = resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Step 3: Insert all modules from the queue into the modules table
        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "INSERT INTO modules (Name, Structure) VALUES(?, ?)")) {

            while (!ModulesList.isEmpty()) { // Process the queue until it's empty
                String moduleName = ModulesList.dequeue();
                if (moduleName == null || moduleName.isEmpty()) {
                    System.err.println("Error: Module name is null or empty. Skipping insertion.");
                    continue; // Skip insertion if the module name is invalid
                }

                preparedStatement.setString(1, moduleName);
                preparedStatement.setInt(2, structureID);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void DeleteProjectStructure(){}
    public int GetID(){return ID;}
    public int GetProjectID(){return ProjectID;}
    public int GetTeamLeadID(){return TeamLeadID;}
    public int GetTeamID(){return TeamID;}

}

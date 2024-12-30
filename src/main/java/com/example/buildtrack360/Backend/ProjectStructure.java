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
    public void AddProjectStructure(){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con= connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("INSERT INTO projectstructure (ProjectID, TeamLeadID, TeamID) VALUES(?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setInt(1,ProjectID);
            preparedStatement.setInt(2, TeamLeadID);
            preparedStatement.setInt(3,TeamID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        int StructureID=0;
        try(Connection con= connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("Select ID FROM projectstructure where (TeamLeadID) VALUES(?)");) {
            preparedStatement.setInt(1,TeamLeadID);

            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                StructureID=resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(Connection con=connection.GetConnection();
        PreparedStatement preparedStatement= con.prepareStatement("INSERT INTO modules (Name, Structure) VALUES(?,?)");){
            preparedStatement.setString(1,ModulesList.dequeue());
            preparedStatement.setInt(2,StructureID);
            preparedStatement.executeUpdate();
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

package com.example.buildtrack360.Backend;

import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {
    private int ID, Role;
    private String Name, Email,PhoneNumber,Username;
    public Users(int PropID, String PropName,int PropRole,String PropEmail,String PropPhoneNumber,String PropUsername){
        ID=PropID;
        Name=PropName;
        Role=PropRole;
        Email=PropEmail;
        PhoneNumber=PropPhoneNumber;
        Username=PropUsername;
    }
    public Users(){}
    public int GetID(){return ID;}
    public String GetName(){return Name;}
    public int GetRoleID(){return Role;}
    public String GetEmail(){return Email;}
    public String GetPhoneNumber(){return PhoneNumber;}
    public String GetUsername(){return Username;}
    public String GetRoleName(){
        DatabaseConnection con=new DatabaseConnection();
        String RoleName=null;
        try(
                Connection connection=con.GetConnection();
                PreparedStatement preparedStatement= connection.prepareStatement("Select * From roles WHERE ID=?");){
            preparedStatement.setInt(1, Role);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                RoleName= resultSet.getString("Name");
            }
        }
        catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        return RoleName;
    }

    public int GetRoleID(String RoleName) {
        DatabaseConnection con = new DatabaseConnection();
        int RoleID = 0;  // Default value when no role is found.

        try (Connection connection = con.GetConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM roles WHERE Name = ?")) {

            // Set the role name as a parameter in the query
            preparedStatement.setString(1, RoleName);

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a result is returned
            if (resultSet.next()) {
                // Get the Role ID from the result set
                RoleID = resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            // Log or throw an exception
            throw new RuntimeException("Error fetching role ID", e);
        }

        return RoleID;  // Return 0 if no result is found
    }

}

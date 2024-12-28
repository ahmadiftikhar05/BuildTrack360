package com.example.buildtrack360.Backend.UserRoles;

import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Roles {
    private int ID;
    private String Name;
    public void SetName(String NameDatabase){
        Name=NameDatabase;
    }
    public void SetID(int IDDatabase){
        ID=IDDatabase;
    }
    public int GetID(){
        return ID;
    }
    public String GetName(){
        return Name;
    }
    public void AddRoles(String Name){
        DatabaseConnection con=new DatabaseConnection();
        try(

                Connection connection=con.GetConnection();
                PreparedStatement preparedStatement= connection.prepareStatement("INSERT INTO roles(Name) VALUES(?)"))
        {
            preparedStatement.setString(1,Name);
            preparedStatement.executeUpdate();
            System.out.println("Successfully Added the Role: "+Name);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

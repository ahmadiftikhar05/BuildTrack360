package com.example.buildtrack360.Test;

import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolesInput {
    public void InputRoles(String Name){
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

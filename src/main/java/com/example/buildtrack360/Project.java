package com.example.buildtrack360;

import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Project {
    private int ID;
    private String Name;
    private int Customer;
    private int Amount;
    private String Agreement;
    public void AddProject(String PropName,int PropCustomer, int PropAmount,String PropAgreement){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con=connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("INSERT INTO projects(Name, Customer, Amount, Agreement) VALUES(?,?,?,?)")){
            preparedStatement.setString(1,PropName);
            preparedStatement.setInt(2,PropCustomer);
            preparedStatement.setInt(3,PropAmount);
            preparedStatement.setString(4,PropAgreement);
            preparedStatement.executeUpdate();
            System.out.println("Customer Added Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void DeleteProject(String DeleteName){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con=connection.GetConnection();
        PreparedStatement preparedStatement=con.prepareStatement("DELETE FROM projects WHERE(Name=?)");
        ) {
            preparedStatement.setString(1,DeleteName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

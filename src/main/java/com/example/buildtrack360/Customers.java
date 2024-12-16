package com.example.buildtrack360;

import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Customers {
    private int ID;
    private String Name;
    private String Email;
    private String PhoneNumber;
    private int Priority;
    public Customers(int PropID,String PropName, String PropPhoneNumber,String PropEmail, int PropPriority){
        ID=PropID;
        Name=PropName;
        PhoneNumber=PropPhoneNumber;
        Email=PropEmail;
        Priority=PropPriority;
    }
    public Customers(String PropName, String PropPhoneNumber,String PropEmail, int PropPriority){
        Name=PropName;
        PhoneNumber=PropPhoneNumber;
        Email=PropEmail;
        Priority=PropPriority;
    }
    public Customers(){}
    public void AddCustomer(String PropName, String PropPhoneNumber,String PropEmail, int PropPriority){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con=connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("INSERT INTO customers(Name, PhoneNumber, Email, Priority) VALUES(?,?,?,?)")){
            preparedStatement.setString(1,PropName);
            preparedStatement.setString(2,PropPhoneNumber);
            preparedStatement.setString(3,PropEmail);
            preparedStatement.setInt(4,PropPriority);
            preparedStatement.executeUpdate();
            System.out.println("Customer Added Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void DeleteCustomer(String DeleteName){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con=connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("DELETE FROM Customers WHERE(Name=?)");
        ) {
            preparedStatement.setString(1,DeleteName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

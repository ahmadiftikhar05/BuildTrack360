package com.example.buildtrack360.Database;

import com.example.buildtrack360.Customers;
import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.UserRoles.Roles;

import java.sql.*;

public class LoadDatabase {
    public LinkedList<Roles> RolesList=new LinkedList<Roles>();
    public LinkedList<Customers> CustomersList=new LinkedList<Customers>();
    public void LoadRoles() {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "SELECT ID, Name FROM roles";  // Explicitly specify columns
        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Check if there are results
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No roles found in the database.");
                return;
            }

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String Name = resultSet.getString("Name");

                Roles role = new Roles();
                role.SetID(ID);
                role.SetName(Name);

                // Insert role into the list
                RolesList.InsertData(role);
            }
        } catch (SQLException e) {
            // Log or print more detailed error message
            System.err.println("Error loading roles from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void LoadCustomers(){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con=connection.GetConnection();
        PreparedStatement preparedStatement=con.prepareStatement("SELECT * FROM customers")){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int ID=resultSet.getInt("ID");
                String Name=resultSet.getString("Name");
                String PhoneNumber=resultSet.getString("PhoneNumber");
                String Email=resultSet.getString("Email");
                int Priority=resultSet.getInt("Priority");
                Customers customer=new Customers(ID,Name,PhoneNumber,Email,Priority);
                CustomersList.InsertData(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

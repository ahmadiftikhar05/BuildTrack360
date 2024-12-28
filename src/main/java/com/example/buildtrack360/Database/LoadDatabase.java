package com.example.buildtrack360.Database;

import com.example.buildtrack360.Backend.Customers;
import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.Backend.Project.Project;
import com.example.buildtrack360.Backend.UserRoles.Roles;
import com.example.buildtrack360.Backend.Stage;
import com.example.buildtrack360.Backend.Users;


import java.sql.*;

public class LoadDatabase {
    public LinkedList<Roles> RolesList=new LinkedList<Roles>();
    public LinkedList<Customers> CustomersList=new LinkedList<Customers>();
    public LinkedList<Project> ProjectsList=new LinkedList<Project>();
    public LinkedList<Stage> StageList=new LinkedList<Stage>();
    public LinkedList<Users> UsersList=new LinkedList<Users>();

    public void LoadRoles() {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "SELECT ID, Name FROM roles";
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
    public void LoadProject(){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con= connection.GetConnection();
        PreparedStatement preparedStatement=con.prepareStatement("SELECT * FROM projects");){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int ID=resultSet.getInt("ID");
                String Name=resultSet.getString("Name");
                int Customer=resultSet.getInt("Customer");
                int Amount=resultSet.getInt("Amount");
                String Agreement=resultSet.getString("Agreement");
                int Stages=resultSet.getInt("Stages");
                int ProjectManagerID=resultSet.getInt("ProjectManager");
                Project project=new Project(ID,Name,Customer,Amount,Agreement,Stages);
                if(ProjectManagerID!=0) {
                    project.setProjectManagerID(ProjectManagerID);
                }
                ProjectsList.InsertData(project);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void LoadStages(){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con= connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("SELECT * FROM buildpathstages");){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int ID=resultSet.getInt("ID");
                String Name=resultSet.getString("Name");

                Stage stage=new Stage(ID,Name);
                StageList.InsertData(stage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void LoadUsers(String Role){
        Users user=new Users();
        int RoleID=user.GetRoleID(Role);
        DatabaseConnection connection =new DatabaseConnection();
        try(Connection con= connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("SELECT * FROM users WHERE Role=?");){
            preparedStatement.setInt(1, RoleID);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int ID=resultSet.getInt("ID");
                String Name=resultSet.getString("Name");
                String Email=resultSet.getString("Email");
                String PhoneNumber=resultSet.getString("PhoneNumber");
                String Username=resultSet.getString("Username");
                Users users=new Users(ID,Name,RoleID,Email,PhoneNumber,Username);
                UsersList.InsertData(users);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

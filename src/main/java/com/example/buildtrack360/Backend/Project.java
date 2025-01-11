package com.example.buildtrack360.Backend;

import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Project {
    private int ID;
    private String Name;
    private int Customer;
    private int Amount;
    private String Agreement;
    private int Stages;
    private int CompletePercent=0;
    private int ProjectManagerID=0;

    public int getID(){return ID;}
    public String getName(){return Name;}
    public int getCustomerID(){return Customer;}
    public int getAmount(){return Amount;}
    public String getAgreement(){return Agreement;}
    public int getStage(){return Stages;}
    public int getCompletePercent(){return  CompletePercent;}
    public int getProjectManagerID(){return ProjectManagerID;}
    public void setProjectManagerID(int PropProjectManagerID){ProjectManagerID=PropProjectManagerID;}
    public Project(int PropID, String PropName, int PropCustomer, int PropAmount, String PropAgreement,int PropStages){
        ID=PropID;
        Name=PropName;
        Customer=PropCustomer;
        Amount=PropAmount;
        Agreement=PropAgreement;
        Stages=PropStages;
    }
    public Project(String PropName, int PropCustomer, int PropAmount, String PropAgreement,int  PropStages){
        Name=PropName;
        Customer=PropCustomer;
        Amount=PropAmount;
        Agreement=PropAgreement;
        Stages=PropStages;
    }
    public Project(String PropName, int PropCustomer, int PropAmount, String PropAgreement,int  PropStages,int PropCompeltePercent){
        Name=PropName;
        Customer=PropCustomer;
        Amount=PropAmount;
        Agreement=PropAgreement;
        Stages=PropStages;
        CompletePercent=PropCompeltePercent;
    }
    public boolean AddProject(){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con=connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("INSERT INTO projects(Name, Customer, Amount, Agreement,Stages) VALUES(?,?,?,?,?)")){
            preparedStatement.setString(1,Name);
            preparedStatement.setInt(2,Customer);
            preparedStatement.setInt(3,Amount);
            preparedStatement.setString(4,Agreement);
            preparedStatement.setInt(5,Stages);
            preparedStatement.executeUpdate();
            System.out.println("Customer Added Successfully");
            return true;
        } catch (SQLException e) {
            return false;
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
    public String getStageName(){
        String stageName="";
        DatabaseConnection con=new DatabaseConnection();
        try(
                Connection connection=con.GetConnection();
                PreparedStatement preparedStatement= connection.prepareStatement("Select Name From buildpathstages Where ID=?");
                ) {
            preparedStatement.setInt(1, Stages);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    stageName = resultSet.getString("Name");
                }
            }
            return stageName;
        } catch (SQLException e) {
            System.out.println("Error in Project.java in getting stage data");
        }
        System.out.println("Stage Name From Project.java: "+stageName);
        return stageName;
    }

    public String getCustomerName(){
        String CustomerName="";
        DatabaseConnection con=new DatabaseConnection();
        try(
                Connection connection=con.GetConnection();
                PreparedStatement preparedStatement= connection.prepareStatement("Select Name From customers Where ID=?");
        ) {
            preparedStatement.setInt(1, Customer);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CustomerName = resultSet.getString("Name");
                }
            }
            return CustomerName;
        } catch (SQLException e) {
            System.out.println("Error in Project.java in getting customer data");
        }
        System.out.println("Stage Name From Project.java: "+CustomerName);
        return CustomerName;
    }

    public String getCustomerPriority(){
        int CustomerPriority=0;
        DatabaseConnection con=new DatabaseConnection();
        try(
                Connection connection=con.GetConnection();
                PreparedStatement preparedStatement= connection.prepareStatement("Select Priority From customers Where ID=?");
        ) {
            preparedStatement.setInt(1, Customer);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CustomerPriority = resultSet.getInt("Priority");
                }
            }
            if(CustomerPriority==1){
                return "Medium";
            }
            else if(CustomerPriority==2){
                return "High";
            } else if (CustomerPriority==3) {
                return "Very High";
            }
            else {
                System.out.println("Didn't got priority");
            }

        } catch (SQLException e) {
            System.out.println("Error in Project.java in getting customer data");
        }
        System.out.println("Stage Name From Project.java: "+CustomerPriority);
        System.out.println("Didn't got priority");
        return "Didn't got priority";
    }
    public String GetProjectManagerName(){
        DatabaseConnection con=new DatabaseConnection();
        String RoleName=null;
        try(
                Connection connection=con.GetConnection();
                PreparedStatement preparedStatement= connection.prepareStatement("Select Name From users WHERE ID=?");){
            preparedStatement.setInt(1, ProjectManagerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                RoleName= resultSet.getString("Name");
            }
        }
        catch (
                SQLException e) {
            throw new RuntimeException("Error fetching project manager name", e);
        }
        return RoleName;
    }
}

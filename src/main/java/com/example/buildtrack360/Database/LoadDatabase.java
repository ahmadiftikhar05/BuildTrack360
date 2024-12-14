package com.example.buildtrack360.Database;

import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.UserRoles.Roles;

import java.sql.*;

public class LoadDatabase {
    public LinkedList<Roles> list=new LinkedList<Roles>();
    public void LoadRoles() {
        DatabaseConnection connection = new DatabaseConnection();
        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT* FROM role");) {
            ResultSet resultset=preparedStatement.executeQuery();
            while(resultset.next()){
                int ID=resultset.getInt("ID");
                String Name=resultset.getString("Name");
                Roles role=new Roles();
                role.SetID(ID);
                role.SetName(Name);
                list.InsertData(role);
            }
            resultset.close();
            con.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

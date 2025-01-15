package com.example.buildtrack360.Database;

import com.example.buildtrack360.Backend.*;
import com.example.buildtrack360.Controllers.ProjectManager.TeamsController;
import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.Backend.Project;
import com.example.buildtrack360.Backend.Roles;


import java.sql.*;

public class LoadDatabase {
    public LinkedList<Roles> RolesList=new LinkedList<Roles>();
    public LinkedList<Customers> CustomersList=new LinkedList<Customers>();
    public LinkedList<Project> ProjectsList=new LinkedList<Project>();
    public LinkedList<Stage> StageList=new LinkedList<Stage>();
    public LinkedList<Users> UsersList=new LinkedList<Users>();
    public LinkedList<ProjectStructure> ProjectStructureList=new LinkedList<ProjectStructure>();
    public LinkedList<Teams> TeamList=new LinkedList<Teams>();
    public LinkedList<Members> MemberList=new LinkedList<Members>();
    public LinkedList<Tasks> TasksList = new LinkedList<Tasks>();
    public LinkedList<Tasks> CompletedTasksList = new LinkedList<Tasks>();
    public LinkedList<Modules> ModulesList = new LinkedList<Modules>();

    public int taskcount=0;
    public int completetaskcount=0;

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
    public void LoadProjectStructure(){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con= connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("SELECT * FROM projectstructure");){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int ID=resultSet.getInt("ID");
                int ProjectID=resultSet.getInt("ProjectID");
                int TeamLeadID=resultSet.getInt("TeamLead");
                int TeamID=resultSet.getInt("TeamID");

                ProjectStructure projectStructure=new ProjectStructure(ID,ProjectID,TeamLeadID,TeamID);
                ProjectStructureList.InsertData(projectStructure);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void LoadTeams(){
        DatabaseConnection connection=new DatabaseConnection();
        try(Connection con= connection.GetConnection();
            PreparedStatement preparedStatement=con.prepareStatement("SELECT * FROM teams");){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int ID=resultSet.getInt("ID");
                String Name=resultSet.getString("Name");
                int TeamLeadID=resultSet.getInt("TeamLead");

                Teams team=new Teams(ID,Name,TeamLeadID);

                try(PreparedStatement preparedStatement1=con.prepareStatement("Select * FROM teammembers");){
                    ResultSet resultSet1=preparedStatement1.executeQuery();
                    LoadUsers("Developer");
                    UsersList.tempNode=UsersList.GetHead();
                    while(UsersList.tempNode!=null) {
                        while (resultSet1.next()) {
                            int MemberUserID = resultSet1.getInt("UserID");
                            int MemberTeam = resultSet1.getInt("Team");
                            if(UsersList.tempNode.data.GetID()==MemberUserID){
                                team.TeamMembersList.InsertData(UsersList.tempNode.data);
                            }
                        }
                        UsersList.tempNode=UsersList.tempNode.next;
                    }
                }
                TeamList.InsertData(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void LoadMembers(int TeamID) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "SELECT * FROM teammembers where Team=?";
        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, TeamID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                int UserID = resultSet.getInt("UserID");
                Members member = new Members(ID, UserID, TeamID);  // Use 3-parameter constructor
                MemberList.InsertData(member);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    // Load all modules for a project
    public void LoadModules(int projectId) {
        DatabaseConnection connection = new DatabaseConnection();
        String query = "SELECT m.* FROM modules m " +
                "JOIN projectstructure ps ON m.Structure = ps.ID " +
                "WHERE ps.ProjectID = ?";

        // Clear existing list before loading new data
        ModulesList = new LinkedList<>();

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String Name = resultSet.getString("Name");
                int Structure = resultSet.getInt("Structure");
                int Complete = resultSet.getInt("Complete");

                Modules module = new Modules(ID, Name, Structure, Complete);
                ModulesList.InsertData(module);
            }
        } catch (SQLException e) {
            System.err.println("Error loading modules: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Load tasks by project name
    public void LoadTasksByProject(String projectName) {
        TasksList=new LinkedList<>();
        taskcount = 0;
        DatabaseConnection connection = new DatabaseConnection();
        String query = "SELECT t.* FROM tasks t " +
                "INNER JOIN modules m ON t.Module = m.ID " +
                "INNER JOIN projectstructure ps ON m.Structure = ps.ID " +
                "INNER JOIN projects p ON ps.ProjectID = p.ID " +
                "WHERE p.Name = ?";

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, projectName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String Name = resultSet.getString("Name");
                String Description = resultSet.getString("Description");
                int Module = resultSet.getInt("Module");
                int UserID = resultSet.getInt("UserID");
                boolean Complete = resultSet.getBoolean("Complete");

                Tasks task = new Tasks(ID, Name, Description, Module, UserID, Complete);
                TasksList.InsertData(task);
                taskcount++;
            }
        } catch (SQLException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Load completed tasks by project name
    public void LoadCompletedTasksByProject(String projectName) {
        CompletedTasksList=new LinkedList<>();
        completetaskcount = 0;
        DatabaseConnection connection = new DatabaseConnection();
        String query = "SELECT t.* FROM tasks t " +
                "INNER JOIN modules m ON t.Module = m.ID " +
                "INNER JOIN projectstructure ps ON m.Structure = ps.ID " +
                "INNER JOIN projects p ON ps.ProjectID = p.ID " +
                "WHERE p.Name = ? AND t.Complete = 1";

        try (Connection con = connection.GetConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, projectName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String Name = resultSet.getString("Name");
                String Description = resultSet.getString("Description");
                int Module = resultSet.getInt("Module");
                int UserID = resultSet.getInt("UserID");
                boolean Complete = resultSet.getBoolean("Complete");

                Tasks task = new Tasks(ID, Name, Description, Module, UserID, Complete);
                CompletedTasksList.InsertData(task);
                completetaskcount++;
            }
        } catch (SQLException e) {
            System.err.println("Error loading completed tasks: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
package com.example.buildtrack360.Backend;

import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Teams {
    private int ID,TeamLeadID,ProjectID;
    private String Name;
    public LinkedList<Users> TeamMembersList=new LinkedList<Users>();
    public int GetID(){return ID;}
    public String GetName(){return Name;}
    public int GetTeamLeadID(){return TeamLeadID;}
    public int GetProjectID(){return ProjectID;}

    //Constructor with ID
    public Teams(int propID, String propName, int propTeamLeadID,int propProjectID){
        ID=propID;
        Name=propName;
        TeamLeadID=propTeamLeadID;
        ProjectID=propProjectID;
    }

    //Constructor without ID
    public Teams(String propName, int propTeamLeadID,int propProjectID){
        Name=propName;
        TeamLeadID=propTeamLeadID;
        ProjectID=propProjectID;
    }

    //Constructor only with Name
    public Teams(String propName){Name=propName;}


}

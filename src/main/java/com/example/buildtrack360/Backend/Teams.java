package com.example.buildtrack360.Backend;

import com.example.buildtrack360.DSA.LinkedList;
import com.example.buildtrack360.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Teams {
    private int ID,TeamLeadID;
    private String Name;
    public LinkedList<Users> TeamMembersList=new LinkedList<Users>();
    public int GetID(){return ID;}
    public String GetName(){return Name;}
    public int GetTeamLeadID(){return TeamLeadID;}

    //Constructor with ID
    public Teams(int propID, String propName, int propTeamLeadID){
        ID=propID;
        Name=propName;
        TeamLeadID=propTeamLeadID;

    }

    //Constructor without ID
    public Teams(String propName, int propTeamLeadID){
        Name=propName;
        TeamLeadID=propTeamLeadID;

    }

    //Constructor only with Name
    public Teams(String propName){Name=propName;}


}

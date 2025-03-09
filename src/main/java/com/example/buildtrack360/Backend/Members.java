package com.example.buildtrack360.Backend;

public class Members {
    private int ID,UserID,TeamID;
    public Members(int propID,int porpUserID,int propTeamID){
        ID=propID;
        UserID=porpUserID;
        TeamID=propTeamID;
    }
    public Members(int propID,int porpUserID){
        ID=propID;
        UserID=porpUserID;
    }
    public int GetID(){return ID;}
    public int GetUserID(){return UserID;}
    public int GetTeamID(){return TeamID;}
}

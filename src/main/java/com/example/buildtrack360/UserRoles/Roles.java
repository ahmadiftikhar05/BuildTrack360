package com.example.buildtrack360.UserRoles;

public class Roles {
    private int ID;
    private String Name;
    public void SetName(String NameDatabase){
        Name=NameDatabase;
    }
    public void SetID(int IDDatabase){
        ID=IDDatabase;
    }
    public int GetID(){
        return ID;
    }
    public String GetName(){
        return Name;
    }
}

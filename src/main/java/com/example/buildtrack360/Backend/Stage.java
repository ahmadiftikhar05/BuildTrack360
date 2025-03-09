package com.example.buildtrack360.Backend;

public class Stage {
    private int ID;
    private String Name;
    public int getID(){
        return ID;
    }
    public String getName(){return Name;}
    public Stage(int propID, String propName){
        ID=propID;
        Name=propName;
    }

}

package com.example.buildtrack360;

public class project {
    private String name;
    private String projectManager;
    private String status;

    public project(String name, String projectManager,String status) {
        this.name = name;
        this.projectManager = projectManager;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public String getStatus() {
        return status;
    }
}

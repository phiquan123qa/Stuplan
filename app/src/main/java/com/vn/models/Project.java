package com.vn.models;

import java.util.ArrayList;

public class Project {
    private String id;
    private String title;
    private String icon;
    private ArrayList<String> issueId = new ArrayList<>();
    private String ownerID;
    private ArrayList<String> listUser = new ArrayList<>();
    public Project() {
    }
    public Project(String id, String title, String icon, ArrayList<String> issueId, String ownerID, ArrayList<String> listUser) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.issueId = issueId;
        this.ownerID = ownerID;
        this.listUser = listUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<String> getIssueId() {
        return issueId;
    }

    public void setIssueId(ArrayList<String> issueId) {
        this.issueId = issueId;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public ArrayList<String> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<String> listUser) {
        this.listUser = listUser;
    }
}

package com.vn.models;

import java.util.Date;

public class Issue {
    private String id;
    private String idProject;
    private String title;
    private String description;
    private String color;
    private String icon;
    private IssueStatusEnum status;
    private Date dateCreate;
    private String userID;
    public Issue() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Issue(String id, String idProject, String title, String description, String color, String icon, IssueStatusEnum status, Date dateCreate, String userID) {
        this.id = id;
        this.idProject = idProject;
        this.title = title;
        this.description = description;
        this.color = color;
        this.icon = icon;
        this.status = status;
        this.dateCreate = dateCreate;
        this.userID = userID;
    }

    public Issue(String idProject, String title, String description, String color, String icon, IssueStatusEnum status, Date dateCreate, String userID) {
        this.idProject = idProject;
        this.title = title;
        this.description = description;
        this.color = color;
        this.icon = icon;
        this.status = status;
        this.dateCreate = dateCreate;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public IssueStatusEnum getStatus() {
        return status;
    }

    public void setStatus(IssueStatusEnum status) {
        this.status = status;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
}

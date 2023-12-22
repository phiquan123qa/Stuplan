package com.vn.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Parcelable {
    private String id;
    private String title;
    private String icon;
    private ArrayList<String> issueId = new ArrayList<>();
    private String ownerID;
    private ArrayList<String> listUser = new ArrayList<>();
    public Project() {
    }

    public Project(String title, String icon, String ownerID) {
        this.title = title;
        this.icon = icon;
        this.ownerID = ownerID;
    }

    public Project(String id, String title, String icon, String ownerID) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.ownerID = ownerID;
    }

    public Project(String id, String title, String icon, ArrayList<String> issueId, String ownerID, ArrayList<String> listUser) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.issueId = issueId;
        this.ownerID = ownerID;
        this.listUser = listUser;
    }

    protected Project(Parcel in) {
        id = in.readString();
        title = in.readString();
        icon = in.readString();
        issueId = in.createStringArrayList();
        ownerID = in.readString();
        listUser = in.createStringArrayList();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

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

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(icon);
        dest.writeStringList(issueId);
        dest.writeString(ownerID);
        dest.writeStringList(listUser);
    }
}

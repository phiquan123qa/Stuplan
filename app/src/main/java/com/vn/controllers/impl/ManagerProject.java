package com.vn.controllers.impl;

import android.app.Activity;

import com.vn.models.Project;

public interface ManagerProject {
    public void addProject(Project project);
    public void editProject(Project project);
    public void deleteProject(Project project);
    public void findAllProject();
}

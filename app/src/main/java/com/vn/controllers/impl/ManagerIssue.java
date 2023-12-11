package com.vn.controllers.impl;

import com.vn.models.Issue;

public interface ManagerIssue {
    public void addIssue(Issue issue);

    public void editIssue(Issue issue);

    public void deleteIssue(Issue issue);

    public void findAllIssueInProject();
}


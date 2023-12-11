package com.vn.controllers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vn.controllers.impl.ManagerProject;
import com.vn.models.Project;

public class ManagerProjectImpl implements ManagerProject {
    @Override
    public void addProject(Project project) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String projectNameToCheck = project.getTitle();
        Query query = database.child("PROJECT").orderByChild("title").equalTo(projectNameToCheck);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("ProjectExists", "Project already exists with name: " + projectNameToCheck);
                } else {
                    String key = database
                            .child("PROJECT")
                            .push()
                            .getKey();
                    project.setId(key);
                    assert key != null;
                    database.child("PROJECT")
                            .child(key)
                            .setValue(project);
                    Log.d("NewProjectAdded", "New project added with ID: " + key);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error checking project existence: " + error.getMessage());
            }
        });
    }

    @Override
    public void editProject(Project project) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("PROJECT")
                .child(project.getId())
                .setValue(project);
    }

    @Override
    public void deleteProject(Project project) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("PROJECT")
                .child(project.getId()).removeValue();
    }

    @Override
    public void findAllProject() {

    }
}

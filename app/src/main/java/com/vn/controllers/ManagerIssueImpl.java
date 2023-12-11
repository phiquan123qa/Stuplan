package com.vn.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vn.controllers.impl.ManagerIssue;
import com.vn.models.Issue;

public class ManagerIssueImpl implements ManagerIssue {

    @Override
    public void addIssue(Issue issue) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String issueNameToCheck = issue.getTitle();
        Query query = database.child("ISSUE").orderByChild("title").equalTo(issueNameToCheck);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("IssueExists", "Issue already exists with name: " + issueNameToCheck);
                } else {
                    String key = database
                            .child("ISSUE")
                            .push()
                            .getKey();
                    issue.setId(key);
                    assert key != null;
                    database.child("ISSUE")
                            .child(key)
                            .setValue(issue);
                    Log.d("NewIssueAdded", "New issue added with ID: " + key);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error checking issue existence: " + error.getMessage());
            }
        });
    }

    @Override
    public void editIssue(Issue issue) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("ISSUE")
                .child(issue.getId())
                .setValue(issue);
    }

    @Override
    public void deleteIssue(Issue issue) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("ISSUE")
                .child(issue.getId()).removeValue();
    }

    @Override
    public void findAllIssueInProject() {

    }
}


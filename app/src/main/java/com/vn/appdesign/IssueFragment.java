package com.vn.appdesign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vn.models.Issue;
import com.vn.models.Project;
import com.vn.utility.AdapterIssuesList;
import com.vn.utility.AdapterProjectsList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PRJ_ID = "projectId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String projectID;
    RecyclerView recyclerView;
    DatabaseReference reference;
    AdapterIssuesList adapterIssuesList;
    List<Issue> listIssues;

    public IssueFragment() {
        // Required empty public constructor
    }

    public static IssueFragment newInstance(String param1, String param2, String projectID) {
        IssueFragment fragment = new IssueFragment();
        Bundle args = new Bundle();
        args.putString(PRJ_ID, projectID);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String projectId = arguments.getString("projectId");
            if (projectId != null) {
                //projectID = arguments.getString(PRJ_ID);
            }
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue, container, false);
        Bundle arguments = getArguments();
        String projectId = null;
        if (arguments != null) {
            projectId = arguments.getString("projectId");
            if (projectId != null) {
                //projectID = arguments.getString(PRJ_ID);
            }
        }
        recyclerView = view.findViewById(R.id.recycler_view_list_issue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listIssues = new ArrayList<>();
        adapterIssuesList = new AdapterIssuesList(getContext(), listIssues);
        recyclerView.setAdapter(adapterIssuesList);
        reference = FirebaseDatabase.getInstance().getReference("ISSUE");
        String finalProjectId = projectId;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Issue> newList = new ArrayList<>();
                String projectIdd = finalProjectId;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Issue issue = dataSnapshot.getValue(Issue.class);
                    if (issue != null && projectIdd.equals(issue.getIdProject())) {
                        newList.add(issue);
                    }
                }
                listIssues.clear();
                listIssues.addAll(newList);
                adapterIssuesList.notifyDataSetChanged();
                if (listIssues.isEmpty()) {
                    // If empty, show a TextView or handle it in your UI
                    showEmptyIssuesNotification(view, 1);
                } else {
                    // If not empty, hide the notification
                    showEmptyIssuesNotification(view, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void showEmptyIssuesNotification(View view, Integer check) {
        if(check == 1){
            RelativeLayout emptyIssuesNotification = view.findViewById(R.id.container_link_add_new_issue);
            emptyIssuesNotification.setVisibility(View.VISIBLE);
        }
        else if(check == 0){
            RelativeLayout emptyIssuesNotification = view.findViewById(R.id.container_link_add_new_issue);
            emptyIssuesNotification.setVisibility(View.GONE);
        }

    }
}
package com.vn.appdesign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vn.models.Issue;
import com.vn.models.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference referenceProject;
    DatabaseReference referenceIssue;
    List<Project> listProjects;
    List<Issue> listIssues;
    Integer projectCount;
    Integer issueCount;
    Integer successIssuesCount;
    TextView successRate;
    TextView completeIssue;
    TextView numberIssueTotal;
    TextView numberProjectTotal;

    public SummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        listProjects = new ArrayList<>();
        listIssues = new ArrayList<>();
        referenceProject = FirebaseDatabase.getInstance().getReference("PROJECT");
        referenceProject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectCount = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    projectCount++;
                }
                updateUIElementsProject(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referenceIssue = FirebaseDatabase.getInstance().getReference("ISSUE");
        referenceIssue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                issueCount = 0;
                successIssuesCount = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    issueCount++;
                    String status = dataSnapshot.child("status").getValue(String.class);
                    if(status != null && "DONE".equals(status)){
                        successIssuesCount++;
                    }
                }
                updateUIElementsIssue(view);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        int percent = successIssuesCount/issueCount*100;
//        successRate = view.findViewById(R.id.successRate);
//        successRate.setText(Integer.toString(percent) +"%");
//        completeIssue = view.findViewById(R.id.complete_issue_count);
//        completeIssue.setText(successIssuesCount.toString());
//        numberProjectTotal = view.findViewById(R.id.project_count);
//        numberProjectTotal.setText(projectCount.toString());
//        numberIssueTotal = view.findViewById(R.id.issue_count);
//        numberIssueTotal.setText(issueCount.toString());

        return view;
    }
    private void updateUIElementsIssue(View view) {
        int percent = (issueCount == 0) ? 0 : (successIssuesCount * 100) / issueCount;
        successRate = view.findViewById(R.id.successRate);
        successRate.setText(percent + "%");
        completeIssue = view.findViewById(R.id.complete_issue_count);
        completeIssue.setText(successIssuesCount.toString());
        numberIssueTotal = view.findViewById(R.id.issue_count);
        numberIssueTotal.setText(issueCount.toString());
    }
    private void updateUIElementsProject(View view) {
        numberProjectTotal = view.findViewById(R.id.project_count);
        numberProjectTotal.setText(projectCount.toString());
    }
}
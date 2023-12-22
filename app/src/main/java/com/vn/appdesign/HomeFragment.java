package com.vn.appdesign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.vn.models.Issue;
import com.vn.models.Project;
import com.vn.utility.AdapterIssuesList;
import com.vn.utility.AdapterProjectsListToHome;
import com.vn.utility.ShowDropDownMenuNoti;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerViewProject;
    RecyclerView recyclerViewIssue;
    DatabaseReference referenceProject;
    DatabaseReference referenceIssue;
    AdapterProjectsListToHome adapterProjectsList;
    AdapterIssuesList adapterIssuesList;
    List<Project> listProjects;
    List<Issue> listIssues;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewProject = view.findViewById(R.id.recycler_view_list_project);
        recyclerViewProject.setHasFixedSize(true);
        recyclerViewProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewIssue = view.findViewById(R.id.recycler_view_list_issue);
        recyclerViewIssue.setHasFixedSize(true);
        recyclerViewIssue.setLayoutManager(new LinearLayoutManager(getContext()));
        listProjects = new ArrayList<>();
        listIssues = new ArrayList<>();
        adapterProjectsList = new AdapterProjectsListToHome(getContext(), listProjects, new AdapterProjectsListToHome.OnItemClickListener() {
            @Override
            public void onItemClick(String projectId) {
                navigateToAnotherProjectFragment(projectId);
            }
        });
        adapterIssuesList = new AdapterIssuesList(getContext(), listIssues, new AdapterIssuesList.OnItemClickListener() {
            @Override
            public void onItemClick(String issueId, String projectId) {
                navigateToAnotherIssueFragment(issueId);
            }
        });
        recyclerViewProject.setAdapter(adapterProjectsList);
        recyclerViewIssue.setAdapter(adapterIssuesList);
        referenceProject = FirebaseDatabase.getInstance().getReference("PROJECT");
        referenceProject.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Project> newList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Project project = dataSnapshot.getValue(Project.class);
                    newList.add(project);
                }
                listProjects.clear();
                listProjects.addAll(newList);
                adapterProjectsList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referenceIssue = FirebaseDatabase.getInstance().getReference("ISSUE");
        referenceIssue.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Issue> newList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Issue issue = dataSnapshot.getValue(Issue.class);
                    newList.add(issue);
                }
                listIssues.clear();
                listIssues.addAll(newList);
                adapterProjectsList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TextView link_from_home_to_project = view.findViewById(R.id.link_from_home_to_project);
        link_from_home_to_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProjectFragment());
            }
        });
        Button buttonNoti = view.findViewById(R.id.btn_open_notification_home);
        buttonNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDropDownMenuNoti.showDropdownMenu(v);
            }
        });
        return view;
    }

    private void navigateToAnotherProjectFragment(String projectId) {
        IssueFragment fragment = new IssueFragment();
        Bundle bundle = new Bundle();
        bundle.putString("projectId", projectId);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToAnotherIssueFragment(String issueId) {
        IssueDetailFragment fragment = new IssueDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("issueId", issueId);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
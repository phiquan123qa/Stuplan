package com.vn.appdesign;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vn.models.Issue;
import com.vn.models.IssueStatusEnum;
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
    ColorStateList def;
    TextView item1, item2, item3, select;
    Integer selectedTab = 0;

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
        Button buttonBack = view.findViewById(R.id.btn_back_to_project) ;
        TextView titleTextView = view.findViewById(R.id.title_issue_fragment);
        item1 = view.findViewById(R.id.item_select1);
        item2 = view.findViewById(R.id.item_select2);
        item3 = view.findViewById(R.id.item_select3);
        select = view.findViewById(R.id.selected_tab);
        def = item2.getTextColors();
        Bundle arguments = getArguments();
        String projectId = null;
        if (arguments != null) {
            projectId = arguments.getString("projectId");
            if (projectId != null) {
                //projectID = arguments.getString(PRJ_ID);
                fetchProjectName(projectId, titleTextView);
            }
        }
        String finalProjectId = projectId;
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.animate().x(0).setDuration(100);
                item1.setTextColor(Color.WHITE);
                item2.setTextColor(def);
                item3.setTextColor(def);
                selectedTab = 0;
                updateListBasedOnSelectedTab(selectedTab, listIssues, reference, finalProjectId, view);
                adapterIssuesList.notifyDataSetChanged();

            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = item2.getWidth();
                select.animate().x(size).setDuration(100);
                item1.setTextColor(def);
                item2.setTextColor(Color.WHITE);
                item3.setTextColor(def);
                selectedTab = 1;
                updateListBasedOnSelectedTab(selectedTab, listIssues, reference, finalProjectId, view);
                adapterIssuesList.notifyDataSetChanged();
            }
        });
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = item3.getWidth()*2;
                select.animate().x(size).setDuration(100);
                item1.setTextColor(def);
                item2.setTextColor(def);
                item3.setTextColor(Color.WHITE);
                selectedTab = 2;
                updateListBasedOnSelectedTab(selectedTab, listIssues, reference, finalProjectId, view);
                adapterIssuesList.notifyDataSetChanged();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_list_issue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listIssues = new ArrayList<>();
        adapterIssuesList = new AdapterIssuesList(getContext(), listIssues);
        recyclerView.setAdapter(adapterIssuesList);
        reference = FirebaseDatabase.getInstance().getReference("ISSUE");
        updateListBasedOnSelectedTab(selectedTab, listIssues, reference, finalProjectId, view);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Issue> newList = new ArrayList<>();
//                String projectIdd = finalProjectId;
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Issue issue = dataSnapshot.getValue(Issue.class);
//                    if (issue != null && projectIdd.equals(issue.getIdProject())) {
//                        switch (selectedTab) {
//                            case 0: // TODO tab
//                                if (IssueStatusEnum.TODO.equals(issue.getStatus())) {
//                                    newList.add(issue);
//                                }
//                                break;
//                            case 1: // DOING tab
//                                if (IssueStatusEnum.DOING.equals(issue.getStatus())) {
//                                    newList.add(issue);
//                                }
//                                break;
//                            case 2: // DONE tab
//                                if (IssueStatusEnum.DONE.equals(issue.getStatus())) {
//                                    newList.add(issue);
//                                }
//                                break;
//                        }
//                    }
//                }
//                listIssues.clear();
//                listIssues.addAll(newList);
//                adapterIssuesList.notifyDataSetChanged();
//                if (listIssues.isEmpty()) {
//                    // If empty, show a TextView or handle it in your UI
//                    showEmptyIssuesNotification(view, 1);
//                } else {
//                    // If not empty, hide the notification
//                    showEmptyIssuesNotification(view, 0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToProjectFragment();
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
    private void navigateBackToProjectFragment() {
        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Pop the back stack to go back to the ProjectFragment
        fragmentManager.popBackStack();
    }
    private void fetchProjectName(String projectId, TextView titleTextView) {
        DatabaseReference projectReference = FirebaseDatabase.getInstance().getReference("PROJECT").child(projectId);
        projectReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Project project = snapshot.getValue(Project.class);
                    if (project != null) {
                        String projectName = project.getTitle();
                        int maxLength = 8;
                        String truncatedTitle = projectName.length() > maxLength ? projectName.substring(0, maxLength) + "..." : projectName;
                        titleTextView.append(": " + truncatedTitle);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
    private void updateListBasedOnSelectedTab(int selectedTab, List<Issue> listIssues, DatabaseReference reference,String finalProjectId, View view) {
        // You can implement additional logic here if needed
        // For example, update the UI or perform other actions based on the selected tab
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Issue> newList = new ArrayList<>();
                String projectIdd = finalProjectId;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Issue issue = dataSnapshot.getValue(Issue.class);
                    if (issue != null && projectIdd.equals(issue.getIdProject())) {
                        switch (selectedTab) {
                            case 0: // TODO tab
                                if (IssueStatusEnum.TODO.equals(issue.getStatus())) {
                                    newList.add(issue);
                                }
                                break;
                            case 1: // DOING tab
                                if (IssueStatusEnum.DOING.equals(issue.getStatus())) {
                                    newList.add(issue);
                                }
                                break;
                            case 2: // DONE tab
                                if (IssueStatusEnum.DONE.equals(issue.getStatus())) {
                                    newList.add(issue);
                                }
                                break;
                        }
                    }
                }
                listIssues.clear();
                listIssues.addAll(newList);
                adapterIssuesList.notifyDataSetChanged();
                if (listIssues.isEmpty()&&selectedTab==0) {
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


    }
}
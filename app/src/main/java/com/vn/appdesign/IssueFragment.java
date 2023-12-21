package com.vn.appdesign;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

public class IssueFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PRJ_ID = "projectId";

    private String mParam1;
    private String mParam2;
    String projectID;
    RecyclerView recyclerView;
    DatabaseReference reference;
    AdapterIssuesList adapterIssuesList;
    List<Issue> listIssues;
    ColorStateList def;
    TextView item1, item2, item3, select;
    Integer selectedTab;
    private String finalProjectId;
    private View view;
    SearchView searchView;

    public IssueFragment() {
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
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (selectedTab == null) {
            selectedTab = 0;
        }
        if (savedInstanceState != null) {
            selectedTab = savedInstanceState.getInt("selectedTab", 0);
        } else {
            selectedTab = retrieveStateFromSharedPreferences();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_issue, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboardInFragment();
                return false;
            }
        });
        searchView = view.findViewById(R.id.input_search_issue);
        Button buttonBack = view.findViewById(R.id.btn_back_to_project);
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
                fetchProjectName(projectId, titleTextView);
            }
        }
        finalProjectId = projectId;
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
                int size = item3.getWidth() * 2;
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
        adapterIssuesList = new AdapterIssuesList(getContext(), listIssues, new AdapterIssuesList.OnItemClickListener() {
            @Override
            public void onItemClick(String issueId) {
                navigateToAnotherFragment(issueId);
            }
        });
        recyclerView.setAdapter(adapterIssuesList);
        reference = FirebaseDatabase.getInstance().getReference("ISSUE");
        updateListBasedOnSelectedTab(selectedTab, listIssues, reference, finalProjectId, view);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToProjectFragment();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterIssues(newText);
                return true;
            }
        });
        Button sortByNameButton = view.findViewById(R.id.btn_sort_issue);
        sortByNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterIssuesList.sortByName();
            }
        });
        return view;
    }

    private void showEmptyIssuesNotification(View view, Integer check) {
        if (check == 1) {
            RelativeLayout emptyIssuesNotification = view.findViewById(R.id.container_link_add_new_issue);
            emptyIssuesNotification.setVisibility(View.VISIBLE);
        } else if (check == 0) {
            RelativeLayout emptyIssuesNotification = view.findViewById(R.id.container_link_add_new_issue);
            emptyIssuesNotification.setVisibility(View.GONE);
        }

    }

    private void navigateBackToProjectFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
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
            }
        });
    }

    private void updateListBasedOnSelectedTab(int selectedTab, List<Issue> listIssues, DatabaseReference reference, String finalProjectId, View view) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Issue> newList = new ArrayList<>();
                String projectIdd = finalProjectId;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
                if (listIssues.isEmpty() && selectedTab == 0) {
                    showEmptyIssuesNotification(view, 1);
                } else {
                    showEmptyIssuesNotification(view, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void navigateToAnotherFragment(String issueId) {
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedTab", selectedTab);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveStateToSharedPreferences(selectedTab);
    }

    private void saveStateToSharedPreferences(int selectedTab) {
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedTab", selectedTab);
        editor.apply();
    }

    private int retrieveStateFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getInt("selectedTab", 0);
    }

    private void filterIssues(String query) {
        // Create a filtered list based on the search query
        List<Issue> filteredList = new ArrayList<>();
        for (Issue issue : listIssues) {
            if (issue.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(issue);
            }
        }
        adapterIssuesList.setFilter(filteredList);
    }
    private void closeKeyboardInFragment() {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
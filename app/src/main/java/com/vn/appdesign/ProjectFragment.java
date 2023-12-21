package com.vn.appdesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vn.models.Project;
import com.vn.utility.AdapterProjectsList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference reference;
    AdapterProjectsList adapterProjectsList;
    List<Project> listProjects;
    SearchView searchView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProjectFragment() {
    }

    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
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
        View view = inflater.inflate(R.layout.fragment_project, container, false);
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
        searchView = view.findViewById(R.id.input_search_prj);
        recyclerView = view.findViewById(R.id.recycler_view_list_project);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listProjects = new ArrayList<>();
        adapterProjectsList = new AdapterProjectsList(getContext(), listProjects, new AdapterProjectsList.OnItemClickListener() {
            @Override
            public void onItemClick(String projectId) {
                navigateToAnotherFragment(projectId);
            }
        });
        recyclerView.setAdapter(adapterProjectsList);
        reference = FirebaseDatabase.getInstance().getReference("PROJECT");
        reference.addValueEventListener(new ValueEventListener() {
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProjects(newText);
                return true;
            }
        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchView.clearFocus();
                }
            }
        });
        Button sortByNameButton = view.findViewById(R.id.btn_sort_prj);
        sortByNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterProjectsList.sortByName();
            }
        });
        return view;
    }

    private void navigateToAnotherFragment(String projectId) {
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

    private void filterProjects(String query) {
        // Create a filtered list based on the search query
        List<Project> filteredList = new ArrayList<>();
        for (Project project : listProjects) {
            if (project.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(project);
            }
        }
        adapterProjectsList.setFilter(filteredList);
    }

    private void closeKeyboardInFragment() {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
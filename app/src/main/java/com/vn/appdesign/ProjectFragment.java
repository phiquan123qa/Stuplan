package com.vn.appdesign;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project, container, false);
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Project> newList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
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
        return view;
    }
    private void navigateToAnotherFragment(String projectId) {
        // Implement navigation logic here, for example, using FragmentManager
        // and replace the current fragment with a new one while passing the projectId
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
}
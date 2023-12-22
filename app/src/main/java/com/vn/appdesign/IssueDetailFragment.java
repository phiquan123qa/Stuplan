package com.vn.appdesign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vn.models.Issue;
import com.vn.models.Project;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ISS_ID = "issueId";
    EditText titleIssueTextView;
    EditText descriptionIssue;
    DatabaseReference issueReference;
    Button buttonBack;
    Button option;
    String projectId = null;
    private String mParam1;
    private String mParam2;
    public IssueDetailFragment() {
        // Required empty public constructor
    }
    public static IssueDetailFragment newInstance(String param1, String param2, String issueId) {
        IssueDetailFragment fragment = new IssueDetailFragment();
        Bundle args = new Bundle();
        args.putString(ISS_ID, issueId);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (getArguments() != null) {
            String issueId = arguments.getString("issueId");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue_detail, container, false);
        buttonBack = view.findViewById(R.id.btn_back_to_list_issue);
        titleIssueTextView = view.findViewById(R.id.text_title_detail_issue);
        descriptionIssue = view.findViewById(R.id.edit_text_description_deatil_issue);
        option = view.findViewById(R.id.btn_open_option);
        Bundle arguments = getArguments();
        String issueId = null;
        if (arguments != null) {
            issueId = arguments.getString("issueId");
            projectId = arguments.getString("projectId");
            if (issueId != null) {
                fetchIssueName(issueId, titleIssueTextView, descriptionIssue);
            }
        }
        titleIssueTextView.addTextChangedListener(textWatcher);
        descriptionIssue.addTextChangedListener(textWatcher);
        option.setOnClickListener(v -> showDropdownMenu(v));
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToProjectFragment();
            }
        });
        return view;
    }

    private void fetchIssueName(String issueId, EditText titleTextView, EditText descriptionText) {
        issueReference = FirebaseDatabase.getInstance().getReference("ISSUE").child(issueId);
        issueReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Issue issue = snapshot.getValue(Issue.class);
                    if (issue != null) {
                        String issueTitle = issue.getTitle();
                        String description = issue.getDescription();
                        titleTextView.setText(issueTitle);
                        descriptionText.setText(description);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            saveIssueData();
        }
    };

    private void saveIssueData() {
        String newTitle = titleIssueTextView.getText().toString();
        String newDescription = descriptionIssue.getText().toString();
        issueReference.child("title").setValue(newTitle);
        issueReference.child("description").setValue(newDescription);
    }

    private void navigateBackToProjectFragment() {
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

    private void showDropdownMenu(View anchorView) {
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_menu_option_issue, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        int x = location[0] - popupView.getWidth() - 300;
        int y = location[1] + 100;

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

        TextView mtTodo = popupView.findViewById(R.id.move_to_todo);
        TextView mtDoing = popupView.findViewById(R.id.move_to_doing);
        TextView mtDone = popupView.findViewById(R.id.move_to_done);
        TextView mtDelete = popupView.findViewById(R.id.move_to_delete);

        mtTodo.setOnClickListener(v -> {
            issueReference.child("status").setValue("TODO");
            Toast.makeText(getContext(), "Success move to todo", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        mtDoing.setOnClickListener(v -> {
            issueReference.child("status").setValue("DOING");
            Toast.makeText(getContext(), "Success move to doing", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        mtDone.setOnClickListener(v -> {
            issueReference.child("status").setValue("DONE");
            Toast.makeText(getContext(), "Success move to done", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        mtDelete.setOnClickListener(v -> {
            issueReference.removeValue();
            navigateBackToProjectFragment();
            Toast.makeText(getContext(), "Delete complete", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
    }
}
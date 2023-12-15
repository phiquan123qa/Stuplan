package com.vn.appdesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vn.appdesign.databinding.ActivityHomeBinding;
import com.vn.controllers.ManagerIssueImpl;
import com.vn.controllers.ManagerProjectImpl;
import com.vn.controllers.impl.ManagerIssue;
import com.vn.controllers.impl.ManagerProject;
import com.vn.models.Issue;
import com.vn.models.IssueStatusEnum;
import com.vn.models.Project;
import com.vn.utility.colorPicker.ColorPickerDialog;
import com.vn.utility.icon.IconAdapter;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    List<Integer> icons = Arrays.asList(
            R.drawable.angular_16_svgrepo_com,
            R.drawable.c_16_svgrepo_com,
            R.drawable.class_16_svgrepo_com,
            R.drawable.c_sharp_16_svgrepo_com,
            R.drawable.css_16_svgrepo_com,
            R.drawable.dart_16_svgrepo_com,
            R.drawable.docker_16_svgrepo_com,
            R.drawable.elm_16_svgrepo_com,
            R.drawable.error_16_svgrepo_com,
            R.drawable.eslint_16_svgrepo_com,
            R.drawable.go_16_svgrepo_com,
            R.drawable.graphql_16_svgrepo_com,
            R.drawable.html_16_svgrepo_com,
            R.drawable.interface_16_svgrepo_com,
            R.drawable.ionic_16_svgrepo_com,
            R.drawable.java_16_svgrepo_com,
            R.drawable.javascript_16_svgrepo_com,
            R.drawable.json_16_svgrepo_com,
            R.drawable.keyword_16_svgrepo_com,
            R.drawable.kotlin_16_svgrepo_com,
            R.drawable.kubernetes_16_svgrepo_com,
            R.drawable.layout_16_svgrepo_com,
            R.drawable.lua_16_svgrepo_com,
            R.drawable.next_16_svgrepo_com,
            R.drawable.nginx_16_svgrepo_com,
            R.drawable.node_16_svgrepo_com,
            R.drawable.npm_16_svgrepo_com,
            R.drawable.php_16_svgrepo_com,
            R.drawable.python_16_svgrepo_com,
            R.drawable.r_16_svgrepo_com,
            R.drawable.rust_16_svgrepo_com,
            R.drawable.scala_16_svgrepo_com,
            R.drawable.struct_16_svgrepo_com,
            R.drawable.svelte_16_svgrepo_com,
            R.drawable.swift_16_svgrepo_com,
            R.drawable.terraform_16_svgrepo_com,
            R.drawable.tmux_16_svgrepo_com,
            R.drawable.toml_16_svgrepo_com,
            R.drawable.typescript_16_svgrepo_com,
            R.drawable.variable_16_svgrepo_com,
            R.drawable.vim_16_svgrepo_com,
            R.drawable.vim_replace_mode_16_svgrepo_com,
            R.drawable.vscode_16_svgrepo_com,
            R.drawable.vue_16_svgrepo_com,
            R.drawable.yarn_16_svgrepo_com
    );
    FirebaseAuth mAuth;
    ActivityHomeBinding binding;
    FloatingActionButton mainFAB;
    ExtendedFloatingActionButton issueFAB, prfFAB;
    DatabaseReference reference;
    ManagerIssue managerIssue = new ManagerIssueImpl();
    ManagerProject managerProject = new ManagerProjectImpl();
    Boolean isVisible = false;
    private int currentColor = Color.GRAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        mainFAB = findViewById(R.id.floatingActionButton);
        issueFAB = findViewById(R.id.addIssueBtn);
        prfFAB = findViewById(R.id.addPrjBtn);

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.bottom_project) {
                replaceFragment(new ProjectFragment());
            } else if (item.getItemId() == R.id.bottom_summary) {
                replaceFragment(new SummaryFragment());
            } else if (item.getItemId() == R.id.bottom_acc) {
                replaceFragment(new AccFragment());
            }
            return true;
        });
        replaceFragment(new HomeFragment());

        mainFAB.setOnClickListener(v -> {
            if (isVisible) {
                hideSubFabs();
            } else {
                showSubFabs();
            }
            int newIconResource = isVisible ?  R.drawable.baseline_close_24 :R.drawable.baseline_add_24;
            mainFAB.setImageResource(newIconResource);
        });

        issueFAB.setOnClickListener(v -> showInputAddIssueDialog());
        prfFAB.setOnClickListener(v -> showInputAddProjectDialog());


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void showSubFabs() {
        isVisible = true;
        issueFAB.setVisibility(View.VISIBLE);
        prfFAB.setVisibility(View.VISIBLE);
    }

    private void hideSubFabs() {
        isVisible = false;
        issueFAB.setVisibility(View.GONE);
        prfFAB.setVisibility(View.GONE);
    }

    private void showInputAddIssueDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_issue_dialog);
        EditText textTitle = dialog.findViewById(R.id.editTextTitle);
        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.auto_cpl_txt);
        RelativeLayout rlBtnIcon = dialog.findViewById(R.id.selector_holder_icon);
        TextView iconButtonChoose = dialog.findViewById(R.id.selector_icon);
        EditText textDes = dialog.findViewById(R.id.editTextDescriptionIssue);
        TextView textColor = dialog.findViewById(R.id.text_color_show_issue);
        TextInputLayout textInputLayout = dialog.findViewById(R.id.header_round_selector);
        Button sybSubmitIssue = dialog.findViewById(R.id.btnCreateNewIssue);
        reference = FirebaseDatabase.getInstance().getReference("PROJECT");
        List<Project> itemsList = new ArrayList<>();

        ArrayAdapter<Project> adapterItems = new ArrayAdapter<Project>(this, R.layout.list_item_project, itemsList);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Project> newItems = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Project project = dataSnapshot.getValue(Project.class);
                    assert project != null;
                    newItems.add(project);
                }
                itemsList.clear();
                itemsList.addAll(newItems);
                adapterItems.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        autoCompleteTextView.setAdapter(adapterItems);
        final Project[] selectedProject = new Project[1];
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProject[0] = (Project) parent.getItemAtPosition(position);
                String projectId = selectedProject[0].getId();
                String projectTitle = selectedProject[0].getTitle();
                Toast.makeText(HomeActivity.this, "ID:"+projectId+"Item: " + projectTitle, Toast.LENGTH_SHORT).show();
                textInputLayout.clearFocus();
                autoCompleteTextView.clearFocus();
                textInputLayout.setHintEnabled(false);
            }
        });
        rlBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.add_icon_dialog, null);
                dialogView.setMinimumWidth(300);
                dialogView.setMinimumHeight(300);
                ListView iconListView = dialogView.findViewById(R.id.icon_list);
                IconAdapter iconAdapter = new IconAdapter(HomeActivity.this, icons);
                iconListView.setAdapter(iconAdapter);
                iconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        iconAdapter.setSelectedItemPosition(position);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Choose an Icon")
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int selectedIcon = iconAdapter.getSelectedItemPosition();
                                // Handle selected icon
                                if (selectedIcon != -1) {
                                    int iconSLT = icons.get(selectedIcon);
                                    iconButtonChoose.setBackgroundResource(iconSLT);
                                    // Handle selected icon
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        //Button select color
        //https://github.com/chiralcode/Android-Color-Picker
        Button btnColor = dialog.findViewById(R.id.selector_holder_color);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(HomeActivity.this, Color.parseColor("#3C47FF"), new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        btnColor.setBackgroundTintList(ColorStateList.valueOf(color));
                        //textColor.setText();
                    }

                });
                colorPickerDialog.show();
            }
        });

        sybSubmitIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleIssue = textTitle.getText().toString();
                String iconIssue = iconButtonChoose.getBackground().toString();
                String colorIssue = btnColor.getBackgroundTintList().toString();
                String projectIssue = (selectedProject[0] != null && selectedProject[0].getId() != null)
                        ? selectedProject[0].getId()
                        : null;
                String descriptionIssue = textDes.getText().toString();
                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (titleIssue.isEmpty() || projectIssue == null || descriptionIssue.isEmpty() || Uid.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    Issue issue = new Issue(null, projectIssue, titleIssue, descriptionIssue, colorIssue, iconIssue, IssueStatusEnum.TODO, new Date(), Uid);
                    managerIssue.addIssue(issue);
                    dialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Create Issue Successfully", Toast.LENGTH_SHORT).show();

                }

            }
        });

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        mainFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        dialog.show();


    }
    private void showInputAddProjectDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_project_dialog);

        RelativeLayout rlBtnIcon = dialog.findViewById(R.id.selector_holder_icon_prj);
        TextView iconButtonChoose = dialog.findViewById(R.id.selector_icon_prj);
        EditText projectTitle = dialog.findViewById(R.id.editTextTitlePrj);
        Button sybSubmitProject = dialog.findViewById(R.id.btnCreateNewPrj);

        rlBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.add_icon_dialog, null);
                dialogView.setMinimumWidth(300);
                dialogView.setMinimumHeight(300);
                ListView iconListView = dialogView.findViewById(R.id.icon_list);
                IconAdapter iconAdapter = new IconAdapter(HomeActivity.this, icons);
                iconListView.setAdapter(iconAdapter);
                iconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        iconAdapter.setSelectedItemPosition(position);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Choose an Icon")
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int selectedIcon = iconAdapter.getSelectedItemPosition();
                                // Handle selected icon
                                if (selectedIcon != -1) {
                                    int iconSLT = icons.get(selectedIcon);
                                    iconButtonChoose.setBackgroundResource(iconSLT);
                                    // Handle selected icon
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        sybSubmitProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleIssue = projectTitle.getText().toString();
                String iconIssue = iconButtonChoose.getBackground().toString();
                Drawable backgroundDrawable = iconButtonChoose.getBackground();
                String resourceName = getResourceNameFromDrawable(backgroundDrawable);
                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (titleIssue.isEmpty()||titleIssue==null) {
                    Toast.makeText(HomeActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    Project project = new Project(null, titleIssue, resourceName, Uid);
                    managerProject.addProject(project);
                    dialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Create Project Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        dialog.show();
    }
    private String getResourceNameFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        try {
            Resources resources = getResources();

            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                int resourceId = resources.getIdentifier(
                        resources.getResourceEntryName(bitmapDrawable.getBitmap().getGenerationId()),
                        "drawable",
                        getPackageName()
                );
                return resources.getResourceEntryName(resourceId);
            } else {
                return resources.getResourceEntryName(R.drawable.kotlin_16_svgrepo_com);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
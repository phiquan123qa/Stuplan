package com.vn.appdesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vn.appdesign.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    FloatingActionButton mainFAB;
    ExtendedFloatingActionButton issueFAB, prfFAB;
    Boolean isVisible = false;

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

        issueFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        prfFAB.setOnClickListener(v -> showInputDialog());
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

    private void showInputDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_issue_dialog);

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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
package com.vn.appdesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vn.utility.UtilityKeyboard;

public class EmailLoginActivity extends AppCompatActivity {

    private EditText editTextEmailAddress;
    private EditText editTextPassword;

    private TextView textViewLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View rootLayout = findViewById(R.id.rootLayout);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewLink = findViewById(R.id.linkTextView);


        rootLayout.setOnTouchListener((view, motionEvent) -> {
            UtilityKeyboard.hideSoftKeyboard(EmailLoginActivity.this);
            return false;
        });
        editTextEmailAddress.setOnTouchListener((view, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextEmailAddress.getRight() - editTextEmailAddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    UtilityKeyboard.clearData(editTextEmailAddress);
                    return true;
                }
            }
            return false;
        });

        editTextPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    UtilityKeyboard.togglePasswordVisibility(editTextPassword);
                    return true;
                }
            }
            return false;
        });

        textViewLink.setOnClickListener(view -> {
            Intent intent = new Intent(EmailLoginActivity.this, EmailRegisterActivity.class);
            startActivity(intent);
        });
    }

}
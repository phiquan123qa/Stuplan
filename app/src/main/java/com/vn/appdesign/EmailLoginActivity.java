package com.vn.appdesign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vn.controllers.AuthenHandleImpl;
import com.vn.controllers.impl.AuthenHandle;
import com.vn.utility.UtilityKeyboard;

public class EmailLoginActivity extends AppCompatActivity {

    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button buttonLG;
    private AuthenHandle authenHandle = new AuthenHandleImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View rootLayout = getWindow().getDecorView().findViewById(R.id.rootLayoutLogin);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLG = findViewById(R.id.button);
        TextView textViewLink = findViewById(R.id.linkTextView);

        buttonLG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(editTextEmailAddress.getText()).trim();
                password = String.valueOf(editTextPassword.getText()).trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(EmailLoginActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(EmailLoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                authenHandle.handleLoginByEmailPassword(EmailLoginActivity.this, email, password);
            }
        });

        rootLayout.setOnTouchListener((view, motionEvent) -> {
            UtilityKeyboard.hideSoftKeyboard(this);
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
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

}
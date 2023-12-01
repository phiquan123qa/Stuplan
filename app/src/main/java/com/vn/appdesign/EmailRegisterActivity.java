package com.vn.appdesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vn.utility.AuthenHandle;
import com.vn.utility.UtilityKeyboard;

public class EmailRegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRePassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);
        TextView textViewLink = findViewById(R.id.linkTextViewRG);

        View rootLayout = getWindow().getDecorView().findViewById(R.id.rootLayoutRegister);
        etEmail = findViewById(R.id.editTextEmailAddressRG);
        etPassword = findViewById(R.id.editTextPasswordRG);
        etRePassword = findViewById(R.id.editTextRePasswordRG);
        btnSubmit = findViewById(R.id.buttonRG);
        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(view -> {
            String email, password, repassword;
            email = String.valueOf(etEmail.getText()).trim();
            password = String.valueOf(etPassword.getText()).trim();
            repassword = String.valueOf(etRePassword.getText()).trim();

            if(TextUtils.isEmpty(email)){
                Toast.makeText(EmailRegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(EmailRegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(repassword)){
                Toast.makeText(EmailRegisterActivity.this, "Please Enter Your RePassword", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!TextUtils.equals(password, repassword)){
                Toast.makeText(EmailRegisterActivity.this, "Please Check Password and RePassword", Toast.LENGTH_SHORT).show();
                return;
            }
            AuthenHandle.handleCreateUserByEmailPassword(EmailRegisterActivity.this, email, password);
        });



        rootLayout.setOnTouchListener((view, motionEvent) -> {
            UtilityKeyboard.hideSoftKeyboard(this);
            return false;
        });
        textViewLink.setOnClickListener(view -> {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            AuthenHandle.updateUI(this, currentUser);
        }
    }
}
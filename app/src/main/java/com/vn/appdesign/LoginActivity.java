package com.vn.appdesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vn.controllers.AuthenHandleImpl;
import com.vn.controllers.impl.AuthenHandle;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    Button buttonEmail;
    Button buttonFace;
    Button buttonGoogle;
    FirebaseAuth mAuth;
    DatabaseReference database;
    CallbackManager callbackManager;
    AuthenHandle authenHandle = new AuthenHandleImpl();
    int RC_SIGN_IN = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set layout screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Definition value
        FacebookSdk.sdkInitialize(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        callbackManager = CallbackManager.Factory.create();
        buttonEmail = findViewById(R.id.login_email);
        buttonFace = findViewById(R.id.login_facebook);
        buttonGoogle = findViewById(R.id.login_google);
        //Create new intent when login by email and password
        buttonEmail.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, EmailLoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
        buttonGoogle.setOnClickListener(view -> authenHandle.googleSignIn(LoginActivity.this));
        //Set onclick to login Facebook
        buttonFace.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    authenHandle.handleFacebookAccessToken(LoginActivity.this, loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            authenHandle.updateUI(this, currentUser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authenHandle.handleGoogleAccessToken(this, account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
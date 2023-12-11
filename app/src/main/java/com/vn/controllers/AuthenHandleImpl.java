package com.vn.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vn.controllers.impl.AuthenHandle;
import com.vn.models.User;
import com.vn.appdesign.EmailLoginActivity;
import com.vn.appdesign.EmailRegisterActivity;
import com.vn.appdesign.HomeActivity;
import com.vn.appdesign.LoginActivity;

public class AuthenHandleImpl implements AuthenHandle {
    public void handleFacebookAccessToken(LoginActivity activity, AccessToken token) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        User userAuth = new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
                        database.child("USERS")
                                .child(user.getUid())
                                .setValue(userAuth)
                                .addOnCompleteListener(writeTask -> {
                                    if (writeTask.isSuccessful()) {
                                        updateUI(activity, user);
                                    } else {
                                        Toast.makeText(activity, "Failed to write data to the database: " + writeTask.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void handleGoogleAccessToken(LoginActivity activity, String idToken) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        User userAuth = new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
                        database.child("USERS")
                                .child(user.getUid())
                                .setValue(userAuth)
                                .addOnCompleteListener(writeTask -> {
                                    if (writeTask.isSuccessful()) {
                                        updateUI(activity,user);
                                    } else {
                                        Toast.makeText(activity, "Failed to write data to the database: " + writeTask.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(activity, "Login google error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void handleCreateUserByEmailPassword(EmailRegisterActivity activity, String email, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        User userAuth = new User(user.getUid(), user.getEmail(), null);
                        database.child("USERS")
                                .child(user.getUid())
                                .setValue(userAuth)
                                .addOnCompleteListener(writeTask -> {
                                    if (writeTask.isSuccessful()) {
                                        updateUI(activity,user);
                                    } else {
                                        Toast.makeText(activity, "Failed to write data to the database: " + writeTask.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                    }
                     else {
                        Toast.makeText(activity, task.toString(), Toast.LENGTH_SHORT).show();}
                });
    }

    public void handleLoginByEmailPassword(EmailLoginActivity activity, String email, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        updateUI(activity,user);
                        Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Login emailpswd error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateUI(Activity activity, FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);

        } else {
            Toast.makeText(activity, "Please Sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }
    public void googleSignIn(LoginActivity activity) {
        int RC_SIGN_IN = 20;
        GoogleSignInOptions  googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("440222015307-2bt1k55i43baa473uaruh1ka7jsu8vrp.apps.googleusercontent.com")
                .requestEmail()
                .requestId()
                .build();;
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);
        googleSignInClient.signOut().addOnCompleteListener( activity,
                task -> {
                    Intent intent = googleSignInClient.getSignInIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivityForResult(intent, RC_SIGN_IN);
                });
    }

}

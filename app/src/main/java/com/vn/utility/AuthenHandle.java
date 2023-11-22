package com.vn.utility;

import static androidx.core.app.ActivityCompat.startActivityForResult;

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
import com.vn.Models.User;
import com.vn.appdesign.HomeActivity;
import com.vn.appdesign.LoginActivity;

import java.util.concurrent.Executor;

public class AuthenHandle {
    public static void handleFacebookAccessToken(LoginActivity activity, AccessToken token) {
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

    public static void handleGoogleAccessToken(LoginActivity activity, String idToken) {
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
    public static void updateUI(LoginActivity activity, FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Please Sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }
    public static void googleSignIn(LoginActivity activity) {
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
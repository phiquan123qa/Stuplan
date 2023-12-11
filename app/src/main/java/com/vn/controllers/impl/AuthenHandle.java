package com.vn.controllers.impl;

import android.app.Activity;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;
import com.vn.appdesign.EmailLoginActivity;
import com.vn.appdesign.EmailRegisterActivity;
import com.vn.appdesign.LoginActivity;

public interface AuthenHandle {
    public void handleFacebookAccessToken(LoginActivity activity, AccessToken token);
    public  void handleGoogleAccessToken(LoginActivity activity, String idToken);
    public  void handleCreateUserByEmailPassword(EmailRegisterActivity activity, String email, String password);
    public  void handleLoginByEmailPassword(EmailLoginActivity activity, String email, String password);
    public  void updateUI(Activity activity, FirebaseUser user);
    public  void googleSignIn(LoginActivity activity);
}

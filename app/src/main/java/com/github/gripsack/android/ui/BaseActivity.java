package com.github.gripsack.android.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.auth.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public abstract class BaseActivity extends AppCompatActivity implements ProgressState {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    protected FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected abstract boolean isAuthRequired();

    protected abstract FirebaseAuth.AuthStateListener createAuthStateListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForUpdates();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = createAuthStateListener();
        if (isAuthRequired()) {
            mUser = mAuth.getCurrentUser();
            if (mUser == null) {
//                Intent intent = AuthActivity.newIntent(this);
                Intent intent = SignInActivity.newIntent(this);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        // Remove this for store builds!
        UpdateManager.unregister();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
}

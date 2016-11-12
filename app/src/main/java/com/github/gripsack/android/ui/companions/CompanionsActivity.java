package com.github.gripsack.android.ui.companions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.SingleFragmentActivity;
import com.google.firebase.auth.FirebaseAuth;


public class CompanionsActivity extends SingleFragmentActivity {


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, CompanionsActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return CompanionsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setTitle(getResources().getString(R.string.title_companions));
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    protected FirebaseAuth.AuthStateListener createAuthStateListener() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}

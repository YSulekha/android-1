package com.github.gripsack.android.ui.timeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.SingleFragmentActivity;
import com.google.firebase.auth.FirebaseAuth;


public class TimelineActivity extends SingleFragmentActivity {


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TimelineActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return TimelineFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

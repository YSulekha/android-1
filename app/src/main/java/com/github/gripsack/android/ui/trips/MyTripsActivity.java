package com.github.gripsack.android.ui.trips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.SingleFragmentActivity;

public class MyTripsActivity extends SingleFragmentActivity {

    public final static String EXTRA_ITEMTYPE = "itemtype";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MyTripsActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
     /*   if (getIntent().getStringExtra(EXTRA_ITEMTYPE).equals("Upcoming")) {
            getSupportActionBar().setTitle(getString(R.string.tab_trips_upcoming));
            return UpcomingFragment.newInstance();
        }
        if (getIntent().getStringExtra(EXTRA_ITEMTYPE).equals("Completed")) {
            getSupportActionBar().setTitle(getString(R.string.tab_trips_completed));
            return CompletedFragment.newInstance();
        }*/
        return UpcomingFragment.newInstance();
    }

    @Override
    protected void onAuthStateSignIn() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

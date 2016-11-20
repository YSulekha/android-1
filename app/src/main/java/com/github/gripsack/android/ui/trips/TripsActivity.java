package com.github.gripsack.android.ui.trips;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.TabFragmentsActivity;
import com.github.gripsack.android.ui.navigation.ViewPagerAdapter;

public class TripsActivity extends TabFragmentsActivity {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TripsActivity.class);
        return intent;
    }

    @Override
    protected void onAuthStateSignIn() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected ViewPagerAdapter createViewPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(UpcomingFragment.newInstance(), getResources().getString(R.string.tab_trips_upcoming));
        adapter.addFrag(CompletedFragment.newInstance(), getResources().getString(R.string.tab_trips_completed));
        adapter.addFrag(MapsFragment.newInstance(), getResources().getString(R.string.tab_trips_maps));
        adapter.addFrag(PhotosFragment.newInstance(), getResources().getString(R.string.tab_trips_photos));
        return adapter;
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

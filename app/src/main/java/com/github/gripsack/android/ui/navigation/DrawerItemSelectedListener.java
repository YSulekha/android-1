package com.github.gripsack.android.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.companions.CompanionsActivity;
import com.github.gripsack.android.ui.explore.ExploreFragment;
import com.github.gripsack.android.ui.files.FilesActivity;
import com.github.gripsack.android.ui.places.MyPlacesActivity;
import com.github.gripsack.android.ui.settings.SettingsActivity;
import com.github.gripsack.android.ui.timeline.TimelineActivity;
import com.github.gripsack.android.ui.trips.MyTripsActivity;
import com.github.gripsack.android.ui.timeline.TimelineFragment;
import com.github.gripsack.android.ui.trips.AddTripActivity;
import com.github.gripsack.android.ui.trips.CompletedFragment;
import com.github.gripsack.android.ui.trips.UpcomingFragment;



import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerItemSelectedListener implements
        NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private Callbacks mCallbacks;
    private GoogleApiClient mGoogleApiClient;

    public interface Callbacks {
        void onSwitchFragment(Fragment fragment, MenuItem item);
    }

    public DrawerItemSelectedListener(FragmentActivity context, GoogleApiClient client) {
        mGoogleApiClient = client;
        mCallbacks = (Callbacks) context;
        mContext = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass = null;
        Intent intent = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_explore:
                fragmentClass = ExploreFragment.class;
                break;

            case R.id.nav_trips_upcoming:
                intent = MyTripsActivity.newIntent(mContext);
                intent.putExtra(MyTripsActivity.EXTRA_ITEMTYPE,"Upcoming");
                mContext.startActivity(intent);
              //  fragmentClass = UpcomingFragment.class;
                break;

            case R.id.nav_places_bucketlist:
                intent = MyPlacesActivity.newIntent(mContext);
                intent.putExtra(MyPlacesActivity.EXTRA_ITEMTYPE,"bucketlist");
                mContext.startActivity(intent);
               // fragmentClass = BucketlistFragment.class;
                break;


            case R.id.nav_places_visited:
                intent = MyPlacesActivity.newIntent(mContext);
                intent.putExtra(MyPlacesActivity.EXTRA_ITEMTYPE,"visited");
                mContext.startActivity(intent);
           //     fragmentClass = VisitedFragment.class;
                break;

            case R.id.nav_timeline:
                intent = TimelineActivity.newIntent(mContext);
                mContext.startActivity(intent);
            //    fragmentClass = TimelineFragment.class;
                break;

            case R.id.nav_storage:
                intent = FilesActivity.newIntent(mContext);
                mContext.startActivity(intent);
              //  fragmentClass = FilesFragment.class;
                break;

            case R.id.nav_companions:
                intent = CompanionsActivity.newIntent(mContext);
                mContext.startActivity(intent);
                return true;

            case R.id.nav_settings:
                intent = SettingsActivity.newIntent(mContext);
                mContext.startActivity(intent);
                /*intent= AddTripActivity.newIntent(mContext);
                mContext.startActivity(intent);*/
                break;

            case R.id.nav_logout:
                signOut();
                break;
        }

        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCallbacks.onSwitchFragment(fragment, item);
        }

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }
}

package com.github.gripsack.android.ui.explore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.DrawerActivity;
import com.github.gripsack.android.ui.companions.CompanionsActivity;
import com.github.gripsack.android.ui.files.FilesActivity;
import com.github.gripsack.android.ui.media.MediaActivity;
import com.github.gripsack.android.ui.places.PlacesActivity;
import com.github.gripsack.android.ui.settings.SettingsActivity;
import com.github.gripsack.android.ui.timeline.TimelineActivity;
import com.github.gripsack.android.ui.trips.AddTripActivity;
import com.github.gripsack.android.ui.trips.TripsActivity;
import com.github.gripsack.android.utils.GoogleUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class ExploreActivity extends DrawerActivity {


    @Override
    protected Fragment createFragment() {
        return ExploreFragment.newInstance();
    }

    @Override
    protected GoogleApiClient createGoogleApiClient() {
        return GoogleUtil.getGoogleApiClient(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                //TODO handle ConnectionFailed here
            }
        });
    }

    @Override
    protected FirebaseAuth.AuthStateListener createAuthStateListener() {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateNavigationView();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(ExploreActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent intent;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_explore:
                break;
            case R.id.nav_places:
                intent = PlacesActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.nav_trips:
                intent = TripsActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.nav_timeline:
                intent = TimelineActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.nav_media:
                intent = MediaActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.nav_files:
                intent = FilesActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.nav_companions:
                intent = CompanionsActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = SettingsActivity.newIntent(this);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

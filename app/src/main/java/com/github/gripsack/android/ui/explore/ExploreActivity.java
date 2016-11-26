package com.github.gripsack.android.ui.explore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.DrawerActivity;
import com.github.gripsack.android.ui.companions.CompanionsActivity;
import com.github.gripsack.android.ui.files.FilesActivity;
import com.github.gripsack.android.ui.media.MediaActivity;
import com.github.gripsack.android.ui.places.PlacesActivity;
import com.github.gripsack.android.ui.settings.SettingsActivity;
import com.github.gripsack.android.ui.timeline.TimelineActivity;
import com.github.gripsack.android.ui.trips.TripsActivity;
import com.github.gripsack.android.utils.GoogleUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

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
    protected void onAuthStateSignIn() {
        updateNavigationView();

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

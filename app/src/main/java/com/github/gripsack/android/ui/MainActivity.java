package com.github.gripsack.android.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.navigation.DrawerItemSelectedListener;
import com.github.gripsack.android.utils.FirebaseUtil;
import com.github.gripsack.android.utils.GoogleUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends SingleFragmentActivity implements DrawerItemSelectedListener.Callbacks {

    private TextView mEmail;
    private TextView mDisplayName;
    private CircleImageView mProfileImageView;
    private NavigationView mNavigationView;
    private DrawerItemSelectedListener mNavigationListener;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mFriendsCount;
    private TextView mTripsCount;
    private TextView mBucketListCount;

    private static final String MENU_ITEM_ACTIVE = "menuItemActive";
    private static int mMenuItemActive = 0;

    private DrawerLayout mDrawer;


    @Override
    protected Fragment createFragment() {
        return null;
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_drawer;
    }

    @Override
    protected void onAuthStateSignIn() {
        updateNavigationView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                updateNavigationView();
            }
        };
        TextView textView = (TextView) findViewById(R.id.text);
        mDrawer.addDrawerListener(mDrawerToggle);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        GoogleApiClient googleApiClient = GoogleUtil.getGoogleApiClient(this, this);
        mNavigationListener = new DrawerItemSelectedListener(this, googleApiClient);
        mNavigationView.setNavigationItemSelectedListener(mNavigationListener);


        updateNavigationView();

        MenuItem item = mNavigationView.getMenu().getItem(0);
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mMenuItemActive = (int) savedInstanceState.getSerializable(MENU_ITEM_ACTIVE);
            item = mNavigationView.getMenu().findItem(mMenuItemActive);
        }
        mNavigationListener.onNavigationItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void updateNavigationView() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        View header = mNavigationView.getHeaderView(0);
        mEmail = (TextView) header.findViewById(R.id.email);
        mDisplayName = (TextView) header.findViewById(R.id.userDisplayName);
        mProfileImageView = (CircleImageView) header.findViewById(R.id.profileImageView);
        mFriendsCount = (TextView) header.findViewById(R.id.userFriendsCount);
        mTripsCount = (TextView) header.findViewById(R.id.userTripCount);
        mBucketListCount = (TextView) header.findViewById(R.id.userBucketListCount);

        String currentUserId = FirebaseUtil.getCurrentUserId();

        if(currentUserId!=null) {
            DatabaseReference UserInforef = FirebaseUtil.getUsersRef().child(currentUserId);

            //Get number of trips
            UserInforef.child("trips").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (mTripsCount != null) {
                        mTripsCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            //Get number of bucketlist
            UserInforef.child("places").child("bucketlist").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (mBucketListCount != null) {
                        mBucketListCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Get number of companions
            FirebaseUtil.getCompanionsRef().child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (mFriendsCount != null) {
                        mFriendsCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            mTripsCount.setText(String.valueOf(0));
            mBucketListCount.setText(String.valueOf(0));
            mFriendsCount.setText(String.valueOf(0));
        }
        if (mUser != null) {
            String name = "";
            Uri photoUrl = null;
            for (UserInfo profile : mUser.getProviderData()) {
                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                photoUrl = profile.getPhotoUrl();
            }

            if (photoUrl != null) {
                Glide.with(this).load(photoUrl.toString())
                        .centerCrop()
                        .dontAnimate()
                        .into(mProfileImageView);
            }
            mEmail.setText(mUser.getEmail());
            mDisplayName.setText(name);
        } else {
            mEmail.setText("");
            mDisplayName.setText("");
            mProfileImageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSwitchFragment(Fragment fragment, MenuItem item) {
        mMenuItemActive = item.getItemId();
        switchActivityFragment(fragment);
        getSupportActionBar().setTitle(item.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(MENU_ITEM_ACTIVE, mMenuItemActive);
        super.onSaveInstanceState(savedInstanceState);
    }
}
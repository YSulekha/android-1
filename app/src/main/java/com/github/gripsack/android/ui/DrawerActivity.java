package com.github.gripsack.android.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public abstract class DrawerActivity extends SingleFragmentActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private TextView mEmail;
    private TextView mDisplayName;
    private ImageView mProfileImageView;
    private NavigationView mNavigationView;
//    private Button mSignInButton;
    private Button mSignOutButton;

    protected abstract GoogleApiClient createGoogleApiClient();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = createGoogleApiClient();
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        updateNavigationView();
    }

    protected void updateNavigationView() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        View header = mNavigationView.getHeaderView(0);
        mEmail = (TextView) header.findViewById(R.id.email);
        mDisplayName = (TextView) header.findViewById(R.id.userDisplayName);
        mProfileImageView = (ImageView) header.findViewById(R.id.profileImageView);
//        mSignInButton = (Button) header.findViewById(R.id.google_sign_in_button);
        mSignOutButton = (Button) header.findViewById(R.id.sign_out_button);

//        mSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = AuthActivity.newIntent(DrawerActivity.this);
//                Intent intent = SignInActivity.newIntent(DrawerActivity.this);
//                startActivity(intent);
//            }
//        });

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

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
                        .crossFade()
                        .into(mProfileImageView);
            }
            mEmail.setText(mUser.getEmail());
            mDisplayName.setText(name);
//            mSignInButton.setVisibility(View.GONE);
//            mSignOutButton.setVisibility(View.VISIBLE);
        } else {
//            mSignOutButton.setVisibility(View.GONE);
//            mSignInButton.setVisibility(View.VISIBLE);
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

    private void signOut() {
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }
}

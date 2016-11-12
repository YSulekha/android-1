package com.github.gripsack.android.utils;


import android.support.v4.app.FragmentActivity;

import com.github.gripsack.android.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleUtils {

    public static GoogleApiClient getGoogleApiClient(FragmentActivity context, GoogleApiClient.OnConnectionFailedListener listener) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(context)
                .enableAutoManage(context /* FragmentActivity */, listener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}

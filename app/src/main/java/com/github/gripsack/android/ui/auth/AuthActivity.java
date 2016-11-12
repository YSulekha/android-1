
package com.github.gripsack.android.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.gripsack.android.ui.SingleFragmentActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AuthActivity.class);
        return intent;
    }

    @Override
    protected boolean isAuthRequired() {
        return false;
    }

    @Override
    protected FirebaseAuth.AuthStateListener createAuthStateListener() {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                ((AuthFragment) mActivityFragment).onAuthStateChanged(firebaseAuth);
            }
        };
    }

    @Override
    protected Fragment createFragment() {
        return AuthFragment.newInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("AuthActivity","onActivityResult");
        mActivityFragment.onActivityResult(requestCode, resultCode, data);
    }
}

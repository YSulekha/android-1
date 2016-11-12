package com.github.gripsack.android.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.gripsack.android.R;
import com.github.gripsack.android.ui.ProgressState;
import com.github.gripsack.android.utils.GoogleUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AuthFragment extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    public SignInButton mGoogleSignInButton;
    private static final int RC_SIGN_IN = 9001;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    private ProgressState mProgressState;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mProgressState = (ProgressState) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);

        // Button listeners
        mGoogleSignInButton = (SignInButton) rootView.findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(view -> googleSignIn());

        mGoogleApiClient = GoogleUtils.getGoogleApiClient(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d(TAG, "onConnectionFailed:" + connectionResult);
                Toast.makeText(getContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void updateUI(FirebaseUser user) {
        mProgressState.hideProgressDialog();
        if (user != null) {
            mGoogleSignInButton.setVisibility(View.GONE);
        } else {
            mGoogleSignInButton.setVisibility(View.VISIBLE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        mProgressState.showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("AuthFragment","onActivityResult");
        Log.d("AuthFragment", "requestCode " + requestCode);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d("AuthFragment", "Hey You ");

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("AuthFragment", "Sup ");

            if (result.isSuccess()) {
                Log.d("AuthFragment", "result.isSuccess() " );

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.d("AuthFragment", "Google Sign In failed" );
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }
    }

    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent data = new Intent();
            getActivity().setResult(RESULT_OK, data); // set result code and bundle data for response
            getActivity().finish();
        }
        updateUI(user);
    }
}
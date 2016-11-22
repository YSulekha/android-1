package com.github.gripsack.android.ui.companions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.CompanionInvite;
import com.github.gripsack.android.data.model.User;
import com.github.gripsack.android.ui.SingleFragmentActivity;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;


public class CompanionsActivity extends SingleFragmentActivity implements CompanionsFragment.OnCompanionSelectedListener {

    private DatabaseReference mDatabase;

    private DatabaseReference mInviteReference;
    private DatabaseReference mUserReference;


    private static final int REQUEST_INVITE = 0;

    public FloatingActionButton mFab;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, CompanionsActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return CompanionsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.VISIBLE);
        mFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_plus));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                        .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);

                String uid = user.getUid();
                for (String id : ids) {
                    CompanionInvite invite = new CompanionInvite(user.getUid(), System.currentTimeMillis() / 1000);
                    mDatabase.child("invitation").child(id).setValue(invite);
                    Timber.d("invite %s from User %s", id, uid);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                showMessage(getString(R.string.send_failed));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if the intent contains an AppInvite and then process the referral information.
        Intent intent = getIntent();
        if (AppInviteReferral.hasReferral(intent)) {
            processReferralIntent(intent);
        }
    }

    private void processReferralIntent(Intent intent) {
        // Extract referral information from the intent
        String invitationId = AppInviteReferral.getInvitationId(intent);
        String deepLink = AppInviteReferral.getDeepLink(intent);
        Timber.d("Found Referral %s:%s", invitationId, deepLink);


        //TODO get invite info
        mInviteReference = FirebaseDatabase.getInstance().getReference()
                .child("invitation").child(invitationId);

        ValueEventListener inviteListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                CompanionInvite invite = dataSnapshot.getValue(CompanionInvite.class);
                Timber.d("Invite from user %s", invite.uid);
                mUserReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(invite.uid);

                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        User user = dataSnapshot.getValue(User.class);
                        Timber.d("Invite from user %s", user.displayName);


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanionsActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Add companion");

                        // Setting Dialog Message
                        alertDialog.setMessage(user.displayName + " invites you to share travel adventures together");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_account_plus);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user == null) {
                                    return;
                                }
                                invite.acceptedBy = user.getUid();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("invitation")
                                        .child(invitationId)
                                        .setValue(invite);
                                FirebaseDatabase.getInstance().getReference()
                                        .child("companions")
                                        .child(user.getUid())
                                        .child(invite.uid)
                                        .setValue(true);
                                FirebaseDatabase.getInstance().getReference()
                                        .child("companions")
                                        .child(invite.uid)
                                        .child(user.getUid())
                                        .setValue(true);
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("invitation")
                                        .child(invitationId)
                                        .removeValue();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.d(databaseError.toException());
                    }
                };
                mUserReference.addListenerForSingleValueEvent(userListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.d(databaseError.toException());
            }
        };
        mInviteReference.addListenerForSingleValueEvent(inviteListener);
    }

    private void showMessage(String msg) {
        Snackbar.make(mActivityFragment.getView(), msg, Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    protected FirebaseAuth.AuthStateListener createAuthStateListener() {
        return null;
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

    @Override
    public void onCompanionRemoved(String companionKey) {

    }

    @Override
    public void onCompanionAdded(String companionKey) {

    }

    @Override
    public void onCompanionSelected(String companionKey) {

    }
}

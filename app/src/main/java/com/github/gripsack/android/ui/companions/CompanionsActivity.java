package com.github.gripsack.android.ui.companions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.CompanionInvite;
import com.github.gripsack.android.data.model.User;
import com.github.gripsack.android.ui.SingleFragmentActivity;
import com.github.gripsack.android.utils.FirebaseUtil;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;


public class CompanionsActivity extends SingleFragmentActivity implements CompanionsFragment.OnCompanionSelectedListener {

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
    protected void onAuthStateSignIn() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String inviteId : ids) {
                    FirebaseUtil.saveCompanionInvitation(inviteId);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                showMessage(getString(R.string.send_failed));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Timber.d("Found Referral invitationId %s", invitationId);

        ValueEventListener inviteListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                CompanionInvite invite = dataSnapshot.getValue(CompanionInvite.class);
                Timber.d("Invite from user %s", invite.uid);

                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        User user = dataSnapshot.getValue(User.class);
                        Timber.d("Invite from user %s", user.displayName);
                        AlertDialog.Builder alertDialog = new AlertDialog
                                .Builder(CompanionsActivity.this)
                                .setTitle("Companion Invitation")
                                .setMessage(user.displayName + " invites you to share travel adventures together")
                                .setIcon(R.drawable.ic_account_plus)
                                .setPositiveButton("ACCEPT",
                                        (dialog, which) -> FirebaseUtil.acceptCompanionInvite(invitationId, invite))
                                .setNegativeButton("DECLINE",
                                        (dialog, which) -> FirebaseUtil.declineCompanionInvite(invitationId));
                        // Showing Alert Message
                        alertDialog.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.d(databaseError.toException());
                    }
                };
                FirebaseUtil
                        .getUserByIdRef(invite.uid)
                        .addListenerForSingleValueEvent(userListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.d(databaseError.toException());
            }
        };
        FirebaseUtil
                .getInvitationByIdRef(invitationId)
                .addListenerForSingleValueEvent(inviteListener);
    }

    private void showMessage(String msg) {
        Snackbar.make(mActivityFragment.getView(), msg, Snackbar.LENGTH_LONG).show();
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

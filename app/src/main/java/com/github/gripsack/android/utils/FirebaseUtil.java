package com.github.gripsack.android.utils;


import com.github.gripsack.android.data.model.CompanionInvite;
import com.github.gripsack.android.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class FirebaseUtil {

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseRef().child("users").child(getCurrentUserId());
        }
        return null;
    }


    public static DatabaseReference getCompanionsRef() {
        return getBaseRef().child("companions");
    }

    public static DatabaseReference getUsersRef() {
        return getBaseRef().child("users");
    }

    public static DatabaseReference getInvitationsRef() {
        return getBaseRef().child("invitation");
    }

    public static DatabaseReference getInvitationByIdRef(String invitationId) {
        return getBaseRef().child("invitation").child(invitationId);
    }

    public static void saveUser(User user) {
        String uid = getCurrentUserId();
        if (uid == null) {
            return;
        }
        getUsersRef().child(uid).setValue(user);
    }

    public static void saveCompanionInvitation(String inviteId) {
        String uid = getCurrentUserId();
        if (uid == null) {
            return;
        }
        CompanionInvite invite = new CompanionInvite(uid, System.currentTimeMillis() / 1000);
        getInvitationsRef().child(inviteId).setValue(invite);
        Timber.d("invite %s from User %s", inviteId, uid);
    }

    public static DatabaseReference getUserByIdRef(String uid) {
        return getUsersRef().child(uid);
    }

    public static void acceptCompanionInvite(String invitationId, CompanionInvite invite) {
        String uid = getCurrentUserId();
        if (uid == null) {
            return;
        }
        invite.acceptedBy = uid;
        getInvitationByIdRef(invitationId).setValue(invite);
        getCompanionsRef()
                .child(uid)
                .child(invite.uid)
                .setValue(true);
        getCompanionsRef()
                .child(invite.uid)
                .child(uid)
                .setValue(true);
    }

    public static void declineCompanionInvite(String invitationId) {
        getInvitationByIdRef(invitationId).removeValue();
    }
}

package com.github.gripsack.android.utils;


import com.github.gripsack.android.data.model.CompanionInvite;
import com.github.gripsack.android.data.model.Place;
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

    public static DatabaseReference getCurrentUserPlacesRef() {
        DatabaseReference user = getCurrentUserRef();
        if (user != null) {
            return
                    user
                    .child("places");
        }
        return null;
    }

    public static DatabaseReference getCurrentUserBucketPlacesRef() {
        DatabaseReference places = getCurrentUserPlacesRef();
        if (places != null) {
            return places.child("bucketlist");
        }
        return null;
    }

    public static DatabaseReference getCurrentUserLikedPlacesRef() {
        DatabaseReference places = getCurrentUserPlacesRef();
        if (places != null) {
            return places.child("liked");
        }
        return null;
    }


    public static DatabaseReference getCompanionsRef() {
        return getBaseRef().child("companions");
    }

    public static DatabaseReference getUsersRef() {
        return getBaseRef().child("users");
    }

    public static DatabaseReference getPlacesRef() {
        return getBaseRef().child("places");
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
        getUsersRef().child(uid).child("displayName").setValue(user.displayName);
        getUsersRef().child(uid).child("profileImageUrl").setValue(user.profileImageUrl);
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

    public static void savePlace(Place place) {
        getPlacesRef().child(place.getPlaceid()).setValue(place);
    }

    public static void likePlace(String placeId) {
        DatabaseReference user = getCurrentUserRef();
        if (user != null) {
            user.child("places")
                    .child("liked")
                    .child(placeId)
                    .setValue(true);
        }
    }

    public static void bucketPlace(String placeId) {
        DatabaseReference user = getCurrentUserRef();
        if (user != null) {
            user.child("places")
                    .child("bucketlist")
                    .child(placeId)
                    .setValue(true);
        }
    }
}

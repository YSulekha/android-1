package com.github.gripsack.android.data.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String displayName;
    public String profileImageUrl;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(CompanionInvite.class)
    }

    public User(String displayName, Uri profileImageUrl) {
        this.displayName = displayName;
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl.toString();
        }
    }
}
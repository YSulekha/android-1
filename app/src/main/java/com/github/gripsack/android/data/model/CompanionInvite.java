package com.github.gripsack.android.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CompanionInvite {

    public String uid;
    public long timestamp;
    public String acceptedBy;

    public CompanionInvite() {
        // Default constructor required for calls to DataSnapshot.getValue(CompanionInvite.class)
    }

    public CompanionInvite(String uid, long timestamp) {
        this.uid = uid;
        this.timestamp = timestamp;
    }
}
package com.github.gripsack.android.ui.settings;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.github.gripsack.android.R;


public class SettingsFragment extends PreferenceFragmentCompat {


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}



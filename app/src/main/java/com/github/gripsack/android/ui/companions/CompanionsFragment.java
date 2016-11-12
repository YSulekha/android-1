package com.github.gripsack.android.ui.companions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.gripsack.android.R;

public class CompanionsFragment extends Fragment {
    public static CompanionsFragment newInstance() {
        return new CompanionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_companions, container, false);
        return rootView;
    }
}

package com.github.gripsack.android.ui.places;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.gripsack.android.R;

public class RecommendedFragment extends Fragment {

    public static RecommendedFragment newInstance() {
        return new RecommendedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places_recommended, container, false);
        return rootView;
    }
}

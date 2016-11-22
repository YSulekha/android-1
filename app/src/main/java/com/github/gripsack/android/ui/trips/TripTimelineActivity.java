package com.github.gripsack.android.ui.trips;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.vipul.hp_hp.timelineview.TimelineView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class TripTimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trip Timeline");

        //ArrayList<Place> places = (ArrayList<Place>)Parcels.unwrap(savedInstanceState.getParcelable("Places"));
        ArrayList<Place> places=(ArrayList<Place>)getIntent().getSerializableExtra("Places");
        TripTimelineAdapter adapter=new TripTimelineAdapter(this,places);
        RecyclerView view=(RecyclerView) findViewById(R.id.recyclerView);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);
    }

}

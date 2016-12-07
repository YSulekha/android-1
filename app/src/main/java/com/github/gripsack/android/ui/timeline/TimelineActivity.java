package com.github.gripsack.android.ui.timeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.ui.companions.CompanionsActivity;
import com.vipul.hp_hp.timelineview.TimelineView;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

    ImageView ivTripStart;
    ArrayList<Place> places;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TimelineActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        places = new ArrayList<Place>();

        //TODO:Need to get trip destinations to show
        /*ArrayList<Place> places=(ArrayList<Place>)getIntent().getSerializableExtra("Places");*/
        TripTimelineAdapter adapter = new TripTimelineAdapter(this, places);
        RecyclerView view=(RecyclerView) findViewById(R.id.recyclerView);
        ivTripStart=(ImageView)findViewById(R.id.ivStart);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
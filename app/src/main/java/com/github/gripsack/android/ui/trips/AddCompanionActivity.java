package com.github.gripsack.android.ui.trips;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.data.model.User;
import com.github.gripsack.android.utils.FirebaseUtil;
import com.github.gripsack.android.utils.MapUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.lucasr.twowayview.TwoWayView;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class AddCompanionActivity extends AppCompatActivity {
    Trip trip;
    private SearchView searchView;
    CompanionListAdapter companionAdapter;
    private ListView lvCompanionList;
    ArrayList<User>companionList;
    private TwoWayView lvAddedCompanions;
    AddedCompanionImageAdapter addedCompanionImageAdapter;
    ArrayList<User>addedCompanionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_companion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Companion");
        trip=new Trip();
        trip=(Trip) Parcels.unwrap(getIntent()
                .getParcelableExtra("Trip"));

        lvCompanionList=(ListView)findViewById(R.id.lvCompanionList);
        lvAddedCompanions=(TwoWayView)findViewById(R.id.lvAddedCompanions);

        companionList=new ArrayList<User>();
        addedCompanionList=new ArrayList<User>();

        //Add current user  first

        DatabaseReference mAddedCompanionRef= FirebaseUtil.getTripsRef().child(trip.getTripId()).child("companion");
        mAddedCompanionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String value = postSnapshot.getKey();
                    FirebaseUtil.getUsersRef().child(value).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            boolean added=false;
                            for(User currentUser:addedCompanionList){
                                if(currentUser.userId.equals(user.userId)){
                                    added=true;
                                }
                            }
                            if(added==false && !FirebaseUtil.getCurrentUserId().equals(user.userId))
                               addedCompanionList.add(user);
                        }
                        @Override public void onCancelled(DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        addedCompanionImageAdapter=new AddedCompanionImageAdapter(this,addedCompanionList);
        lvAddedCompanions.setAdapter(addedCompanionImageAdapter);

        String userRef=FirebaseUtil.getCurrentUserId().toString().trim();
        DatabaseReference mCompanionsRef= FirebaseUtil.getCompanionsRef().child(userRef);
        mCompanionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String value = postSnapshot.getKey();
                    FirebaseUtil.getUsersRef().child(value).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            companionList.add(user);
                        }
                        @Override public void onCancelled(DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        lvCompanionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                User selectedFromList =(User)lvCompanionList.getItemAtPosition(position);
                boolean added=false;
                for(User user:addedCompanionList){
                    if(user.displayName.equals(selectedFromList.displayName)){
                        added=true;
                    }
                }
                if(added==false) {
                    //TODO:Add trip to collaborator's list
                   // FirebaseUtil.getUsersRef().child(selectedFromList.userId).child("trips").child(trip.getTripId()).setValue(true);
                    FirebaseUtil.getTripsRef().child(trip.getTripId()).child("companion").child(selectedFromList.userId).setValue(true);
                    addedCompanionList.add(selectedFromList);
                    addedCompanionImageAdapter.notifyDataSetChanged();
                }
            }});

        companionAdapter=new CompanionListAdapter(this,companionList);
        lvCompanionList.setAdapter(companionAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_companions, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search Companion...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                companionAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }
}

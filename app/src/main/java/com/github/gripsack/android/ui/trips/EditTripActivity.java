
package com.github.gripsack.android.ui.trips;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.utils.MapUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTripActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Place searchedLocation;
    private ArrayList<Place> suggestedPlaces;
    private ArrayList<LatLng> placesCoordinates;
    private Trip trip;

    @BindView(R.id.tvTripName)
    TextView tvTripName;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        suggestedPlaces=new ArrayList<Place>();
        searchedLocation=new Place();

        getSuggestedPlaces();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO:Set collaborators and destinations
               //TODO:Update Trip  updateTrip(trip);

                Intent intent=new Intent(EditTripActivity.this,DisplayTripActivity.class);
                startActivity(intent);

            }
        });

    }

    private void updateTrip(Trip trip){
        //DB part
    }

    //TODO:Dummy Data for now Get data from suggestedPlaces
    private  void getSuggestedPlaces(){
        placesCoordinates=new ArrayList<LatLng>();
        LatLng point = new LatLng(37.819929, -122.478255);
        placesCoordinates.add(point);

        point = new LatLng(37.794138, -122.407791);
        placesCoordinates.add(point);

        point = new LatLng(37.810474, -122.366592);
        placesCoordinates.add(point);

        point = new LatLng(37.769493, -122.486229);
        placesCoordinates.add(point);

        point = new LatLng(37.802189, -122.418766);
        placesCoordinates.add(point);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        //TODO:Search Destination
        LatLng sf = new LatLng(37.773972, -122.431297); //San Francisco

        //Suggested Destinations
        for (int i=0;i<placesCoordinates.size();i++){

            mMap.addMarker(new MarkerOptions().position(placesCoordinates.get(i)).title("Marker")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.green_dot)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(placesCoordinates.get(i)));
        }

        mMap.addMarker(new MarkerOptions().position(sf).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_orange)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sf));
        placesCoordinates.add(sf);

        MapUtil.focusPoints(placesCoordinates,mMap);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent=new Intent(EditTripActivity.this,EditDestinationActivity.class);
       // intent.putExtra("SuggestedPlaces", Parcels.wrap(suggestedPlaces));
       // intent.putExtra("SearchedLocation", Parcels.wrap(searchedLocation));
        startActivity(intent);
    }
}

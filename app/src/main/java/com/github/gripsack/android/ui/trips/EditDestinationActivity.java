package com.github.gripsack.android.ui.trips;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.utils.MapUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EditDestinationActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;
    private ArrayList<Place> suggestedPlaces;
    private Place searchedLocation;
    private ArrayList<LatLng> placesCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_destination);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //suggestedPlaces = Parcels.unwrap(savedInstanceState.getParcelable("SuggestedPlaces"));
       // searchedLocation= Parcels.unwrap(getIntent().getParcelableExtra("SearchedLocation"));
        getSuggestedPlaces();
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
        mMap.setOnMapLongClickListener(this);

        //TODO:Search Destination
        //LatLng destination = new LatLng(searchedLocation.getLatitude(), searchedLocation.getLongitude()); //San Francisco
        LatLng sf = new LatLng(37.773972, -122.431297);

        //Suggested Destinations
        for (int i=0;i<placesCoordinates.size();i++){

            mMap.addMarker(new MarkerOptions().position(placesCoordinates.get(i)).title("")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.green_dot)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(placesCoordinates.get(i)));
        }

        mMap.addMarker(new MarkerOptions().position(sf).title("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_orange)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sf));
        placesCoordinates.add(sf);

        mMap.setOnMarkerClickListener(this);
        MapUtil.focusPoints(placesCoordinates,mMap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showSelectedItemForPoint(marker);
        return false;
    }

    private void showSelectedItemForPoint(final Marker marker) {

        View messageView = LayoutInflater.from(this).
                inflate(R.layout.explore_grid_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(messageView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        ImageButton btnDelete = ((ImageButton) messageView.findViewById(R.id.btnDelete));
        btnDelete.setVisibility(View.VISIBLE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.remove();
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_orange)));

    }
}

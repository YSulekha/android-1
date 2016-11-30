
package com.github.gripsack.android.ui.trips;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.ui.MainActivity;
import com.github.gripsack.android.ui.companions.CompanionsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTripActivity extends AppCompatActivity
        implements OnMapReadyCallback,View.OnClickListener{//,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Trip trip;
    int HOTEL_PICKER_REQUEST = 1;

    @BindView(R.id.tvTripName)
    TextView tvTripName;
    @BindView(R.id.lyLocation)
    LinearLayout lyLocation;
    @BindView(R.id.lyCompanion)
    LinearLayout lyCompanion;
    @BindView(R.id.lyHotel)
    LinearLayout lyHotel;
    @BindView(R.id.lyPhotos)
    LinearLayout lyPhotos;
    @BindView(R.id.tvDone)
    TextView tvDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        trip=new Trip();
        trip=(Trip) Parcels.unwrap(getIntent()
                .getParcelableExtra("Trip"));

        tvTripName.setText(trip.getTripName());
        lyCompanion.setOnClickListener(this);
        lyHotel.setOnClickListener(this);
        lyLocation.setOnClickListener(this);
        lyPhotos.setOnClickListener(this);

        tvDone.setOnClickListener(this);

    }

    private void updateTrip(Trip trip){
        //DB part
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng destination = new LatLng(trip.getSearchDestination().getLatitude(), trip.getSearchDestination().getLongitude());

        mMap.addMarker(new MarkerOptions().position(destination).title(trip.getSearchDestination().getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng latLng=new LatLng((destination.latitude+0.05),destination.longitude+0.05);
        builder.include(destination);
        builder.include(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                builder.build(), 300, 300, 0));
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.lyLocation:
                Intent intentLocation=new Intent(this, EditDestinationActivity.class)
                        .putExtra("Trip", Parcels.wrap(trip));
                startActivity(intentLocation);
                break;

            case R.id.lyCompanion:
                Intent intentCompanion=new Intent(this, CompanionsActivity.class);
                startActivity(intentCompanion);
                break;

            case R.id.lyHotel:
                Uri gmmIntentUri = Uri.parse("geo:"+trip.getSearchDestination().getLatitude()+","+trip.getSearchDestination().getLongitude()
                        +"?q=hotels");
                Intent intentHotel=new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                intentHotel.setPackage("com.google.android.apps.maps");
                startActivityForResult(intentHotel,HOTEL_PICKER_REQUEST);
                break;

            case R.id.lyPhotos:
                Intent intentPhotos=new Intent(this,AddPhotoActivity.class)
                        .putExtra("Trip", Parcels.wrap(trip));;
                startActivity(intentPhotos);
                break;
            case R.id.tvDone:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
        }

    }
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HOTEL_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

            }
        }
    }*/
}

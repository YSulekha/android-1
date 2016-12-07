package com.github.gripsack.android.ui.trips;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gripsack.android.BuildConfig;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.ui.navigation.DrawerItemSelectedListener;
import com.github.gripsack.android.ui.places.BucketlistFragment;
import com.github.gripsack.android.utils.FirebaseUtil;
import com.github.gripsack.android.utils.MapUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class EditDestinationActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener{

    int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;
    private ArrayList<Place> tripPlaces;
    private Trip trip;
    private BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.btnBucketList)
    Button btnBucketList;
    /*@BindView(R.id.btnLikedList)
    Button btnLikedList;*/
    @BindView(R.id.btnVisitedList)
    Button btnVisitedList;
    @BindView(R.id.btnRecommendedList)
    Button btnRecommendedList;
    @BindView(R.id.tvDone)
    TextView tvDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_destination);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View bottomSheet = findViewById(R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(200);

        trip=new Trip();
        trip=(Trip) Parcels.unwrap(getIntent()
                .getParcelableExtra("Trip"));


        tripPlaces = (ArrayList<Place>)getIntent().getSerializableExtra("Destinations");

        btnBucketList.setOnClickListener(this);
        btnRecommendedList.setOnClickListener(this);
       // btnLikedList.setOnClickListener(this);
        btnVisitedList.setOnClickListener(this);
        tvDone.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removePlace(Marker marker) {

        for (Place place : tripPlaces) {
            if (place.getLatitude() == marker.getPosition().latitude
                    && place.getLongitude() == marker.getPosition().longitude) {
                tripPlaces.remove(place);
                //TODO:Remove places from DB
            }
        }
        marker.remove();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        LatLng destination = new LatLng(trip.getSearchDestination().getLatitude(), trip.getSearchDestination().getLongitude());
        addMarker(trip.getSearchDestination().getName(),destination);
        for (Place place:tripPlaces){
            destination = new LatLng(place.getLatitude(), place.getLongitude());
            addMarker(place.getName(),destination);
        }
        tripPlaces.add(trip.getSearchDestination());
        MapUtil.focusPoints(tripPlaces,mMap);
    }

    private void addMarker(String name,LatLng destination){
        mMap.addMarker(new MarkerOptions().position(destination).title(name)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showSelectedItemForPoint(marker);
        return false;
    }

    private void showSelectedItemForPoint(final Marker marker) {

        View messageView = LayoutInflater.from(this).
                inflate(R.layout.item_pin, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(messageView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button btnUnpin = (Button) messageView.findViewById(R.id.btnUnpin);
        btnUnpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlace(marker);
                alertDialog.cancel();
            }
        });

        Button btnNavigate = (Button) messageView.findViewById(R.id.btnNavigate);
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Place place:tripPlaces) {
                    if (place.getLatitude()==marker.getPosition().latitude
                            && place.getLongitude()==marker.getPosition().longitude){
                        String [] list;
                        list=place.getName().split(" ");
                        String url="google.navigation:q="+list[0];
                        for (int i=1;i<list.length;i++){
                            url=url+"+"+list[i];
                        }

                        Uri gmmIntentUri=Uri.parse(url);
                        Intent mapIntent=new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }

            }
        });

        Button btnPanorama = (Button) messageView.findViewById(R.id.btnPanorama);
        btnPanorama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri=Uri.parse("google.streetview:cbll="+marker.getPosition().latitude+","+marker.getPosition().longitude);
                Intent mapIntent=new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                startActivity(mapIntent);
            }
        });

        alertDialog.show();
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            LatLng latLng2=new LatLng((latLng.latitude+0.05),latLng.longitude+0.05);
            LatLngBounds bounds = new LatLngBounds(latLng,latLng);
            builder.setLatLngBounds(bounds);
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        Intent intent;
        switch (id){
            case R.id.btnBucketList:
                break;
            /*case R.id.btnLikedList:
                break;*/
            case R.id.btnVisitedList:
                break;
            case R.id.btnRecommendedList:
                break;
            case R.id.tvDone:
                intent=new Intent(this,EditTripActivity.class)
                        .putExtra("Trip", Parcels.wrap(trip));
                startActivity(intent);
                finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                addMarker(place.getName().toString(),place.getLatLng());
                sendRequest(place.getId());
            }
        }
    }

    public void sendRequest(String placeId) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String apiKey = BuildConfig.MyPlacesApiKey;
        params.put("placeid", placeId);
        params.put("key", apiKey);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Place place= null;
                try {
                    place = Place.fromJSONObject(response.getJSONObject("result"));
                    FirebaseUtil.saveDestination(place,trip.getTripId());
                    tripPlaces.add(place);
                    Toast.makeText(EditDestinationActivity.this, place.getName(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });
    }

}

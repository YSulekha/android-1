package com.github.gripsack.android.ui.trips;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.data.model.TripTypes;
import com.github.gripsack.android.utils.MapUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatePickerDialog dpBeginDate;
    private SimpleDateFormat dateFormatter;
    private Place searchedPlace;
    private GoogleMap mMap;

    @BindView(R.id.tvTripName)
    TextView tvTripName;
    @BindView(R.id.etBeginDate)
    EditText etBeginDate;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.cbAdventure)
    CheckBox cbAdventure;
    @BindView(R.id.cbCityBreak)
    CheckBox cbCityBreak;
    @BindView(R.id.cbCulinary)
    CheckBox cbCulinary;
    @BindView(R.id.cbDisappear)
    CheckBox cbDisappear;
    @BindView(R.id.cbCulture)
    CheckBox cbCulture;
    @BindView(R.id.cbRomantic)
    CheckBox cbRomantic;
    @BindView(R.id.cbRelaxing)
    CheckBox cbRelaxing;
    @BindView(R.id.cbWithFamily)
    CheckBox cbWithFamily;
    @BindView(R.id.cbWithFriends)
    CheckBox cbWithFriends;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.tvSearchedPlaceName)
    TextView tvSearchedPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        searchedPlace= new Place();

        /*TODO:To test, It will open*/
        /*searchedPlace.setLatitude(37.773972);
        searchedPlace.setLongitude(-122.431297);
        searchedPlace.setName("San Francisco");
        searchedPlace.setRating(4);*/

        searchedPlace=(Place) Parcels.unwrap(getIntent()
                .getParcelableExtra("SearchedLocation"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Glide.with(this).load(searchedPlace.getPhotoUrl()).into(toolbarImage);

        tvSearchedPlaceName.setText(searchedPlace.getName());
       // tvSearchedRating.setText(String.valueOf(searchedPlace.getRating()));

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        etBeginDate=(EditText)findViewById(R.id.etBeginDate);
        etBeginDate.setInputType(InputType.TYPE_NULL);
        setDateTimeField();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip trip=new Trip();
                trip.setBeginDate(etBeginDate.getText().toString());
                trip.setSearchDestination(searchedPlace);
                trip.setTripName(tvTripName.getText().toString());
                ArrayList<Integer> tripTypes=getTripTypes();
                trip.setTripTypes(tripTypes);

                saveTrip(trip);

                Intent intent=new Intent(AddTripActivity.this,EditTripActivity.class)
                        .putExtra("Trip", Parcels.wrap(trip));
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    //Get user's trip type
    private ArrayList<Integer> getTripTypes(){
        ArrayList<Integer> types=new ArrayList<Integer>();

        if(cbAdventure.isChecked())
            types.add(TripTypes.ADVENTURE);

        if (cbCityBreak.isChecked())
            types.add(TripTypes.CITY_BREAK);

        if(cbCulinary.isChecked())
            types.add(TripTypes.CULINARY);

        if(cbCulture.isChecked())
            types.add(TripTypes.CULTURE);

        if (cbDisappear.isChecked())
            types.add(TripTypes.DISAPPEAR);

        if(cbRelaxing.isChecked())
            types.add(TripTypes.RELAXING);

        if (cbRomantic.isChecked())
            types.add(TripTypes.ROMANTIC);

        if(cbWithFamily.isChecked())
            types.add(TripTypes.WITH_FAMILY);

        if (cbWithFriends.isChecked())
            types.add(TripTypes.WITH_FRIENDS);

        return types;

    }

    private void saveTrip(Trip trip){
        //TODO:DB Part
    }

    //Set dialog calendar to beginDate
    private void setDateTimeField() {

        etBeginDate.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(view == etBeginDate) {
                    dpBeginDate.show();
                }
                return true;
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        dpBeginDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etBeginDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng destination = new LatLng(searchedPlace.getLatitude(), searchedPlace.getLongitude());

        mMap.addMarker(new MarkerOptions().position(destination).title(searchedPlace.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng latLng=new LatLng((destination.latitude+0.05),destination.longitude+0.05);
        builder.include(destination);
        builder.include(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                builder.build(), 300, 300, 0));
    }
}

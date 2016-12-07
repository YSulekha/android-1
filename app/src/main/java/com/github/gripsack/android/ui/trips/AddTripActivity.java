package com.github.gripsack.android.ui.trips;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.data.model.TripTypes;
import com.github.gripsack.android.ui.companions.CompanionsActivity;
import com.github.gripsack.android.utils.FirebaseUtil;
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

public class AddTripActivity extends AppCompatActivity implements OnMapReadyCallback ,View.OnClickListener{

    private DatePickerDialog dpBeginDate;
    private DatePickerDialog dpEndDate;
    private SimpleDateFormat dateFormatter;
    private Place searchedPlace;
    private GoogleMap mMap;
    private Vibrator vib;
    Animation animShake;

    @BindView(R.id.etTripName)
    TextView etTripName;
    @BindView(R.id.etBeginDate)
    EditText etBeginDate;
    @BindView(R.id.etEndDate)
    EditText etEndDate;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.ibAdventure)
    ImageButton ibAdventure;
    @BindView(R.id.ibCityBreak)
    ImageButton ibCityBreak;
    @BindView(R.id.ibCulinary)
    ImageButton ibCulinary;
    @BindView(R.id.ibDisappear)
    ImageButton ibDisappear;
    @BindView(R.id.ibCulture)
    ImageButton ibCulture;
    @BindView(R.id.ibRomantic)
    ImageButton ibRomantic;
    @BindView(R.id.ibRelaxing)
    ImageButton ibRelaxing;
    @BindView(R.id.ibWithFamily)
    ImageButton ibWithFamily;
    @BindView(R.id.ibWithFriends)
    ImageButton ibWithFriends;
    @BindView(R.id.toolbarImage)
    KenBurnsView toolbarImage;
    @BindView(R.id.tvSearchedPlaceName)
    TextView tvSearchedPlaceName;
    @BindView(R.id.lyBeginDate)
    TextInputLayout lyBeginDate ;
    @BindView(R.id.lyTripName)
    TextInputLayout lyTripName;
    @BindView(R.id.ivAdventure)
    ImageView ivAdventure;
    @BindView(R.id.ivCityBreak)
    ImageView ivCityBreak;
    @BindView(R.id.ivCulture)
    ImageView ivCulture;
    @BindView(R.id.ivDisappear)
    ImageView ivDisappear;
    @BindView(R.id.ivRelaxing)
    ImageView ivRelaxing;
    @BindView(R.id.ivRomantic)
    ImageView ivRomantic;
    @BindView(R.id.ivWithFamily)
    ImageView ivWithFamily;
    @BindView(R.id.ivWithFriends)
    ImageView ivWithFriends;
    @BindView(R.id.ivCulinary)
    ImageView ivCulinary;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddTripActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchedPlace = new Place();
        searchedPlace=(Place) Parcels.unwrap(getIntent()
                .getParcelableExtra("SearchedLocation"));
        tvSearchedPlaceName.setText(searchedPlace.getName());

        Glide.with(this).load(searchedPlace.getPhotoUrl()).into(toolbarImage);

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        etBeginDate.setInputType(InputType.TYPE_NULL);
        etEndDate.setInputType(InputType.TYPE_NULL);
        setDateTimeField();

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setListeners();
    }

    private void setListeners(){
        btnSave.setOnClickListener(this);
        ibAdventure.setOnClickListener(this);
        ibCityBreak.setOnClickListener(this);
        ibCulinary.setOnClickListener(this);
        ibCulture.setOnClickListener(this);
        ibDisappear.setOnClickListener(this);
        ibRelaxing.setOnClickListener(this);
        ibRomantic.setOnClickListener(this);
        ibWithFamily.setOnClickListener(this);
        ibWithFriends.setOnClickListener(this);
        ivAdventure.setOnClickListener(this);
        ivCityBreak.setOnClickListener(this);
        ivCulinary.setOnClickListener(this);
        ivCulture.setOnClickListener(this);
        ivDisappear.setOnClickListener(this);
        ivRelaxing.setOnClickListener(this);
        ivRomantic.setOnClickListener(this);
        ivWithFamily.setOnClickListener(this);
        ivWithFriends.setOnClickListener(this);
    }

    //Get user's trip type
    private ArrayList<Integer> getTripTypes(){
        ArrayList<Integer> types=new ArrayList<Integer>();

        if(ibAdventure.getVisibility()==View.VISIBLE)
            types.add(TripTypes.ADVENTURE);

        if (ibCityBreak.getVisibility()==View.VISIBLE)
            types.add(TripTypes.CITY_BREAK);

        if(ibCulinary.getVisibility()==View.VISIBLE)
            types.add(TripTypes.CULINARY);

        if(ibCulture.getVisibility()==View.VISIBLE)
            types.add(TripTypes.CULTURE);

        if (ibDisappear.getVisibility()==View.VISIBLE)
            types.add(TripTypes.DISAPPEAR);

        if(ibRelaxing.getVisibility()==View.VISIBLE)
            types.add(TripTypes.RELAXING);

        if (ibRomantic.getVisibility()==View.VISIBLE)
            types.add(TripTypes.ROMANTIC);

        if(ibWithFamily.getVisibility()==View.VISIBLE)
            types.add(TripTypes.WITH_FAMILY);

        if (ibWithFriends.getVisibility()==View.VISIBLE)
            types.add(TripTypes.WITH_FRIENDS);

        return types;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Set dialog calendar to beginDate
    private void setDateTimeField() {

        etBeginDate.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(view == etBeginDate) {
                    dpBeginDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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

        etEndDate.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(view == etEndDate) {
                    dpEndDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dpEndDate.show();
                }
                return true;
            }
        });

        dpEndDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etEndDate.setText(dateFormatter.format(newDate.getTime()));
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
                builder.build(), 20, 20, 0));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnSave:
                if(checkMandatoryFields()==true) {
                    Trip trip = new Trip();
                    trip.setBeginDate(etBeginDate.getText().toString());
                    trip.setEndDate(etEndDate.getText().toString());
                    trip.setSearchDestination(searchedPlace);
                    trip.setTripName(etBeginDate.getText().toString());
                    ArrayList<Integer> tripTypes = getTripTypes();
                    trip.setTripTypes(tripTypes);

                    FirebaseUtil.saveTrip(trip);

                    Intent intent = new Intent(AddTripActivity.this, EditTripActivity.class)
                            .putExtra("Trip", Parcels.wrap(trip));
                    startActivity(intent);
                }
                break;

            case R.id.ivAdventure:
                ibAdventure.setVisibility(View.VISIBLE);
                break;

            case R.id.ivCityBreak:
                ibCityBreak.setVisibility(View.VISIBLE);
                break;

            case R.id.ivCulinary:
                ibCulinary.setVisibility(View.VISIBLE);
                break;

            case R.id.ivCulture:
                ibCulture.setVisibility(View.VISIBLE);
                break;

            case R.id.ivDisappear:
                ibDisappear.setVisibility(View.VISIBLE);
                break;

            case R.id.ivRelaxing:
                ibRelaxing.setVisibility(View.VISIBLE);
                break;

            case R.id.ivRomantic:
                ibRomantic.setVisibility(View.VISIBLE);
                break;

            case R.id.ivWithFamily:
                ibWithFamily.setVisibility(View.VISIBLE);
                break;

            case R.id.ivWithFriends:
                ibWithFriends.setVisibility(View.VISIBLE);
                break;

            case R.id.ibAdventure:
                ibAdventure.setVisibility(View.GONE);
                break;

            case R.id.ibCityBreak:
                ibCityBreak.setVisibility(View.GONE);
                break;

            case R.id.ibCulinary:
                ibCulinary.setVisibility(View.GONE);
                break;

            case R.id.ibCulture:
                ibCulture.setVisibility(View.GONE);
                break;

            case R.id.ibDisappear:
                ibDisappear.setVisibility(View.GONE);
                break;

            case R.id.ibRelaxing:
                ibRelaxing.setVisibility(View.GONE);
                break;

            case R.id.ibRomantic:
                ibRomantic.setVisibility(View.GONE);
                break;

            case R.id.ibWithFamily:
                ibWithFamily.setVisibility(View.GONE);
                break;

            case R.id.ibWithFriends:
                ibWithFriends.setVisibility(View.GONE);
                break;

        }
    }

    private boolean checkMandatoryFields(){
        if (!checkTripName()) {
            etTripName.setAnimation(animShake);
            etTripName.startAnimation(animShake);
            vib.vibrate(120);
            return false;
        }
        if (!checkBeginDate()) {
            etBeginDate.setAnimation(animShake);
            etBeginDate.startAnimation(animShake);
            vib.vibrate(120);
            return false;
        }

        lyBeginDate.setErrorEnabled(false);
        lyTripName.setErrorEnabled(false);
        return true;
    }

    private boolean checkTripName(){
        if (etTripName.getText().toString().trim().isEmpty()) {
            lyTripName.setErrorEnabled(true);
            lyTripName.setError("Please enter trip name");
            etTripName.setError("Please enter trip name");
            return false;
        }
        lyTripName.setErrorEnabled(false);
        return true;
    }

    private boolean checkBeginDate(){
        if (etBeginDate.getText().toString().trim().isEmpty()) {
            lyBeginDate.setErrorEnabled(true);
            lyBeginDate.setError("Please enter begin date");
            etBeginDate.setError("Please enter begin date");
            return false;
        }
        lyBeginDate.setErrorEnabled(false);
        return true;
    }
}

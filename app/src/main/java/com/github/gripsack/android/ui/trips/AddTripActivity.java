package com.github.gripsack.android.ui.trips;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.data.model.TripTypes;
import com.github.gripsack.android.utils.FirebaseUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripActivity extends AppCompatActivity {

    DatePickerDialog dpBeginDate;
    SimpleDateFormat dateFormatter;

    @BindView(R.id.tvTripName)
    TextView tvTripName;
    @BindView(R.id.etBeginDate)
    EditText etBeginDate;
    @BindView(R.id.tvDestination)
    TextView tvDestination;
    @BindView(R.id.fab)
    FloatingActionButton fab;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO:Get from Search Activity
        Place destination=new Place();

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        etBeginDate=(EditText)findViewById(R.id.etBeginDate);
        etBeginDate.setInputType(InputType.TYPE_NULL);
        setDateTimeField();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip trip=new Trip();
                trip.setBeginDate(etBeginDate.getText().toString());
                trip.setSearchDestination(destination);
                trip.setTripName(tvTripName.getText().toString());
                ArrayList<Integer> tripTypes=getTripTypes();
                trip.setTripTypes(tripTypes);
                FirebaseUtil.saveTrip(trip);
                Intent intent=new Intent(AddTripActivity.this,EditTripActivity.class);
                startActivity(intent);
            }
        });
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
}

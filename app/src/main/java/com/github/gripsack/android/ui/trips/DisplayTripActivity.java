package com.github.gripsack.android.ui.trips;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.DirectionsJSONParser;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayTripActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

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
        //searchedLocation= Parcels.unwrap(getIntent().getParcelableExtra("SearchedLocation"));
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

        //TODO:Search Destination
        //LatLng destination = new LatLng(searchedLocation.getLatitude(), searchedLocation.getLongitude()); //San Francisco
        LatLng sf = new LatLng(37.773972, -122.431297);
        //Suggested Destinations
        for (int i=0;i<placesCoordinates.size();i++){

            mMap.addMarker(new MarkerOptions().position(placesCoordinates.get(i)).title("Marker")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.green_dot)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(placesCoordinates.get(i)));
        }

        mMap.addMarker(new MarkerOptions().position(sf).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sf));
        placesCoordinates.add(sf);

        //TODO:
        if (placesCoordinates.size() >= 2) {
            LatLng origin = (LatLng) placesCoordinates.get(0);
            LatLng dest = placesCoordinates.get(1);

            //Getting URL to the Google Directions API
            String url = MapUtil.getDirectionsUrl(origin, dest,placesCoordinates);

            DownloadTask downloadTask = new DownloadTask();

            //Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

        // MapUtil.focusPoints(placesCoordinates,mMap);
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

        alertDialog.show();
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = MapUtil.downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DisplayTripActivity.ParserTask parserTask = new DisplayTripActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            // Traversing through all the routes
            MapUtil.traverseMarkers(result,mMap);
        }
    }

}

package com.github.gripsack.android.utils;

import android.graphics.Color;
import android.util.Log;

import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tuze on 11/19/16.
 */

public class MapUtil {
    public static void focusPoints(ArrayList<Place> places, GoogleMap map){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Place place:places) {
            LatLng marker=new LatLng(place.getLatitude(),place.getLongitude());
            builder.include(marker);
        }

        if(places.size()==1){
            LatLng latLng=new LatLng((places.get(0).getLatitude()+0.05),places.get(0).getLongitude()+0.05);
            builder.include(latLng);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                builder.build(), 300, 300, 0));
    }

    public static String getDirectionsUrl(LatLng origin,LatLng dest,ArrayList<LatLng> markers){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markers.size();i++){
            LatLng point = (LatLng) markers.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("while downloading url", e.toString());

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public static void traverseMarkers(List<List<HashMap<String, String>>> result, GoogleMap map){
        ArrayList points = null;
        PolylineOptions lineOptions = null;

        for(int i=0;i<result.size();i++){
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j <path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(R.color.colorPrimary);
        }

        // Drawing polyline in the Google Map for the i-th route
        map.addPolyline(lineOptions);
    }
}

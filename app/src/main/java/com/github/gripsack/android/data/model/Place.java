package com.github.gripsack.android.data.model;

import com.github.gripsack.android.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//Model for the data retrieved from places API
public class Place {
    private double latitude;
    private double longitude;
    private String name;
    private String placeid;
    private ArrayList<String> types = new ArrayList<>();
    private float rating;
    private String photoUrl;
    private String vicinity;

    public String getVicinity() {
        return vicinity;
    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getPlaceid() {
        return placeid;
    }

    /*public ArrayList<String> getTypes() {
       // return types;
    }*/

    public float getRating() {
        return rating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }


    public static Place fromJSONObject(JSONObject jsonObject) {
        Place place = new Place();

        String apiKey = BuildConfig.MyPlacesApiKey;
        String ph_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key="+apiKey;
        //   String ph_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyCwFMCAokRRODC9bScVMgDMWYIrVTK641k";
        try {
            JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");
            place.latitude = location.getDouble("lat");
            place.longitude = location.getDouble("lng");
            place.name = jsonObject.getString("name");
            place.placeid = jsonObject.getString("place_id");
            if (jsonObject.has("rating")) {
                place.rating = (float) jsonObject.getDouble("rating");
            }
            if(jsonObject.has("vicinity")){
                place.vicinity = jsonObject.getString("vicinity");
            }
         /*  JSONArray types = jsonObject.getJSONArray("types");
            if(types.length()>0){
                for(int i=0;i<types.length();i++){
                    place.types.add(types.getString(i));
                }
            }*/
            if (jsonObject.has("photos") && jsonObject.getJSONArray("photos").length() > 0) {
                JSONArray photos = jsonObject.getJSONArray("photos");
                JSONObject p = photos.getJSONObject(0);
                String photoreference = p.getString("photo_reference");
                ph_url = ph_url + "&photoreference=" + photoreference;
                place.photoUrl = ph_url;
            } else {
                place.photoUrl = jsonObject.getString("icon");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    public static ArrayList<Place> fromJSONArray(JSONArray result) {
        ArrayList<Place> places = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            JSONObject tweetObject = null;
            try {
                tweetObject = result.getJSONObject(i);
            } catch (JSONException e) {
                continue;
            }
            Place tweet = fromJSONObject(tweetObject);
            places.add(tweet);
        }
        return places;
    }
}

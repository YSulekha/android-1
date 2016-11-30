
package com.github.gripsack.android.ui.explore;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.gripsack.android.BuildConfig;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.services.LocationService;
import com.github.gripsack.android.ui.destinations.DestinationsActivity;
import com.github.gripsack.android.ui.destinations.DestinationsFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ExploreFragment extends Fragment {
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final String TAG = "ExploreFragment";
    com.google.android.gms.location.places.Place searchplace;
    ArrayList<Place> places;

    ExploreRecyclerAdapter ad;

    //List of popular destinations
    String[] placesName = {"San Francisco", "New York", "Seattle", "Sydney", "Agra", "Abu Dhabi",
            "Toronto", "Paris", "Italy", "Chicago", "Shangai", "Montreal", "Kaula Lampur"};

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        if(savedInstanceState==null) {
            places = new ArrayList<>();
        }
        Intent intent = new Intent(getActivity(), LocationService.class);

        //Check if there is a permission to access location
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //else request permission to access location
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);

        }
        getActivity().startService(intent);

     /*   EditText search = (EditText)getActivity().findViewById(R.id.toolbarText);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAutoCompleteActivity();
            }
        });*/

        RecyclerView view = (RecyclerView) rootView.findViewById(R.id.destination_recycler);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        ad = new ExploreRecyclerAdapter(getActivity(), places);
        view.setAdapter(ad);


     /*  mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), (BaseActivity) getActivity())
                .build();*/


        if(savedInstanceState == null){
            sendrequest();
        }
        return rootView;
    }

    public void sendrequest() {
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
        String apiKey = BuildConfig.MyPlacesApiKey;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", apiKey);
        for (int i = 0; i < placesName.length; i++) {
            String query = placesName[i];
            params.put("query", query);
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                    // Handle resulting parsed JSON response here
                    try {
                        JSONArray resultsArray = response.getJSONArray("results");
                        places.addAll(Place.fromJSONArray(resultsArray));
                        ad.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("hhh",response.toString());
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Timber.e(t);
                }
            });
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_explore, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            createAutoCompleteActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createAutoCompleteActivity() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                searchplace = PlaceAutocomplete.getPlace(getActivity(), data);
                Place place = new Place();
                place.setLatitude(searchplace.getLatLng().latitude);
                place.setLongitude(searchplace.getLatLng().longitude);
                place.setName((String) searchplace.getName());
                place.setPlaceid(searchplace.getId());
                place.setRating(searchplace.getRating());

                Intent intent = new Intent(getActivity(), DestinationsActivity.class);
                intent.putExtra(DestinationsFragment.EXTRA_PLACE,Parcels.wrap(place));
                //String latLong = searchplace.getLatLng().latitude + "," + searchplace.getLatLng().longitude;
                //intent.putExtra("latLong", latLong);
                startActivity(intent);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.v(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}

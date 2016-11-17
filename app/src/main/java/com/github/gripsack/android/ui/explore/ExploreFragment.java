package com.github.gripsack.android.ui.explore;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ExploreFragment extends Fragment {

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final String TAG = "ExploreFragment";
    Place searchplace;
    RecyclerView recyclerView;
    ExploreRecyclerAdapter placesAdapter;
    ArrayList<com.github.gripsack.android.data.model.Place> placesList;
    public static ArrayList<com.github.gripsack.android.data.model.Place> bucketList = new ArrayList<>();
    public static ArrayList<com.github.gripsack.android.data.model.Place> likeList = new ArrayList<>();
    public static ArrayList<com.github.gripsack.android.data.model.Place> addToTripList = new ArrayList<>();

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
        placesList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler);
        placesAdapter = new ExploreRecyclerAdapter(getActivity(), placesList);
        recyclerView.setAdapter(placesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return rootView;
    }

    public void sendrequest() {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String apiKey = BuildConfig.MyPlacesApiKey;
        params.put("location", searchplace.getLatLng().latitude + "," + searchplace.getLatLng().longitude);
        params.put("rankby", "prominence");
        params.put("key", apiKey);
        Log.v("sdd", params.toString());
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray resultsArray = response.getJSONArray("results");
                    placesList.addAll(com.github.gripsack.android.data.model.Place.fromJSONArray(resultsArray));
                    placesAdapter.notifyDataSetChanged();
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
                placesList.clear();
                sendrequest();
                Log.v(TAG, "Place: " + searchplace.getName() + searchplace.getLatLng());
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
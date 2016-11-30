package com.github.gripsack.android.ui.destinations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class DestinationsFragment extends Fragment {

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final String TAG = "DestinationsFragment";
    public static final String EXTRA_PLACE = "place";

    Place searchplace;
    String latLong;
    RecyclerView recyclerView;
    DestinationsAdapter placesAdapter;
    ArrayList<com.github.gripsack.android.data.model.Place> placesList;

    CollapsingToolbarLayout mCollapsing;

    ImageView imageView;
    String photoURl;
    String name;

    String types = "amusement_park|aquarium|museum|park|zoo|art_gallery";
  //  String[] type = {"amusement_park", "aquarium", "museum", "park", "zoo", "art_gallery"};


    public DestinationsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_destinations_bac, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsing = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingbar);

        placesList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler);
        placesAdapter = new DestinationsAdapter(getActivity(), placesList);
        recyclerView.setAdapter(placesAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        imageView = (ImageView) rootView.findViewById(R.id.photoCollapse);

        Intent intent = getActivity().getIntent();
        com.github.gripsack.android.data.model.Place place = (com.github.gripsack.android.data.model.Place) Parcels.unwrap(intent
                .getParcelableExtra(EXTRA_PLACE));
        if(place.getPhotoUrl()!=null) {
            Glide.with(getActivity()).load(place.getPhotoUrl()).into(imageView);
            photoURl=place.getPhotoUrl();
        }
        mCollapsing.setTitle(place.getName());
        latLong = place.getLatitude()+ "," + place.getLongitude();


     /*   latLong = intent.getStringExtra("latLong");
        if(intent.hasExtra("photoUrl")){
     //       photoURl = intent.getStringExtra("photoUrl");
            Glide.with(getActivity()).load(photoURl).into(imageView);
        }
        if(intent.hasExtra("name")){
       //     name = intent.getStringExtra("name");
            mCollapsing.setTitle(name);
        }*/
        sendrequest();
        return rootView;
    }

    public void sendrequest() {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String apiKey = BuildConfig.MyPlacesApiKey;
        params.put("location", latLong);
        params.put("rankby", "prominence");
        params.put("key", apiKey);
        params.put("types",types);
        params.put("radius",5000);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray resultsArray = response.getJSONArray("results");
                    placesList.addAll(com.github.gripsack.android.data.model.Place.fromJSONArray(resultsArray));
                    if(photoURl==null && resultsArray.length() >0) {
                        Glide.with(getActivity()).load(placesList.get(0).getPhotoUrl()).into(imageView);
                    }
                    if(searchplace != null){
                        mCollapsing.setTitle(searchplace.getName());
                    }

                    placesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("response",response.toString());
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
        inflater.inflate(R.menu.menu_destinations, menu);
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
                latLong = searchplace.getLatLng().latitude + "," + searchplace.getLatLng().longitude;
                photoURl=null;
                sendrequest();
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

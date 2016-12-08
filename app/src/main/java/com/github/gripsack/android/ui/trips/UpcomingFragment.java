package com.github.gripsack.android.ui.trips;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.utils.FirebaseUtil;
import com.google.firebase.database.Query;

import timber.log.Timber;

import static com.github.gripsack.android.utils.FirebaseUtil.getCurrentUserId;

public class UpcomingFragment extends Fragment {

    public static final String TAG = "UpcomingFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<TripViewHolder> mAdapter;


    private static final String KEY_LAYOUT_POSITION = "layoutPosition";
    private int mRecyclerViewPosition = 0;

    public static UpcomingFragment newInstance() {
        return new UpcomingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trips_upcoming, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String uid = getCurrentUserId();
        if (uid == null) {
            return;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mRecyclerViewPosition = (int) savedInstanceState
                    .getSerializable(KEY_LAYOUT_POSITION);
            mRecyclerView.scrollToPosition(mRecyclerViewPosition);
            // TODO: RecyclerView only restores position properly for some tabs.
        }

        Timber.d("Restoring recycler view position: %d", mRecyclerViewPosition);
        Query companionsQuery = FirebaseUtil.getCompanionsRef();
        mAdapter = getFirebaseRecyclerAdapter(companionsQuery);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                // TODO: Refresh feed view.
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }


    private FirebaseRecyclerAdapter<Trip, TripViewHolder> getFirebaseRecyclerAdapter(Query query) {

        return new FirebaseIndexRecyclerAdapter<Trip, TripViewHolder>(
                Trip.class,
                R.layout.widget_cardview_trip,
                TripViewHolder.class,
                FirebaseUtil.getCurrentUserTripsRef().orderByChild("beginDate"),
                FirebaseUtil.getTripsRef()) {
            @Override
            protected void populateViewHolder(TripViewHolder viewHolder, Trip model, int position) {
               /* SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                try {
                    Date tripDate = sdf.parse(model.getBeginDate());
                    Date todaysDate=new Date();
                    if(todaysDate.compareTo(tripDate) < 0){
                        viewHolder.removeView();
                    }
                    else {*/
                        String key = this.getRef(position).getKey();
                        Timber.d("position %d key %s", position, key);
                        viewHolder.setName(model.getTripName(), key);
                        viewHolder.setDate(model.getBeginDate(),key);
                        viewHolder.setImage(model,key,getContext());
                  /*  }
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null && mAdapter instanceof FirebaseRecyclerAdapter) {
            ((FirebaseRecyclerAdapter) mAdapter).cleanup();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        int recyclerViewScrollPosition = getRecyclerViewScrollPosition();
        Timber.d("Recycler view scroll position: %d", recyclerViewScrollPosition);
        savedInstanceState.putSerializable(KEY_LAYOUT_POSITION, recyclerViewScrollPosition);
        super.onSaveInstanceState(savedInstanceState);
    }

    private int getRecyclerViewScrollPosition() {
        int scrollPosition = 0;
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        return scrollPosition;
    }

}

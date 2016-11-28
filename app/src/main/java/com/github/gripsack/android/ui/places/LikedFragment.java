package com.github.gripsack.android.ui.places;


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
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.utils.FirebaseUtil;
import com.google.firebase.database.Query;

import timber.log.Timber;

import static com.github.gripsack.android.utils.FirebaseUtil.getCurrentUserId;

public class LikedFragment extends Fragment {


    public static final String TAG = "LikedFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<PlaceViewHolder> mAdapter;


    private static final String KEY_LAYOUT_POSITION = "layoutPosition";
    private int mRecyclerViewPosition = 0;

    public static LikedFragment newInstance() {
        return new LikedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places_liked, container, false);
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

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
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

            public void onChanged() {
                Timber.d("AdapterDataObserver.onChanged");
                // Do nothing
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                Timber.d("AdapterDataObserver.onItemRangeChanged %d %d", positionStart, itemCount);
                // do nothing
            }

            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                // fallback to onItemRangeChanged(positionStart, itemCount) if app
                // does not override this method.
                onItemRangeChanged(positionStart, itemCount);
                Timber.d("AdapterDataObserver.onItemRangeChanged %d %d", positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                // do nothing
                Timber.d("AdapterDataObserver.onItemRangeRemoved %d %d", positionStart, itemCount);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                // do nothing
                Timber.d("AdapterDataObserver.onItemRangeMoved %d %d %d", fromPosition, toPosition, itemCount);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
        }
        super.onDestroyView();
    }

    private FirebaseRecyclerAdapter<Place, PlaceViewHolder> getFirebaseRecyclerAdapter(Query query) {

        return new FirebaseIndexRecyclerAdapter<Place, PlaceViewHolder>(
                Place.class,
                R.layout.widget_cardview_place,
                PlaceViewHolder.class,
                FirebaseUtil.getCurrentUserLikedPlacesRef(),
                FirebaseUtil.getPlacesRef()) {
            @Override
            protected void populateViewHolder(PlaceViewHolder viewHolder, Place model, int position) {
                String key = this.getRef(position).getKey();
                Timber.d("position %d key %s", position, key);
                viewHolder.setName(model.getName(), key);
                viewHolder.setPhoto(model.getPhotoUrl(), key);
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

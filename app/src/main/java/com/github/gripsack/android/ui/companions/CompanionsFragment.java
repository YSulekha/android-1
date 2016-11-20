package com.github.gripsack.android.ui.companions;

import android.content.Context;
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
import com.github.gripsack.android.data.model.User;
import com.github.gripsack.android.utils.FirebaseUtil;
import com.google.firebase.database.Query;

import timber.log.Timber;

import static com.github.gripsack.android.utils.FirebaseUtil.getCurrentUserId;

public class CompanionsFragment extends Fragment {

    public static final String TAG = "CompanionsFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<CompanionViewHolder> mAdapter;


    private static final String KEY_LAYOUT_POSITION = "layoutPosition";
    private int mRecyclerViewPosition = 0;
    private OnCompanionSelectedListener mListener;


    public static CompanionsFragment newInstance() {
        return new CompanionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_companions, container, false);
        rootView.setTag(TAG);

        //show the list of my companions
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

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
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


    private FirebaseRecyclerAdapter<User, CompanionViewHolder> getFirebaseRecyclerAdapter(Query query) {

        String uid = getCurrentUserId();

        return new FirebaseIndexRecyclerAdapter<User, CompanionViewHolder>(
                User.class,
                R.layout.widget_cardview_companion,
                CompanionViewHolder.class,
                FirebaseUtil.getCompanionsRef().child(uid),
                FirebaseUtil.getUsersRef()) {
            @Override
            protected void populateViewHolder(CompanionViewHolder viewHolder, User model, int position) {
                String key = this.getRef(position).getKey();
                Timber.d("position %d key %s", position, key);
                viewHolder.setAuthor(model.displayName, key);
                viewHolder.setIcon(model.profileImageUrl, key);
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

    public interface OnCompanionSelectedListener {
        void onCompanionRemoved(String companionKey);

        void onCompanionAdded(String companionKey);

        void onCompanionSelected(String companionKey);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompanionSelectedListener) {
            mListener = (OnCompanionSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCompanionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

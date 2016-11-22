package com.github.gripsack.android.ui.explore;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.ui.trips.AddTripActivity;
import com.github.gripsack.android.utils.FirebaseUtil;

import org.parceler.Parcels;

import java.util.ArrayList;

//Adapter for dislpaying places in RecyclerView in Explore Fragment
public class ExploreRecyclerAdapter extends RecyclerView.Adapter<ExploreRecyclerAdapter.PlaceViewHolder> {

    ArrayList<com.github.gripsack.android.data.model.Place> places;
    Context mContext;

    public ExploreRecyclerAdapter(Context context, ArrayList<com.github.gripsack.android.data.model.Place> p) {
        mContext = context;
        places = p;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.explore_grid_item, parent, false);
        PlaceViewHolder vh = new PlaceViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        com.github.gripsack.android.data.model.Place place = places.get(position);
        holder.name.setText(place.getName());
        Glide.with(mContext).load(place.getPhotoUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public ImageView icon;
        public ImageButton addBucketList;
        public ImageButton addLikeList;
        public ImageButton addTripList;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            icon = (ImageView) itemView.findViewById(R.id.item_image);
            addBucketList = (ImageButton) itemView.findViewById(R.id.item_bucketlist);
            addLikeList = (ImageButton) itemView.findViewById(R.id.item_like);
            addTripList = (ImageButton) itemView.findViewById(R.id.item_add);
            addBucketList.setOnClickListener(this);
            addLikeList.setOnClickListener(this);
            addTripList.setOnClickListener(this);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int pos = getAdapterPosition();
            Place place = places.get(pos);
            switch (id) {
                case R.id.item_bucketlist:
                    FirebaseUtil.savePlace(place);
                    FirebaseUtil.bucketPlace(place.getPlaceid());
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.add_bucketlist),
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_like:
                    FirebaseUtil.savePlace(place);
                    FirebaseUtil.likePlace(place.getPlaceid());
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.add_likedlist),
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_add:
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.add_trip),
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_image:
                    Intent intent=new Intent(mContext,AddTripActivity.class);
                    intent.putExtra("SearchedLocation", Parcels.wrap(place));
                    mContext.startActivity(intent);

                    break;

            }
        }
    }
}
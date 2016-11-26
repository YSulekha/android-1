package com.github.gripsack.android.ui.trips;

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
import com.github.gripsack.android.ui.explore.ExploreFragment;

import org.parceler.Parcels;

import java.util.ArrayList;

//Adapter for dislpaying places in RecyclerView in Explore Fragment
public class TripTimelineAdapter extends RecyclerView.Adapter<TripTimelineAdapter.PlaceViewHolder> {

    ArrayList<Place> places;
    Context mContext;
    int pos;

    public TripTimelineAdapter(Context context, ArrayList<Place> p) {
        mContext = context;
        places = p;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_timeline, parent, false);
        PlaceViewHolder vh = new PlaceViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.name.setText(place.getName());
        Glide.with(mContext).load(place.getPhotoUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView icon;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            icon = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }
}


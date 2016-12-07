package com.github.gripsack.android.ui.timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;

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
        holder.tvName.setText(place.getName());
        holder.tvOrderNumber.setText(""+position+1);
        Glide.with(mContext).load(place.getPhotoUrl()).into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView ivIcon;
        public TextView tvOrderNumber;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_name);
            ivIcon = (ImageView) itemView.findViewById(R.id.item_image);
            tvOrderNumber=(TextView) itemView.findViewById(R.id.tvPlaceNumberOrder);
        }
    }
}


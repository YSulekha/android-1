package com.github.gripsack.android.ui.explore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Place;
import com.github.gripsack.android.ui.destinations.DestinationsActivity;
import com.github.gripsack.android.ui.trips.AddTripActivity;
import com.github.gripsack.android.utils.FirebaseUtil;

import org.parceler.Parcels;

import java.util.ArrayList;




public class ExploreRecyclerAdapter extends RecyclerView.Adapter<ExploreRecyclerAdapter.DestinationViewHolder> {

    ArrayList<Place> destinations;
    Context mContext;

    public ExploreRecyclerAdapter(Context context, ArrayList<Place> p) {
        mContext = context;
        destinations = p;
    }

    @Override
    public DestinationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.destination_item, parent, false);
        ExploreRecyclerAdapter.DestinationViewHolder vh = new ExploreRecyclerAdapter.DestinationViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(DestinationViewHolder holder, int position) {


        Place destination = destinations.get(position);
        Glide.with(mContext)
                .load(destination.getPhotoUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // do something with the bitmap
                        // for demonstration purposes, let's just set it to an ImageView
                        Log.v("Inside Bitmap", "dsfsdf");
                        Palette p = Palette.generate(bitmap);
                        int color = p.getDarkVibrantColor(0xFF333333);
                        holder.icon.setImageBitmap(bitmap);
                        holder.view.setBackgroundColor(color);
                        holder.view.setAlpha(0.5f);
                    }
                });


        holder.name.setText(destination.getName());

    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    class DestinationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public ImageView icon;
        public View view;


        public DestinationViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.destination_name);
            icon = (ImageView) itemView.findViewById(R.id.destination_image);
            view = (View) itemView.findViewById(R.id.destination_view);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int pos = getAdapterPosition();
            Place place = destinations.get(pos);
            switch (id) {

                case R.id.item_image:
                   // Intent intent=new Intent(mContext, AddTripActivity.class)
                     //       .putExtra("SearchedLocation", Parcels.wrap(places.get(getLayoutPosition())));
                    Intent intent=new Intent(mContext,AddTripActivity.class);
                    intent.putExtra("SearchedLocation", Parcels.wrap(place));
                    break;

                case R.id.destination_image:
                    Intent destinationIntent = new Intent(mContext, DestinationsActivity.class);
                    String latLong = place.getLatitude() + "," + place.getLongitude();
                    destinationIntent.putExtra("latLong", latLong);
                    destinationIntent.putExtra("photoUrl",place.getPhotoUrl());
                    mContext.startActivity(destinationIntent);
                    break;
            }
        }
    }
}

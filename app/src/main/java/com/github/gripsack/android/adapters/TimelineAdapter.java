package com.github.gripsack.android.adapters;

/**
 * Created by tuze on 11/20/16.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.utils.DynamicHeightImageView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Tugce on 10/21/2016.
 */
public class TimelineAdapter extends
        RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.ivImage)
        DynamicHeightImageView ivImage;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //Triggers ArticleActivity on click
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            //TODO:
            //Intent intent = new Intent(mContext, ArticleDetailActivity.class);
            //intent.putExtra("article",Parcels.wrap(mArticles.get(position)));
            // launch the activity
            //mContext.startActivity(intent);

        }
    }

    // Store a member variable for the contacts
    private static List<Bitmap> mImages;
    // Store the context for easy access
    private static Context mContext;

    // Pass in the contact array into the constructor
    public TimelineAdapter(Context context, List<Bitmap> images) {
        mImages = images;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
    @Override
    public TimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.trip_image_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TimelineAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Bitmap article = mImages.get(position);

        // Set item views based on your views and data model
        DynamicHeightImageView ivImage=viewHolder.ivImage;
        ivImage.setImageResource(0);


    }
    @Override
    public int getItemCount() {
        return mImages.size();
    }
}
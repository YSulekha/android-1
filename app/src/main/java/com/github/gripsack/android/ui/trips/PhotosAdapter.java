package com.github.gripsack.android.ui.trips;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.gripsack.android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tuze on 11/28/16.
 */

public class PhotosAdapter extends
        RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //Triggers ArticleActivity on click
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position


        }
    }

    // Store a member variable for the contacts
    private static List<Bitmap> mPhotos;
    // Store the context for easy access
    private static Context mContext;

    // Pass in the contact array into the constructor
    public PhotosAdapter(Context context, List<Bitmap> photos) {
        mPhotos = photos;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_photo, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PhotosAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Bitmap photo = mPhotos.get(position);

        ImageView imageView=viewHolder.ivPhoto;
        imageView.setImageBitmap(photo);
      ;
       /* if (!TextUtils.isEmpty(photoUrl)) {
            Glide.with(getContext()).load(photoUrl).into(viewHolder.ivPhoto);
        }*/

    }
    @Override
    public int getItemCount() {
        return mPhotos.size();
    }}

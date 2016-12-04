/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.gripsack.android.ui.places;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.gripsack.android.R;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    private ImageView mPlacePhoto;
    private TextView mPlaceName;
    private View paletteView;

    public PlaceViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mPlacePhoto = (ImageView) mView.findViewById(R.id.placePhoto);
        mPlaceName = (TextView) mView.findViewById(R.id.placeName);
        paletteView = mView.findViewById(R.id.vPalette);
    }

    public void setPhoto(String url, final String uid) {
        //GlideUtil.loadProfilePhoto(url, mPlacePhoto);
        Glide.with(mView.getContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // do something with the bitmap
                        // for demonstration purposes, let's just set it to an ImageView
                        Palette palette = Palette.from(bitmap).generate();
                        Palette.Swatch vibrant = palette.getVibrantSwatch();
                        if (vibrant != null) {
                            // Set the background color of a layout based on the vibrant color
                            paletteView.setBackgroundColor(vibrant.getRgb());
                            // Update the title TextView with the proper text color
                            mPlaceName.setTextColor(vibrant.getTitleTextColor());
                        }
                        mPlacePhoto.setImageBitmap(bitmap);
                    }
                });

        mPlacePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void setName(String author, final String uid) {
        mPlaceName.setText(author);
        mPlaceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
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

package com.github.gripsack.android.ui.trips;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.Trip;
import com.github.gripsack.android.utils.GlideUtil;

import org.parceler.Parcels;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    private TextView tvTripName;
    private TextView tvTripDate;
    private ImageView ivTripImage;
    private CardView cardView;

    public TripViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        tvTripName = (TextView) mView.findViewById(R.id.tvTripName);
        tvTripDate=(TextView)mView.findViewById(R.id.tvTripDate);
        ivTripImage=(ImageView)mView.findViewById(R.id.ivTripImage);
        cardView=(CardView)mView.findViewById(R.id.card_view);

    }

    public void setName(String name, final String uid) {
        tvTripName.setText(name);
    }
    public void setImage(Trip trip, final String uid, Context context) {
        GlideUtil.loadProfilePhoto(trip.getSearchDestination().getPhotoUrl(), ivTripImage);
        ivTripImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,EditTripActivity.class)
                        .putExtra("Trip", Parcels.wrap(trip));
                context.startActivity(intent);
            }
        });
    }

    public void setDate(String date, final String uid) {
        tvTripDate.setText(date);
    }


    public void removeView(){
        cardView.setVisibility(View.GONE);
    }
}
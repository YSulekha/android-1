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

package com.github.gripsack.android.ui.companions;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gripsack.android.R;
import com.github.gripsack.android.utils.GlideUtil;

public class CompanionViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    private ImageView mProfilePhoto;
    private TextView mProfileName;

    public CompanionViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mProfilePhoto = (ImageView) mView.findViewById(R.id.profilePhoto);
        mProfileName = (TextView) mView.findViewById(R.id.profileName);
    }

    public void setIcon(String url, final String uid) {
        GlideUtil.loadProfilePhoto(url, mProfilePhoto);

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void setAuthor(String author, final String uid) {
        mProfileName.setText(author);
        mProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
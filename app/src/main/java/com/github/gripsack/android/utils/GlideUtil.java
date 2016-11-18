package com.github.gripsack.android.utils;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtil {


    public static void loadProfilePhoto(String url, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .fitCenter()
                .into(imageView);
    }
}

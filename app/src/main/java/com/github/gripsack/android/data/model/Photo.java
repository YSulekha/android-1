package com.github.gripsack.android.data.model;

import android.graphics.Bitmap;

import org.parceler.Parcel;

/**
 * Created by Tugce on 12/5/2016.
 */

@Parcel
public class Photo {
    private String image;
    private String location;
    private String comment;
    private String date;

    public Photo(){

    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}

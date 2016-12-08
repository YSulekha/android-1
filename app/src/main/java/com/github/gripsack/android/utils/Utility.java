package com.github.gripsack.android.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {


    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static String formatDate(String createdAt){
        //  Mon Oct 31 14:20:57 +0000 2016"
        String createdDateFormat = "MM-dd-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(createdDateFormat, Locale.ENGLISH);
        Date createdDate = null;
        try {
            createdDate = format.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String f = "EEE MMM dd yyyy";
        SimpleDateFormat requiredFormat = new SimpleDateFormat(f);
        String formattedDate = requiredFormat.format(createdDate);
        Log.v("formatUtility",formattedDate);
        return formattedDate;
    }
}

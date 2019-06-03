package com.main.moviedb.demo.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {
    public static String getDateFormat(String fromFormat, String toDateFormat, String release_date, TimeZone timeZone) {
        String formattedDate = "";
        SimpleDateFormat spf = new SimpleDateFormat(fromFormat);
        Date newDate;
        try {
            newDate = spf.parse(release_date);
            spf = new SimpleDateFormat(toDateFormat);
            formattedDate = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean activeNetworkInfo = connectivityManager.getActiveNetworkInfo() != null ? connectivityManager.getActiveNetworkInfo().isConnected() : false;
        return activeNetworkInfo;
    }
}

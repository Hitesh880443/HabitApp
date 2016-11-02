package com.android.habitapp.extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hitesh on 10/21/2016.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static String setFirstltrCapitcal(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }


    public static void saveStringdata(Context mContext, String value, String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringData(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static void saveIntdata(Context mContext, int value, String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntData(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }
}

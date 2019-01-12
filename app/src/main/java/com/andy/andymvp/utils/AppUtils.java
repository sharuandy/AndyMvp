package com.andy.andymvp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtils {

    public static boolean isNetworkAvailable(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}

package com.mirach.googlesignindemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class GlobalUtil {
    private GlobalUtil() {

    }

    public static boolean isConnectedWithInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}

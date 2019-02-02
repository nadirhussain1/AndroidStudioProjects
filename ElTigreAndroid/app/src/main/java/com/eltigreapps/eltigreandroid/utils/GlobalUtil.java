package com.eltigreapps.eltigreandroid.utils;

import android.util.Log;

/**
 * Created by nadirhussain on 23/05/2018.
 */

public final class GlobalUtil {
    private GlobalUtil() {

    }

    public static void printLog(String tag, String message) {
        Log.d(tag, message);
    }
}

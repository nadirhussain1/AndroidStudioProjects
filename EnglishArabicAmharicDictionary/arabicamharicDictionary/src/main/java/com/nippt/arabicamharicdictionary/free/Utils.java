package com.nippt.arabicamharicdictionary.free;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Utils {
    private Utils() {

    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    /**
     * Return data in format "dd/MM/yyyy"
     *
     * @param date
     * @return string
     */
    public static String getDateString(Date date) {
        return dateFormat.format(date);
    }

    /**
     * Get List<String> by calling toString()
     *
     * @param objects
     * @return generated List<String>
     */
    public static <T> List<String> getStringList(List<T> objects) {
        if (objects == null)
            return null;
        List<String> temp = new ArrayList<String>();
        for (T object : objects)
            temp.add(object.toString());
        return temp;
    }

    /**
     * Hide keyboard
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        hideSoftKeyboard(activity, activity.getCurrentFocus().getWindowToken());
    }

    public static void hideSoftKeyboard(Context context, IBinder windowTocken) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowTocken, 0);
    }

    public static String getDBPath(Context mContext) {
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {
            return mContext.getApplicationInfo().dataDir + "/databases/";
        } else {
            return "/data/data/" + mContext.getPackageName() + "/databases/";
        }
    }
}

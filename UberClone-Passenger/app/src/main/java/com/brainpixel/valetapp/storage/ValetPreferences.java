package com.brainpixel.valetapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nadirhussain on 12/03/2017.
 */

public class ValetPreferences {
    private static final String PREF_NAME = "RctPreferences";
    private static final String LOGGED_IN_STATUS = "LOGGED_IN_STATUS";
    private static final String ARE_SETTINGS_DOWNLOADED = "ARE_SETTINGS_DOWNLOADED";


    private ValetPreferences() {

    }

    public static void savedLoggedInStatus(Context context, int loggedInState) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putInt(LOGGED_IN_STATUS, loggedInState);
        prefEditor.commit();
    }

    public static int getLoggedInStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(LOGGED_IN_STATUS, 0);
    }
    public static void saveSettingsDownloadStatus(Context context, boolean areDownloaded) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(ARE_SETTINGS_DOWNLOADED, areDownloaded);
        prefEditor.commit();
    }

    public static boolean areSettingsDownloaded(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ARE_SETTINGS_DOWNLOADED, false);
    }



}

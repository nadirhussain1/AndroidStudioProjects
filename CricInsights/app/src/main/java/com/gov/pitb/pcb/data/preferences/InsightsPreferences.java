package com.gov.pitb.pcb.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.gov.pitb.pcb.utils.GlobalConstants;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class InsightsPreferences {
    private static final String PREF_NAME = "InsightsPreferences";
    private static final String IS_MATCH_IN_PROGRESS = "IS_MATCH_IN_PROGRESS";
    private static final String IS_OFFLINE_DATA_SAVED = "IS_OFFLINE_DATA_SAVED";
    private static final String KEY_USER_TYPE = "KEY_USER_TYPE";
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";
    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String IS_ALARM_SCHEDULED = "IS_ALARM_SCHEDULED";


    private InsightsPreferences() {

    }

    public static void saveMatchInProgress(Context context, boolean inProgress) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(IS_MATCH_IN_PROGRESS, inProgress);
        prefEditor.commit();
    }

    public static boolean isMatchInProgress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_MATCH_IN_PROGRESS, false);
    }

    public static void saveOfflineDataStatus(Context context, boolean isSaved) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(IS_OFFLINE_DATA_SAVED, isSaved);
        prefEditor.commit();
    }

    public static boolean isOfflineDataSaved(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_OFFLINE_DATA_SAVED, false);
    }

    public static void saveUserType(Context context, int userType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putInt(KEY_USER_TYPE, userType);
        prefEditor.commit();
    }

    public static int getUserType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_TYPE, GlobalConstants.SCORER_TYPE);
    }

    public static void savedLoggedInStatus(Context context, boolean isLoggedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        prefEditor.commit();
    }

    public static boolean getLoggedInStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void saveAccessToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(KEY_ACCESS_TOKEN, token);
        prefEditor.commit();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }


}

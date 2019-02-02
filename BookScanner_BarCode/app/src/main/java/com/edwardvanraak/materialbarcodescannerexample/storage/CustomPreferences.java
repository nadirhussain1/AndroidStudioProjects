package com.edwardvanraak.materialbarcodescannerexample.storage;

/**
 * Created by nadirhussain on 27/03/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nadirhussain on 26/09/2016.
 */

public final class CustomPreferences {
    private static final String PREF_NAME = "RctPreferences";
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String AUTHORIZATION_TOKEN = "AUTHORIZATION_TOKEN";
    private static final String MY_COST = "MY_COST";
    private static final String INBOUND_SHIPPING = "INBOUND_SHIPPING";
    private static final String IS_SOUND_ENABLED = "IS_SOUND_ENABLED";
    private static final String IS_OFFLINE_DB_ENABLED = "IS_OFFLINE_DB_ENABLED";
    private static final String IS_OFFLINE_SETTINGS_CHECKED = "IS_OFFLINE_SETTINGS_CHECKED";
    private static final String PRODUCT_TIMESTAMP = "PRODUCT_TIMESTAMP";
    private static final String PRODUCT_PRICES_TIMESTAMP = "PRICES_TIMESTAMP";
    private static final String ACCOUNT_INFO = "ACCOUNT_INFO";
    private static final String IS_OFFLINE_DB_DOWNLOADED = "IS_OFFLINE_DB_DOWNLOADED";


    private CustomPreferences() {

    }

    public static void savedLoggedInStatus(Context context, boolean isLoggedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        prefEditor.commit();
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }


    public static void saveAuthToken(Context context, String accessToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(AUTHORIZATION_TOKEN, accessToken);
        prefEditor.commit();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AUTHORIZATION_TOKEN, "");
    }

    public static void saveMyCostValue(Context context, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putFloat(MY_COST, value);
        prefEditor.commit();
    }

    public static float getMyCostValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(MY_COST, 3.50f);
    }

    public static void saveInboundShippingValue(Context context, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putFloat(INBOUND_SHIPPING, value);
        prefEditor.commit();
    }

    public static float getInboundShipping(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(INBOUND_SHIPPING, 1);
    }

    public static void saveSoundSettings(Context context, boolean isSoundEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(IS_SOUND_ENABLED, isSoundEnabled);
        prefEditor.commit();
    }

    public static boolean isSoundEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_SOUND_ENABLED, true);
    }

    public static void saveOfflineDbEnabled(Context context, String offlineDbFlag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(IS_OFFLINE_DB_ENABLED, offlineDbFlag);
        prefEditor.commit();
    }

    public static String isOfflineDbEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(IS_OFFLINE_DB_ENABLED, "0");
    }

    public static void saveProductTimeStamp(Context context, String timeStamp) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(PRODUCT_TIMESTAMP, timeStamp);
        prefEditor.commit();
    }

    public static String getProductTimestamp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PRODUCT_TIMESTAMP, "0");
    }

    public static void savePricesTimeStamp(Context context, String timeStamp) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(PRODUCT_PRICES_TIMESTAMP, timeStamp);
        prefEditor.commit();
    }

    public static String getPricesTimestamp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PRODUCT_PRICES_TIMESTAMP, "0");
    }

    public static void saveAccountInfo(Context context, String accountInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(ACCOUNT_INFO, accountInfo);
        prefEditor.commit();
    }

    public static String getAccountInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACCOUNT_INFO, null);
    }

    public static void saveOfflineSettingsStatus(Context context, boolean offlineDb) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(IS_OFFLINE_SETTINGS_CHECKED, offlineDb);
        prefEditor.commit();
    }

    public static Boolean getOfflineDbSettingsFlag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_OFFLINE_SETTINGS_CHECKED, false);
    }

    public static void saveofflineDbDownloadStatus(Context context, boolean isDownloaded) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(IS_OFFLINE_DB_DOWNLOADED, isDownloaded);
        prefEditor.commit();
    }

    public static Boolean isOfflineDbDownloaded(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_OFFLINE_DB_DOWNLOADED, false);
    }

}

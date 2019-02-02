package com.crossover.bicycleproject.storage;

import android.content.SharedPreferences;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class RentAppPreferences {


    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private SharedPreferences sharedPreferences;


    public RentAppPreferences(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

    public boolean saveAccessToken(String accessToken) {
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(ACCESS_TOKEN, accessToken);
        return prefEditor.commit();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, "");
    }

    public boolean saveUserLoginStatus(boolean isLoggedIn) {
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        return prefEditor.commit();
    }

    public boolean getUserLoginStatus() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}

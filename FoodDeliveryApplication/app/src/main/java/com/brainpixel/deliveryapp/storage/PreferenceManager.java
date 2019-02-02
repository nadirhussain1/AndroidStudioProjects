package com.brainpixel.deliveryapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nadirhussain on 15/07/2018.
 */

public final class PreferenceManager {
    private static final String PREF_NAME = "PcomPreferences";
    private static final String IS_SIGNED_UP = "IS_SIGNED_UP";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_PHONE = "KEY_USER_PHONE";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";


    private PreferenceManager() {

    }

    public static void saveSignedUpStatus(Context context, boolean isSignedUp) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putBoolean(IS_SIGNED_UP, isSignedUp);
        prefEditor.commit();
    }

    public static boolean getSignedUpStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_SIGNED_UP, false);
    }

    public static void saveUserInfo(Context context, String userName, String userEmail, String mobileNumber) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(KEY_USER_EMAIL, userEmail);
        prefEditor.putString(KEY_USER_NAME, userName);
        prefEditor.putString(KEY_USER_PHONE, mobileNumber);
        prefEditor.commit();
    }

    public static void updateUserName(Context context, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(KEY_USER_NAME, userName);
        prefEditor.commit();
    }

    public static void updateUserEmail(Context context, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(KEY_USER_EMAIL, email);
        prefEditor.commit();
    }

    public static void updateUserMobile(Context context, String mobile) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(KEY_USER_PHONE, mobile);
        prefEditor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public static String getUserMobile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE, "");
    }


}

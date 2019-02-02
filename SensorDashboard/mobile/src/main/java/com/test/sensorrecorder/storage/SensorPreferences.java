package com.test.sensorrecorder.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nadirhussain on 14/05/2018.
 */

public final class SensorPreferences {

    private static final String PREF_NAME = "SensorPreferences";
    private static final String AVAILABLE_SENSORS_LIST = "AVAILABLE_SENSORS_LIST";

    private SensorPreferences() {

    }

    public static void saveAvailableSensorsIds(Context context, String availableSensorsIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();

        prefEditor.putString(AVAILABLE_SENSORS_LIST, availableSensorsIds);
        prefEditor.commit();
    }

    public static String getAvailableSensorsList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AVAILABLE_SENSORS_LIST, null);
    }
}

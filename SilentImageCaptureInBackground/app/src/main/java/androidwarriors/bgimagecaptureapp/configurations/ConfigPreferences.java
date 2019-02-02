package androidwarriors.bgimagecaptureapp.configurations;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nhussain on 5/29/2016.
 */
public class ConfigPreferences {

    private static final String PREF_NAME = "CameraAppPref";
    private static final String SELECTED_INTERVAL_INDEX = "SELECTED_INTERVAL_INDEX";
    private static final String SELECTED_SIZE_INDEX = "SELECTED_SIZE_INDEX";
    private static final String FLASH_SWITCH_STATE = "FLASH_SWITCH_STATE";


    private static ConfigPreferences configPreferences = null;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor prefEditor = null;

    public static ConfigPreferences getInstance(Context context) {
        if (configPreferences == null) {
            configPreferences = new ConfigPreferences(context);
        }
        return configPreferences;
    }

    private ConfigPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
    }

    public void saveIntervalIndex(int index) {
        prefEditor.putInt(SELECTED_INTERVAL_INDEX, index);
        prefEditor.commit();
    }

    public int getIntervalIndex() {
        return sharedPreferences.getInt(SELECTED_INTERVAL_INDEX, 0);
    }

    public void saveSizeIndex(int index) {
        prefEditor.putInt(SELECTED_SIZE_INDEX, index);
        prefEditor.commit();
    }

    public int getSizeIndex() {
        return sharedPreferences.getInt(SELECTED_SIZE_INDEX, 0);
    }

    public void saveFlashSwitchState(boolean isChecked) {
        prefEditor.putBoolean(FLASH_SWITCH_STATE, isChecked);
        prefEditor.commit();
    }

    public boolean getFlashSwitchState() {
        return sharedPreferences.getBoolean(FLASH_SWITCH_STATE, false);
    }
}

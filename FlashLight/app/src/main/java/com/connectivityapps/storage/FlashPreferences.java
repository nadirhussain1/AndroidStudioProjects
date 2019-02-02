package com.connectivityapps.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.connectivityapps.shared.SettingsController;

/**
 * Created by nadirhussain on 25/02/2015.
 */
public class FlashPreferences {

    private static FlashPreferences prefInstance=null;
    private SharedPreferences sharedPreferences=null;
    private SharedPreferences.Editor editor=null;


    public static FlashPreferences getInstance(Context context) {
        if(prefInstance==null){
            prefInstance=new FlashPreferences(context);
        }
        return prefInstance;
    }
    private FlashPreferences(Context context){
        sharedPreferences = context.getSharedPreferences("FlashLightPref", 0);
        editor = sharedPreferences.edit();
    }

    private void saveSettingsSwitchStates(){
        editor.putBoolean("SOUND_ENABLED_SETT", SettingsController.getInstance().isSoundEnabled);
        editor.putBoolean("SOUND_ICON_SETT",SettingsController.getInstance().isShowSoundSwitchEnabled);
        editor.putBoolean("DEF_LIGHT_STATE",SettingsController.getInstance().isDefaultStateOn);
        editor.putBoolean("DEF_SOS_STATE_SETT",SettingsController.getInstance().isDefaultSosStateOn);
        editor.putBoolean("SOS_REPEAT_SETT",SettingsController.getInstance().isSosRepeatEnabled);

        editor.commit();
    }
    private void saveSettingsSpinnerState(){
        editor.putInt("OFF_LIGHT_TIME_INDEX",SettingsController.getInstance().switchOffLightTimeIndex);
        editor.putInt("LIGHT_MODE_INDEX",SettingsController.getInstance().modeToOpenAtLaunchIndex);
        editor.putInt("BUBBLE_ANGLE_TYPE_INDEX",SettingsController.getInstance().showAngleIndex);

        editor.commit();
    }
    public void saveSettings(){
        saveSettingsSwitchStates();
        saveSettingsSpinnerState();
    }
    public void loadSettingsPref(){
        SettingsController.getInstance().isSoundEnabled=sharedPreferences.getBoolean("SOUND_ENABLED_SETT",true);
        SettingsController.getInstance().isShowSoundSwitchEnabled=sharedPreferences.getBoolean("SOUND_ICON_SETT",true);
        SettingsController.getInstance().isDefaultStateOn=sharedPreferences.getBoolean("DEF_LIGHT_STATE",true);


        SettingsController.getInstance().isDefaultSosStateOn=sharedPreferences.getBoolean("DEF_SOS_STATE_SETT",false);
        SettingsController.getInstance().isSosRepeatEnabled=sharedPreferences.getBoolean("SOS_REPEAT_SETT",true);
        SettingsController.getInstance().switchOffLightTimeIndex=sharedPreferences.getInt("OFF_LIGHT_TIME_INDEX",0);
        SettingsController.getInstance().modeToOpenAtLaunchIndex=sharedPreferences.getInt("LIGHT_MODE_INDEX",0);
        SettingsController.getInstance().showAngleIndex=sharedPreferences.getInt("BUBBLE_ANGLE_TYPE_INDEX",0);
        SettingsController.getInstance().shouldCameraDiscoverDialogShow=sharedPreferences.getBoolean("IS_CAMERA_FIRST_TIME",true);
        SettingsController.getInstance().showWarningDialog=sharedPreferences.getBoolean("SHOW_WARNING_DIALOG",true);
        SettingsController.getInstance().showLightOffAlert=sharedPreferences.getBoolean("SHOW_LIGHT_OFF_ALERT",true);
    }
    public void saveCameraUseSettings(){
        editor.putBoolean("IS_CAMERA_FIRST_TIME", SettingsController.getInstance().shouldCameraDiscoverDialogShow);
        editor.commit();
    }
    public void changeSoundSettings(){
        editor.putBoolean("SOUND_ENABLED_SETT", SettingsController.getInstance().isSoundEnabled);
        editor.commit();
    }
    public void showWarningDialogSettings(){
        editor.putBoolean("SHOW_WARNING_DIALOG", SettingsController.getInstance().showWarningDialog);
        editor.commit();
    }
    public void showLightOffAlertSettings(){
        editor.putBoolean("SHOW_LIGHT_OFF_ALERT", SettingsController.getInstance().showLightOffAlert);
        editor.commit();
    }
    public void saveSosLightOptions(){
        editor.putInt("SOS_LIGHT_FLASH_INDEX",SettingsController.getInstance().sosLightToFlashIndex);
        editor.commit();
    }
    public void saveSosRepeatSignal(){
        editor.putBoolean("SOS_REPEAT_SETT",SettingsController.getInstance().isSosRepeatEnabled);
        editor.commit();
    }
    public void saveScreenLightColorProgress(int progress){
        editor.putInt("COLOR_PROGRESS",progress);
        editor.commit();
    }
    public int getScreenColorProgress(){
        return sharedPreferences.getInt("COLOR_PROGRESS",0);
    }
    public void saveBubbleDisplayOption(String displayOption){
        editor.putString("BUBBLE_DISPLAY_OPTION",displayOption);
        editor.commit();
    }
    public String getBubbleDisplayOption(){
        return sharedPreferences.getString("BUBBLE_DISPLAY_OPTION","ANGLE");
    }
    public boolean IsConversionTrackingDone(){
        return sharedPreferences.getBoolean("CONVERSION_TRACKING",false);
    }
    public void changeConversionTrackingStatus(boolean isAlreadyDone){
        editor.putBoolean("CONVERSION_TRACKING",isAlreadyDone);
        editor.commit();
    }

}

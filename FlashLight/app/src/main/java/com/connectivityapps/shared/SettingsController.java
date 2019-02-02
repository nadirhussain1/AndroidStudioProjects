package com.connectivityapps.shared;

/**
 * Created by nadirhussain on 26/02/2015.
 */
public  class SettingsController {

    public static SettingsController controller=null;
    public boolean isSoundEnabled=true;
    public boolean isShowSoundSwitchEnabled=true;
    public boolean isDefaultStateOn=true;
    public boolean isDefaultSosStateOn=false;
    public boolean isSosRepeatEnabled=true;
    public boolean shouldCameraDiscoverDialogShow=true;

    public boolean showWarningDialog=true;
    public boolean showLightOffAlert=true;
    public int switchOffLightTimeIndex=0;
    public int modeToOpenAtLaunchIndex=0;
    public int sosLightToFlashIndex=2;
    public int showAngleIndex=0;
    public int lastSelectedScreen=0;

    public static SettingsController getInstance(){
        if(controller==null){
            controller=new SettingsController();
        }
        return controller;
    }
    private SettingsController(){

    }
}

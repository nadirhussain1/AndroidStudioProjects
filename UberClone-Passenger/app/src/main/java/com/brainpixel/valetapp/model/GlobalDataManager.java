package com.brainpixel.valetapp.model;

import com.brainpixel.valetapp.model.login.LoggedInUserData;
import com.brainpixel.valetapp.model.settings.SettingsDataModel;

/**
 * Created by nadirhussain on 12/03/2017.
 */

public class GlobalDataManager {
    public static final int PASSENGER_ROLE_ID = 1;

    public LoggedInUserData loggedInUserData;
    public SettingsDataModel settingsDataModel;
    private static GlobalDataManager globalDataManager = null;

    private GlobalDataManager() {

    }

    public static GlobalDataManager getInstance() {
        if (globalDataManager == null) {
            globalDataManager = new GlobalDataManager();
        }
        return globalDataManager;
    }

    public void destroy() {
        loggedInUserData = null;
        globalDataManager = null;
        settingsDataModel=null;
    }

}


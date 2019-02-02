package com.brainpixel.valetapp.model;

import com.brainpixel.valetapp.model.login.LoggedInUserData;

/**
 * Created by nadirhussain on 12/03/2017.
 */

public class GlobalDataManager {
    public static final int DRIVER_ROLE_ID = 2;

    public LoggedInUserData loggedInUserData;
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
    }

}


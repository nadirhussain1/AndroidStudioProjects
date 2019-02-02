package com.brainpixel.cletracker.model;

/**
 * Created by nadirhussain on 06/04/2017.
 */

public class GlobalDataManager {
    private static GlobalDataManager globalDataManager;
    public UserProfileDataModel loggedInUserInfoModel;

    private GlobalDataManager() {

    }

    public static GlobalDataManager getGlobalDataManager() {
        synchronized (GlobalDataManager.class) {
            if (globalDataManager == null) {
                globalDataManager = new GlobalDataManager();
            }
            return globalDataManager;
        }
    }
}

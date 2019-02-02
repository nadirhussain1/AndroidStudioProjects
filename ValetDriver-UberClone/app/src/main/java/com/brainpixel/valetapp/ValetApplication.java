package com.brainpixel.valetapp;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.brainpixel.valetapp.model.login.LoggedInUserData;

/**
 * Created by nadirhussain on 12/03/2017.
 */

public class ValetApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeDB();
    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(LoggedInUserData.class);

        ActiveAndroid.initialize(configurationBuilder.create());
    }
}

package com.eltigreapps.eltigreandroid;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nadirhussain on 23/05/2018.
 */

public class EltigreApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

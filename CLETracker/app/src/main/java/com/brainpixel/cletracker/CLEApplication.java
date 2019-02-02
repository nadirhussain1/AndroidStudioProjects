package com.brainpixel.cletracker;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by nadirhussain on 30/03/2017.
 */

public class CLEApplication extends Application {
    private static final String TWITTER_KEY = "uw8mDlhBF880IbxS0Ps2OXdk1";
    private static final String TWITTER_SECRET = "5P29Agv4n4GAsVkuqOOvyfzoIUBgblD78wjo2dvfJ3veYdgpZH";
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(),new Twitter(authConfig));
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

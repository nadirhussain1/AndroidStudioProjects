package com.connectivityapps.flashlight;

import android.app.Application;

import com.connectivityapps.shared.FlashConstants;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;

import io.fabric.sdk.android.Fabric;


public class FlashApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, FlashConstants.FLURRY_API_KEY);
    }
}

package com.brainpixel.deliveryapp;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class DeliverApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}

package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.mobvista.msdk.MobVistaSDK;
import com.mobvista.msdk.out.MobVistaSDKFactory;
import com.onesignal.OneSignal;

import java.util.Map;

import muslim.prayers.time.R;

public class MyApplication extends MultiDexApplication {

    public static final String TrackerName = null;
    private static MyApplication instance;
    public static int GENERAL_TRACKER = 0;
    static Context context;


    public MyApplication() {
    }


    public static MyApplication getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
        instance = this;
        initializeMobvistasdk();

    }

    private void initializeMobvistasdk() {
        MobVistaSDK sdk = MobVistaSDKFactory.getMobVistaSDK();
        Map<String, String> map = sdk.getMVConfigurationMap(getResources().getString(R.string.mobvista_app_id), getResources().getString(R.string.mobvista_api_key));
        sdk.init(map, this);
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onTerminate() {
        super.onTerminate();
    }

}

package com.prayertimes.qibla.appsourcehub.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import muslim.prayers.time.R;

public class ActivitySplash extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        sendAnalytics("Splash Screen");
        TimerTask timertask = new TimerTask() {
            public void run() {
                if (!isCityEmpty()) {
                    Intent intent = new Intent(ActivitySplash.this, com.prayertimes.qibla.appsourcehub.activity.ActivityMain.class);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    Intent intent1 = new Intent(ActivitySplash.this, com.prayertimes.qibla.appsourcehub.activity.ActivityLocation.class);
                    intent1.putExtra("menu_show", true);
                    startActivity(intent1);
                    finish();
                    return;
                }
            }
        };
        (new Timer()).schedule(timertask, 2000L);
    }

    private boolean isCityEmpty() {
        String city = getSharedPreferences("Prayer_pref", 0).getString(Utils.USER_CITY, "");
        return city.isEmpty();
    }

    public void sendAnalytics(String eventName) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, eventName);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Screen Event");

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

    }
}

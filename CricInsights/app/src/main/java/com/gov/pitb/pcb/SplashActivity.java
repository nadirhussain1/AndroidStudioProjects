package com.gov.pitb.pcb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.views.activities.LoginActivity;
import com.gov.pitb.pcb.views.activities.SuperMainActivity;

/**
 * Created by nadirhussain on 13/06/2017.
 */

public class SplashActivity extends Activity {
    private static final long SPLASH_DELAY_IN_MILLIS = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        initDelay();
    }

    private void initDelay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LaunchNextActivity();
            }
        }, SPLASH_DELAY_IN_MILLIS);
    }

    private void LaunchNextActivity() {
        boolean isLoggedIn = InsightsPreferences.getLoggedInStatus(this);
        if (isLoggedIn) {
            Intent intent = new Intent(SplashActivity.this, SuperMainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}

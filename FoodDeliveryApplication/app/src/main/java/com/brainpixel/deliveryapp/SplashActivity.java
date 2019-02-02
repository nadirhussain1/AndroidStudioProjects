package com.brainpixel.deliveryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.brainpixel.deliveryapp.activities.MainActivity;
import com.brainpixel.deliveryapp.activities.StaticWelcomeActivity;
import com.brainpixel.deliveryapp.storage.PreferenceManager;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

/**
 * Created by nadirhussain on 14/07/2018.
 */


public class SplashActivity extends Activity {
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(this).inflate(R.layout.splash_screen,null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchNextActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    private void launchNextActivity() {
        if(!PreferenceManager.getSignedUpStatus(this)){
            launchSignUpScreen();
        }else{
            launchMainScreen();
        }

        finish();
    }
    private void launchSignUpScreen(){
        Intent intent = new Intent(this, StaticWelcomeActivity.class);
        startActivity(intent);
    }
    private void launchMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

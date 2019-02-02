package com.brainpixel.valetapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brainpixel.valetapp.MainActivity;
import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.storage.ValetPreferences;
import com.brainpixel.valetapp.utils.ScalingUtility;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 11/03/2017.
 */

public class StepOneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.reg_log_step_one, null);
        new ScalingUtility(this).scaleRootView(view);
        ButterKnife.bind(this, view);
        setContentView(view);
        checkLoggInStatus();

    }

    @OnClick(R.id.registerButton)
    public void registerButtonClicked() {
        Intent intent = new Intent(StepOneActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.signInButton)
    public void signINButtonClicked() {
        Intent intent = new Intent(StepOneActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void checkLoggInStatus() {
        int loggedInStatus = ValetPreferences.getLoggedInStatus(this);
        if (loggedInStatus == 1) {
            goToLoginScreen();
        } else if (loggedInStatus == 2) {
            goToHomeScreen();
        }
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(StepOneActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(StepOneActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

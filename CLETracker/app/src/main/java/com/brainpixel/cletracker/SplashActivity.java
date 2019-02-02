package com.brainpixel.cletracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.brainpixel.cletracker.model.GlobalDataManager;
import com.brainpixel.cletracker.model.UserProfileDataModel;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.brainpixel.cletracker.views.ProfileActivity;
import com.brainpixel.cletracker.views.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends Activity {

    private static final long SPLASH_DELAY_IN_MILLIS = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.activity_splash, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            goToLoginScreen();
            return;
        }
        decideNextScreen(currentUser.getUid());

    }

    private void decideNextScreen(String userId) {
        ValueEventListener readProfileDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfileDataModel userProfileDataModel = dataSnapshot.getValue(UserProfileDataModel.class);
                if (userProfileDataModel != null) {
                    GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel = userProfileDataModel;
                    showMainScreen();
                } else {
                    showProfileScreen();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("profile").addListenerForSingleValueEvent(readProfileDataListener);
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProfileScreen() {
        Intent intent = new Intent(SplashActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMainScreen() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

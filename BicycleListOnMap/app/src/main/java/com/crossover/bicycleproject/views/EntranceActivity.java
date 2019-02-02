package com.crossover.bicycleproject.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.crossover.bicycleproject.R;
import com.crossover.bicycleproject.storage.RentAppPreferences;
import com.crossover.bicycleproject.utils.CommonUtil;
import com.crossover.bicycleproject.utils.GlobalData;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class EntranceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//       First check that device is connected with internet. If it is not then exit immediately. Otherwise check
//                that whether user logged to the application before or not. Then navigate it to proper screen.
        if (!CommonUtil.isInternetConnected(this)) {
            CommonUtil.showAlertDialog(getString(R.string.no_connection_alert_message), this);
        } else if (getLocalLoggedInStatus()) {
            goToDataLoadingActivity();
        } else {
            setContentView(R.layout.entrance_layout);
            ButterKnife.bind(this); //Butterknife is third party library for inflating views and applying clicks
        }
    }

    @OnClick(R.id.loginButton)
    public void loginClicked() {
        launchRegLoginActivity(false);
    }

    @OnClick(R.id.registerButton)
    public void registerClicked() {
        launchRegLoginActivity(true);
    }

    // Go to loginRegister screen as user has not been logged in already.
    private void launchRegLoginActivity(boolean isRegister) {
        Intent intent = new Intent(this, RegisterLoginActivity.class);
        intent.putExtra(RegisterLoginActivity.REGISTER_LOGIN_KEY, isRegister);
        startActivity(intent);
        finish();
    }

    // As user logged in already. So load the access toke and go to Data loading screen directly.
    private void goToDataLoadingActivity() {
        loadLocalSavedAccessToken();
        Intent intent = new Intent(EntranceActivity.this, DataLoadingActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean getLocalLoggedInStatus() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        RentAppPreferences rentAppPreferences = new RentAppPreferences(sharedPreferences);
        return rentAppPreferences.getUserLoginStatus();
    }

    private void loadLocalSavedAccessToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        RentAppPreferences rentAppPreferences = new RentAppPreferences(sharedPreferences);
        GlobalData.accessToken = rentAppPreferences.getAccessToken();
    }


}

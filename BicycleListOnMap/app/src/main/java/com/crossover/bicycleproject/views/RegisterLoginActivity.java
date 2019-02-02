package com.crossover.bicycleproject.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.bicycleproject.R;
import com.crossover.bicycleproject.contracts.LoginRegisterContract;
import com.crossover.bicycleproject.model.AccessToken;
import com.crossover.bicycleproject.presenters.RegisterLoginPresenter;
import com.crossover.bicycleproject.storage.RentAppPreferences;
import com.crossover.bicycleproject.utils.CommonUtil;
import com.crossover.bicycleproject.utils.GlobalData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class RegisterLoginActivity extends AppCompatActivity implements LoginRegisterContract.LoginRegisterViewContract {
    public static final String REGISTER_LOGIN_KEY = "REGISTER_LOGIN_KEY";

    private RegisterLoginPresenter registerLoginPresenter;
    private boolean isRegister;

    @Bind(R.id.emailEditText)
    EditText emailEditor;
    @Bind(R.id.passwordEditText)
    EditText passEditor;

    // Code has been written by following MVP pattern that facilitates to write unit tests easily
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_layout);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            isRegister = getIntent().getBooleanExtra(REGISTER_LOGIN_KEY, true);
        }
        registerLoginPresenter = new RegisterLoginPresenter(this);
    }

    @OnClick(R.id.doneButton)
    public void doneButtonClicked() {
        //First check connectivity. If device is not connected with network then exit.
        if (!registerLoginPresenter.checkInternetConnectivity(this)) {
            showNoConnectivityAlert(getString(R.string.no_connection_alert_message));
            return;
        }
        String email = emailEditor.getText().toString();
        String password = passEditor.getText().toString();
        registerLoginPresenter.verifyCredentials(email, password, isRegister);
    }

    // This method is invoked when user presses done button on soft keyboard of android device.
    @OnEditorAction(R.id.passwordEditText)
    public boolean onActionDone(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doneButtonClicked();
            return true;
        }
        return false;
    }

    @Override
    public void showNoConnectivityAlert(String message) {
        CommonUtil.showAlertDialog(message, this);
    }

    @Override
    public void showEmailErrorToast() {
        Toast.makeText(this, getString(R.string.invalid_email_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordErrorToast() {
        Toast.makeText(this, getString(R.string.invalid_pass_error), Toast.LENGTH_SHORT).show();
    }

    // As AccessToken has been retrieved. So login process of user is complete. Save the status
    // so that we can navigate user to the data loading screen next time when he/she comes.
    @Override
    public void goToDataLoadingActivity(AccessToken accessToken) {
        GlobalData.accessToken = accessToken.getAccessToken();
        saveAccessTokenLocally(accessToken);
        launchDataLoadingActivity();
    }

    @Override
    public void showLoginAuthError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void launchDataLoadingActivity() {
        Intent intent = new Intent(RegisterLoginActivity.this, DataLoadingActivity.class);
        startActivity(intent);
        finish();
    }
    // Save Access Token to Local storage : that is preferences.
    private void saveAccessTokenLocally(AccessToken accessToken){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        RentAppPreferences rentAppPreferences = new RentAppPreferences(sharedPreferences);
        rentAppPreferences.saveAccessToken(accessToken.getAccessToken());
        rentAppPreferences.saveUserLoginStatus(true);
    }
}

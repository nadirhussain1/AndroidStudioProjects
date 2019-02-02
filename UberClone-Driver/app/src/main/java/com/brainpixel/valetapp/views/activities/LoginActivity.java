package com.brainpixel.valetapp.views.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.brainpixel.valetapp.BaseActivity;
import com.brainpixel.valetapp.MainActivity;
import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.model.login.LoggedInUserData;
import com.brainpixel.valetapp.model.login.LoginResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.storage.ValetDbManager;
import com.brainpixel.valetapp.storage.ValetPreferences;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 11/03/2017.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(R.id.passwordEditor)
    EditText passwordEditor;
    @BindView(R.id.remCheckBox)
    CheckBox rememberCheckBox;
    @BindView(R.id.errorCodeTextView)
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_login, null);
        new ScalingUtility(this).scaleRootView(view);
        ButterKnife.bind(this, view);
        setContentView(view);
    }

    @OnClick(R.id.forgotPassTextView)
    public void forgotPassClicked() {
        showForgotPassEmailAlert();
    }

    @OnClick(R.id.signInButton)
    public void signInClicked() {
        doSignIn();
    }

    private void doSignIn() {
        String email = emailEditor.getText().toString();
        String pass = passwordEditor.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            showErrorMessage(getString(R.string.empty_field_error));
            return;
        }
        if (!GlobalUtil.isEmailValid(email)) {
            showErrorMessage(getString(R.string.invalid_email));
            return;
        }
        if (performInternetCheck()) {
            doLoginEndPointCall(email, pass, GlobalDataManager.DRIVER_ROLE_ID);
        }
    }

    @OnClick(R.id.signUpTextView)
    public void signUpClicked() {
        goToSignUP();
    }

    @OnFocusChange(R.id.emailEditor)
    public void emailFocusChange() {
        hideErrorMessage();
    }

    @OnFocusChange(R.id.passwordEditor)
    public void passFocusChange() {
        hideErrorMessage();
    }

    @OnEditorAction(R.id.passwordEditor)
    public boolean onDoneAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doSignIn();
            return true;
        }
        return false;
    }

    public void showForgotPassEmailAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.forgot_password));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.single_editor_aler, null);
        final EditText emailInputEditor = (EditText) dialogView.findViewById(R.id.inputEditor);
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.done_label), null);
        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button doneButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                doneButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailInputEditor.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            GlobalUtil.displayToastInfoMessage(LoginActivity.this, getString(R.string.email_empty));
                        } else if (!GlobalUtil.isEmailValid(email)) {
                            GlobalUtil.displayToastInfoMessage(LoginActivity.this, getString(R.string.invalid_email));
                        } else {
                            alertDialog.dismiss();
                            doForgotPasswordEndpointCall(email);
                        }
                    }
                });
            }
        });

        alertDialog.show();

    }


    private void goToSignUP() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorMessage(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        errorTextView.setVisibility(View.INVISIBLE);
    }

    private void doLoginEndPointCall(String email, String password, int role) {
        showProgressDialog();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<LoginResponse> call = apiService.login(email, password, role);
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideProgressDialog();
                LoginResponse loginResponse = response.body();
                if (loginResponse.getError()) {
                    displayMessageAlert(loginResponse.getMessage());
                    return;
                }

                onLoginSuccess(loginResponse.getData());

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideProgressDialog();
                if (t != null) {
                    displayMessageAlert(t.toString());
                }
            }
        });
    }

    private void doForgotPasswordEndpointCall(String email) {
        showProgressDialog();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.forgotPassword(email);
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                hideProgressDialog();
                displayMessageAlert(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                hideProgressDialog();
                if (t != null) {
                    displayMessageAlert(t.getMessage());
                }
            }
        });

    }

    private void onLoginSuccess(LoggedInUserData loggedInUserData) {
        ValetPreferences.savedLoggedInStatus(LoginActivity.this, 2);
        ValetDbManager.saveLoggedInUser(loggedInUserData);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void displayMessageAlert(String error) {
        GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), error);
    }
}

package com.gov.pitb.pcb.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.data.server.login.LoginData;
import com.gov.pitb.pcb.data.server.login.LoginRequestModel;
import com.gov.pitb.pcb.data.server.login.LoginResponse;
import com.gov.pitb.pcb.network.RetroApiClient;
import com.gov.pitb.pcb.network.RetroInterface;
import com.gov.pitb.pcb.utils.ApiHelper;
import com.gov.pitb.pcb.utils.GlobalConstants;
import com.gov.pitb.pcb.utils.GlobalUtil;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 27/07/2017.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.userNameEditor)
    EditText userNameEditor;
    @BindView(R.id.passwordEditor)
    EditText passWordEditor;
    @BindView(R.id.errorTextView)
    TextView errorTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    private boolean hasScrolledTwice = false;
    private boolean isApiCallInProgress=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(this, R.layout.login_screen, null);
        new ViewScaleHandler(this).scaleRootView(rootView);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
    }

    @OnClick(R.id.passwordEditor)
    public void passEditorClicked() {
        hideErrorTextView();
        initScroll();
    }

    @OnFocusChange(R.id.passwordEditor)
    public void onPassFocus() {
        hideErrorTextView();
        initScroll();
    }

    @OnClick(R.id.loginButton)
    public void loginButtonClicked() {
        hideErrorTextView();
        initDeviceLogin();
    }

    @OnEditorAction(R.id.passwordEditor)
    public boolean passwordActionDone(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            initDeviceLogin();
        }
        return false;
    }

    private void showErrorTextView(String text) {
        errorTextView.setText(text);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorTextView() {
        errorTextView.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void scrollToBottom() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 1000);
                if (!hasScrolledTwice) {
                    hasScrolledTwice = true;
                    scrollToBottom();
                }
            }
        }, 250);
    }

    private void initScroll() {
        hasScrolledTwice = false;
        scrollToBottom();

    }

    private void initDeviceLogin() {
        String userName = userNameEditor.getText().toString();
        String password = passWordEditor.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            showErrorTextView("Enter valid credentials");
            return;
        }
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showOkAlertDialog(this, getString(R.string.no_connection_title), getString(R.string.no_connection_msg));
            return;
        }
        initDoLoginApiCall(userName, password);
    }

    private void initDoLoginApiCall(String userName, String password) {
        if(!isApiCallInProgress) {
            isApiCallInProgress=true;

            showProgressBar();
            LoginRequestModel loginRequestModel = new LoginRequestModel(userName, password);
            RetroInterface apiService = RetroApiClient.getClient(this).create(RetroInterface.class);
            Call<LoginResponse> call = apiService.doLogin(loginRequestModel);
            ApiHelper.enqueueWithRetry(call, new retrofit2.Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        onLoginSuccess(loginResponse.getData());
                    } else {
                        onLoginError(loginResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    onLoginError("Login Failed");
                }
            });
        }
    }

    private void onLoginError(String error) {
        isApiCallInProgress=false;
        hideProgressBar();
        showErrorTextView(error);
    }

    private void onLoginSuccess(LoginData loginData) {
        isApiCallInProgress=false;
        hideProgressBar();
        InsightsPreferences.saveAccessToken(this, loginData.getUToken());
        if (loginData.getUType().equalsIgnoreCase("scorer")) {
            InsightsPreferences.saveUserType(this, GlobalConstants.SCORER_TYPE);
        } else {
            InsightsPreferences.saveUserType(this, GlobalConstants.ANALYST_TYPE);
        }
        InsightsPreferences.savedLoggedInStatus(this,true);
        goToNextScreen();
    }

    private void goToNextScreen() {
        Intent intent = new Intent(LoginActivity.this, SuperMainActivity.class);
        startActivity(intent);
        finish();
    }
}

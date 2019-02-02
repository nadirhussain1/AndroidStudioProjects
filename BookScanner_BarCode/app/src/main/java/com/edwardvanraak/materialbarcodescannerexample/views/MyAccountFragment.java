package com.edwardvanraak.materialbarcodescannerexample.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.model.login.LoginResponse;
import com.edwardvanraak.materialbarcodescannerexample.network.RetroApiClient;
import com.edwardvanraak.materialbarcodescannerexample.network.RetroInterface;
import com.edwardvanraak.materialbarcodescannerexample.storage.CustomPreferences;
import com.edwardvanraak.materialbarcodescannerexample.utils.ApiHelper;
import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 27/03/2017.
 */

public class MyAccountFragment extends Fragment {
    private static final String SIGN_UP_URL = "http://www.ezhustle.com/mypanel/signup";

    @BindView(R.id.userNameEditor)
    EditText userNameEditor;
    @BindView(R.id.passwordEditor)
    EditText passwordEditor;
    @BindView(R.id.nickNameEditor)
    EditText nickNameEditor;


    private ProgressDialog mProgressDialog;
    private String userName, password, deviceName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String accountInfo = CustomPreferences.getAccountInfo(getActivity());
        if (!TextUtils.isEmpty(accountInfo)) {
            userNameEditor.append(accountInfo.split(",")[0]);
            passwordEditor.append(accountInfo.split(",")[1]);
            nickNameEditor.append(accountInfo.split(",")[2]);
        }
    }

    @OnClick(R.id.loginButton)
    public void loginButtonClicked() {
        performLogin();
        nickNameEditor.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
    }

    @OnEditorAction(R.id.nickNameEditor)
    public boolean onNickNameEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            performLogin();
            return true;
        }
        return false;
    }

    private void performLogin() {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), getString(R.string.no_connection_message));
            return;
        }

        userName = userNameEditor.getText().toString();
        password = passwordEditor.getText().toString();
        deviceName = nickNameEditor.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(deviceName)) {
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_error_title), "Enter all required info");
            return;
        }

        String userNamePasswordValue = userName + ":" + password;
        byte[] encodeValue = Base64.encode(userNamePasswordValue.getBytes(), Base64.DEFAULT);
        String authorization = "Basic " + new String(encodeValue);

        String deviceSerial = GlobalUtil.getDeviceImeiNumber(getActivity());
        doLoginApiCall(authorization.trim(), deviceSerial, deviceName);
    }

    @OnClick(R.id.signUpLabel)
    public void signUpClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SIGN_UP_URL));
        startActivity(browserIntent);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nickNameEditor.getWindowToken(), 0);

    }

    private void postLogin(LoginResponse loginResponse) {
        String message = "You are logged in successfully. You can now perform search";
        if (!TextUtils.isEmpty(loginResponse.getToken())) {
            String token = "Bearer " + loginResponse.getToken();
            GlobalUtil.auth_token_value = token;

            String accountInfo = userName + "," + password + "," + deviceName;
            CustomPreferences.saveAccountInfo(getActivity(), accountInfo);
            CustomPreferences.saveAuthToken(getActivity(), token);
            CustomPreferences.savedLoggedInStatus(getActivity(), true);
            CustomPreferences.saveOfflineDbEnabled(getActivity(), loginResponse.getOfflineDb());
            hideKeyboard();
        } else {
            message = loginResponse.getError();
        }

        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), message);
        removeFragment();
    }

    private void removeFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);

    }


    private void doLoginApiCall(String authorization, String deviceSerial, String deviceName) {
        showProgressDialog();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<LoginResponse> call = apiService.performLogin(authorization, deviceSerial, deviceName);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideProgressDialog();
                if (response == null || response.body() == null) {
                    String message = "Unable to get user info from server";
                    GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), message);
                    return;
                }

                LoginResponse loginResponse = response.body();
                postLogin(loginResponse);

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideProgressDialog();
                if (t != null) {
                    displayMessageAlert(true, t.getMessage());
                }
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Please wait");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }

    private void displayMessageAlert(boolean isError, String message) {
        String title = getString(R.string.alert_message_title);
        if (isError) {
            title = getString(R.string.alert_error_title);
        }
        GlobalUtil.showMessageAlertWithOkButton(getActivity(), title, message);
    }
}

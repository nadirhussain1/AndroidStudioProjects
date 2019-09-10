package com.mirach.googlesignindemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GoogleLoginActivity extends AppCompatActivity implements OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 10;
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpViewsAndClicks();
        doInitialLoginSetUp();
    }

    private void setUpViewsAndClicks() {
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
    }

    private void doInitialLoginSetUp() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check whether user has already signed-in and whether token is valid or it has been expired.
        OptionalPendingResult<GoogleSignInResult> silentSignInResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (silentSignInResult.isDone()) {
            // This means valid token exists from previous login.
            handleSignInResult(getString(R.string.from_previous_login), silentSignInResult.get());
        } else {
            /*
               There are two possibilities. Either user has not signed in yet or token has expired.
               Callback of silent login will return successful result with refreshed token if user has already signed-in
             */
            silentSignInResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(getString(R.string.refreshed_token), result);
                }
            });
        }

    }

    private void handleSignInResult(String label, GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = signInResult.getSignInAccount();
            launchMainScreen(label, googleSignInAccount.getIdToken());
        } else if (GlobalUtil.isConnectedWithInternet(this)) {
            // User has not signed-in yet, so making sign-in button visible
            showSignInButton();
        } else {
            Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSignInButton() {
        signInButton.setVisibility(View.VISIBLE);
    }

    //Go to main screen with valid Token
    private void launchMainScreen(String tokenLabel, String tokenValue) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_TOKEN_INFO, tokenLabel);
        intent.putExtra(MainActivity.KEY_TOKEN_VALUE, tokenValue);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                onSignInButtonClick();
                break;

        }
    }

    // When user has explicitly clicked sign-in button
    private void onSignInButtonClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                onExplicitSignInResult(data);
                break;
        }
    }

    /*
       Evaluating the data returned after user has explicitly choosen account and signed-in... this will happen only first time
       when user opens app.
     */
    private void onExplicitSignInResult(Intent data) {
        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(getString(R.string.from_new_login), signInResult);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getString(R.string.login_fail_msg), Toast.LENGTH_SHORT).show();
    }
}

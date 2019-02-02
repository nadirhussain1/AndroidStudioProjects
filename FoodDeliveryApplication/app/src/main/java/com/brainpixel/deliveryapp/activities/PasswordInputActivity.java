package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.storage.PreferenceManager;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by nadirhussain on 21/07/2018.
 */

public class PasswordInputActivity extends Activity {
    @BindView(R.id.passwordEditor)
    EditText passwordEditor;
    @BindView(R.id.errorMessageTextView)
    TextView errorMessageTextView;
    @BindView(R.id.doneButton)
    Button doneButton;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);
        init();
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.password_input_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void init() {
        passwordEditor.addTextChangedListener(new PasswordEditorWatcher());
    }

    @OnEditorAction(R.id.passwordEditor)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doneClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.doneButton)
    public void doneClick() {
        String password = passwordEditor.getText().toString();
        if (password.length() < GlobalConstants.PASSWORD_LENGTH_LIMIT) {
            showErrorMessage(getString(R.string.entered_wrong_pass_msg));
            return;
        }
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(PasswordInputActivity.this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }
        matchPasswordWithStoredOne(password);
    }

    private void showErrorMessage(String error) {
        if (error.toLowerCase().startsWith("the password is invalid")) {
            error = "Wrong password";
        }
        errorMessageTextView.setText(error);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.forgotPasswordTextView)
    public void onForgotPasswordClick() {
        launchForgotPasswordScreen();
    }

    private void launchForgotPasswordScreen() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);

    }

    private void launchMainScreen() {
        PreferenceManager.saveSignedUpStatus(this, true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void matchPasswordWithStoredOne(final String inputPassword) {
        showProgressBarLayout();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, inputPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBarLayout();
                        if (!task.isSuccessful()) {
                            showErrorMessage(task.getException().getMessage());
                            return;
                        }

                        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                            launchMainScreen();
                        } else {
                            showErrorMessage("Your email is not verified yet.");
                        }
                    }
                });
    }

    private class PasswordEditorWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Timer delayTimer = new Timer();
            delayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onPasswordInputChange(s.length());
                        }
                    });

                }
            }, 1000);

        }

        private void onPasswordInputChange(int length) {
            errorMessageTextView.setVisibility(View.GONE);
            if (length >= GlobalConstants.PASSWORD_LENGTH_LIMIT) {
                doneButton.setBackgroundResource(R.drawable.green_rounded_bg);
                doneButton.setTextColor(Color.WHITE);
            } else {
                doneButton.setBackgroundResource(R.drawable.disabled_green_rounded_bg);
                doneButton.setTextColor(Color.parseColor("#AAFFFFFF"));
            }
        }
    }
}

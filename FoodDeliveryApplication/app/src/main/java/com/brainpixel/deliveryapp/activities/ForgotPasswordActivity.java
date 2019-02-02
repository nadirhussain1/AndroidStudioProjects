package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
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
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by nadirhussain on 21/07/2018.
 */

public class ForgotPasswordActivity extends Activity {
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(R.id.doneButton)
    Button doneButton;
    @BindView(R.id.errorMessageTextView)
    TextView errorMessageTextView;
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
        View view = LayoutInflater.from(this).inflate(R.layout.forgot_password, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void init() {
        emailEditor.addTextChangedListener(new EmailEditorWatcher());
    }

    @OnClick(R.id.backClickAreaLayout)
    public void onBackClick() {
        finish();
    }

    @OnEditorAction(R.id.emailEditor)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doneClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.doneButton)
    public void doneClick() {
        String email = emailEditor.getText().toString();
        if (!email.contains("@") || email.length() < 8) {
            showErrorMessage(getString(R.string.invalid_email_msg));
            return;
        }
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }
        sendPasswordResetEmail(email);
    }

    private void showErrorMessage(String error) {
        errorMessageTextView.setText(error);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void sendPasswordResetEmail(String email) {
        showProgressBarLayout();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressBarLayout();
                        if (!task.isSuccessful()) {
                            String message = task.getException().getMessage();
                            if (message.startsWith("There is no user record")) {
                                message = "This email is not associated with any account.";
                            }
                            showErrorMessage(message);
                            return;
                        }

                        GlobalUtil.showCustomizedAlert(ForgotPasswordActivity.this, "Success", getString(R.string.reset_password_email_sent), getString(R.string.ok_label), new OnPositiveButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                onPasswordChanged();
                            }
                        });


                    }
                });
    }

    private void onPasswordChanged() {
        finish();
    }

    private class EmailEditorWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            errorMessageTextView.setVisibility(View.GONE);
            if (s.toString().contains("@") && s.length() >= 8) {
                doneButton.setBackgroundResource(R.drawable.green_rounded_bg);
                doneButton.setTextColor(Color.WHITE);
            } else {
                doneButton.setBackgroundResource(R.drawable.disabled_green_rounded_bg);
                doneButton.setTextColor(Color.parseColor("#AAFFFFFF"));
            }
        }
    }

}

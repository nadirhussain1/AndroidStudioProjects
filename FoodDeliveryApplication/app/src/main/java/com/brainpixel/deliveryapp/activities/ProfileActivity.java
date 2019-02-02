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
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.model.User;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by nadirhussain on 21/07/2018.
 */

public class ProfileActivity extends Activity {
    @BindView(R.id.firstNameEditor)
    EditText firstNameEditor;
    @BindView(R.id.lastNameEditor)
    EditText lastnameEditor;
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(R.id.passwordEditor)
    EditText passwordEditor;
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
        View view = LayoutInflater.from(this).inflate(R.layout.profile_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }
    private void init(){
        firstNameEditor.addTextChangedListener(new ProfileEditorsWatcher());
        lastnameEditor.addTextChangedListener(new ProfileEditorsWatcher());
        emailEditor.addTextChangedListener(new ProfileEditorsWatcher());
        passwordEditor.addTextChangedListener(new ProfileEditorsWatcher());
    }

    @OnEditorAction(R.id.passwordEditor)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doneClick();
            return true;
        }
        return false;
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.doneButton)
    public void doneClick() {
        String firstName = firstNameEditor.getText().toString();
        String lastName = lastnameEditor.getText().toString();
        String emailAddress = emailEditor.getText().toString();
        String password = passwordEditor.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            GlobalUtil.showToastMessage(ProfileActivity.this, "Names cannot be empty", GlobalConstants.TOAST_RED);
            return;
        }
        if (emailAddress.isEmpty() || !emailAddress.contains("@")) {
            GlobalUtil.showToastMessage(ProfileActivity.this, "Email is not valid", GlobalConstants.TOAST_RED);
            return;
        }
        if (password.length() < GlobalConstants.PASSWORD_LENGTH_LIMIT) {
            GlobalUtil.showToastMessage(ProfileActivity.this, "Length of password should be more than 4", GlobalConstants.TOAST_RED);
            return;
        }
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(ProfileActivity.this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }

        User user = new User(firstName, lastName, emailAddress);
        user.setPassword(password);
        updateProfileInfoOfUser(user);

    }

    private boolean isValid() {
        String firstName = firstNameEditor.getText().toString();
        String lastName = lastnameEditor.getText().toString();
        String emailAddress = emailEditor.getText().toString();
        String password = passwordEditor.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            return false;
        }
        if (emailAddress.isEmpty() || !emailAddress.contains("@")) {
            return false;
        }
        if (password.length() < GlobalConstants.PASSWORD_LENGTH_LIMIT) {
            return false;
        }
        if (!GlobalUtil.isInternetConnected(this)) {
            return false;
        }
        return true;
    }


    private void updateProfileInfoOfUser(final User userInfo) {
        showProgressBarLayout();
        AuthCredential credential = EmailAuthProvider.getCredential(userInfo.getEmail(), userInfo.getPassword());
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    initProfileUpdateProcess(userInfo);
                } else {
                    hideProgressBarLayout();
                    GlobalUtil.showToastMessage(ProfileActivity.this, task.getException().getMessage(), GlobalConstants.TOAST_RED);
                }
            }
        });


    }


    private void initProfileUpdateProcess(final User userInfo) {
        String displayName = "" + userInfo.getFirstName() + " " + userInfo.getLastName();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            sendVerificationEmail();
                        } else {
                            hideProgressBarLayout();
                            GlobalUtil.showToastMessage(ProfileActivity.this, task.getException().getMessage(), GlobalConstants.TOAST_RED);
                        }
                    }
                });
    }

    private void sendVerificationEmail() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressBarLayout();
                if (task.isSuccessful()) {
                    GlobalUtil.showCustomizedAlert(ProfileActivity.this, "Email Verification", getString(R.string.verify_email), getString(R.string.ok_label), new OnPositiveButtonClickListener() {
                        @Override
                        public void onButtonClick() {
                            goToLoginScreen();
                        }
                    });

                } else {
                    GlobalUtil.showToastMessage(ProfileActivity.this, task.getException().getMessage(), GlobalConstants.TOAST_RED);
                }
            }
        });

    }

    private void goToLoginScreen() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, StaticWelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private class ProfileEditorsWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isValid()) {
                doneButton.setBackgroundResource(R.drawable.green_rounded_bg);
                doneButton.setTextColor(Color.WHITE);
            } else {
                doneButton.setBackgroundResource(R.drawable.disabled_green_rounded_bg);
                doneButton.setTextColor(Color.parseColor("#AAFFFFFF"));
            }
        }
    }


}

package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.storage.PreferenceManager;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 26/11/2018.
 */

public class UpdateProfileActivity extends Activity {
    public static final String KEY_UPDATE_INDEX = "KEY_UPDATE_INDEX";

    public static final int UPDATE_NAME_INDEX = 0;
    public static final int UPDATE_EMAIL_INDEX = 1;
    public static final int UPDATE_PASSWORD_INDEX = 2;

    private static final int STEP_VERIFY_PASSWORD = 1;
    private static final int STEP_POST_PASSWORD_VERIFICATION = 2;

    @BindView(R.id.nameUpdateLayout)
    LinearLayout nameUpdateLayout;
    @BindView(R.id.emailUpdateLayout)
    LinearLayout emailUpdateLayout;
    @BindView(R.id.verifyPasswordLayout)
    LinearLayout verifyPasswordLayout;
    @BindView(R.id.updatePasswordLayout)
    LinearLayout updatePasswordLayout;
    @BindView(R.id.saveButton)
    Button saveButton;
    @BindView(R.id.firstNameEditor)
    EditText firstNameEditor;
    @BindView(R.id.lastNameEditor)
    EditText lastNameEditor;
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(R.id.oldPasswordInputEditor)
    EditText oldPasswordInputEditor;
    @BindView(R.id.newPasswordInputEditor)
    EditText newPasswordInputEditor;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;


    private int currentUpdateIndex = UPDATE_NAME_INDEX;
    private int currentStep = STEP_VERIFY_PASSWORD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);

        if (getIntent() != null) {
            currentUpdateIndex = getIntent().getIntExtra(KEY_UPDATE_INDEX, UPDATE_NAME_INDEX);
        }
        populateViews();
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.edit_profile_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void hideAllLayouts() {
        nameUpdateLayout.setVisibility(View.GONE);
        emailUpdateLayout.setVisibility(View.GONE);
        verifyPasswordLayout.setVisibility(View.GONE);
        updatePasswordLayout.setVisibility(View.GONE);
    }

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void populateViews() {
        hideAllLayouts();
        switch (currentUpdateIndex) {
            case UPDATE_NAME_INDEX:
                switchOnNameViews();
                break;
            case UPDATE_EMAIL_INDEX:
                switchOnVerifyPasswordViews();
                break;
            case UPDATE_PASSWORD_INDEX:
                switchOnVerifyPasswordViews();
                break;
            default:
                break;
        }
    }

    private void switchOnNameViews() {
        String userName = PreferenceManager.getUserName(this);
        firstNameEditor.setText(userName.split(" ")[0]);
        lastNameEditor.setText(userName.split(" ")[1]);

        nameUpdateLayout.setVisibility(View.VISIBLE);
        saveButton.setText("Update");
    }

    private void switchOnEmailUpdateViews() {
        String email = PreferenceManager.getUserEmail(this);
        emailEditor.setText(email);
        emailUpdateLayout.setVisibility(View.VISIBLE);
        saveButton.setText("Update");
    }

    private void switchOnVerifyPasswordViews() {
        verifyPasswordLayout.setVisibility(View.VISIBLE);
        saveButton.setText("Verify Password");
    }

    private void switchOnUpdatePasswordViews() {
        updatePasswordLayout.setVisibility(View.VISIBLE);
        saveButton.setText("Update Password");
    }

    @OnClick(R.id.backIconView)
    public void onArrowClicked() {
        finish();
    }

    @OnClick(R.id.titleView)
    public void onTitleClicked() {
        finish();
    }

    @OnClick(R.id.saveButton)
    public void saveButtonClicked() {
        switch (currentUpdateIndex) {
            case UPDATE_NAME_INDEX:
                handleNameUpdate();
                break;
            case UPDATE_EMAIL_INDEX:
                handleEmailUpdate();
                break;
            case UPDATE_PASSWORD_INDEX:
                handlePasswordUpdate();
                break;
            default:
                break;
        }
    }

    private void handleNameUpdate() {
        String firstName = firstNameEditor.getText().toString();
        String lastName = lastNameEditor.getText().toString();
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            GlobalUtil.showToastMessage(this, "First and last name cannot be empty", GlobalConstants.TOAST_RED);
            return;
        }
        updateUserNameInFirebase(firstName, lastName);
    }

    private void handleEmailUpdate() {
        if (currentStep == STEP_VERIFY_PASSWORD) {
            verifyPassword();
        } else if (currentStep == STEP_POST_PASSWORD_VERIFICATION) {
            initUpdateEmailProcess();
        }
    }

    private void handlePasswordUpdate() {
        if (currentStep == STEP_VERIFY_PASSWORD) {
            verifyPassword();
        } else if (currentStep == STEP_POST_PASSWORD_VERIFICATION) {
            initUpdatePasswordProcess();
        }
    }

    private void verifyPassword() {
        String oldPassword = oldPasswordInputEditor.getText().toString();
        if (oldPassword.trim().isEmpty()) {
            GlobalUtil.showToastMessage(this, "Wrong password", GlobalConstants.TOAST_RED);
            return;
        } else {
            reAuthenticateUser(PreferenceManager.getUserEmail(this), oldPassword);
        }
    }

    private void initUpdateEmailProcess() {
        String email = emailEditor.getText().toString();
        if (email.isEmpty() || !email.contains("@")) {
            GlobalUtil.showToastMessage(this, "Enter valid email", GlobalConstants.TOAST_RED);
            return;
        }
        updateEmailInFirebase(email);
    }

    private void initUpdatePasswordProcess() {
        String newPassword = newPasswordInputEditor.getText().toString();
        if (newPassword.length() < GlobalConstants.PASSWORD_LENGTH_LIMIT) {
            GlobalUtil.showToastMessage(this, "Length of password should be more than 4", GlobalConstants.TOAST_RED);
            return;
        }
        updatePasswordInFirebase(newPassword);
    }

    private void onReAuthenticationCompleted(boolean isSuccess) {
        if (!isSuccess) {
            GlobalUtil.showToastMessage(this, "You have entered wrong password", GlobalConstants.TOAST_RED);
            return;
        }
        currentStep = STEP_POST_PASSWORD_VERIFICATION;
        hideAllLayouts();
        if (currentUpdateIndex == UPDATE_EMAIL_INDEX) {
            switchOnEmailUpdateViews();
        } else {
            switchOnUpdatePasswordViews();
        }
    }


    private void updateUserNameInFirebase(String firstName, String lastName) {
        showProgressBarLayout();
        String displayName = "" + firstName + " " + lastName;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressBarLayout();
                        if (task.isSuccessful()) {
                            PreferenceManager.updateUserName(UpdateProfileActivity.this, displayName);
                            GlobalUtil.showToastMessage(UpdateProfileActivity.this, "Name has been updated successfully.", GlobalConstants.TOAST_DARK);
                        } else {
                            GlobalUtil.showToastMessage(UpdateProfileActivity.this, "Name could not be updated.", GlobalConstants.TOAST_RED);

                        }
                    }
                });
    }

    private void reAuthenticateUser(String email, String password) {
        showProgressBarLayout();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressBarLayout();
                onReAuthenticationCompleted(task.isSuccessful());
            }
        });
    }

    private void updateEmailInFirebase(String email) {
        showProgressBarLayout();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressBarLayout();
                        if (task.isSuccessful()) {
                            PreferenceManager.updateUserEmail(UpdateProfileActivity.this, email);
                            GlobalUtil.showToastMessage(UpdateProfileActivity.this, "Email has been updated successfully.", GlobalConstants.TOAST_DARK);
                        } else {
                            GlobalUtil.showToastMessage(UpdateProfileActivity.this, "Email could not be updated.", GlobalConstants.TOAST_RED);
                        }
                    }
                });
    }

    private void updatePasswordInFirebase(String newPassword) {
        showProgressBarLayout();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressBarLayout();
                        if (task.isSuccessful()) {
                            GlobalUtil.showToastMessage(UpdateProfileActivity.this, "Password has been updated successfully.", GlobalConstants.TOAST_DARK);
                        } else {
                            GlobalUtil.showToastMessage(UpdateProfileActivity.this, "Password could not be updated.", GlobalConstants.TOAST_RED);

                        }
                    }
                });
    }
}

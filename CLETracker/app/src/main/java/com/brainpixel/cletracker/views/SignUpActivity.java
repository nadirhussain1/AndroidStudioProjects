package com.brainpixel.cletracker.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.model.UserProfileDataModel;
import com.brainpixel.cletracker.utils.GlobalUtil;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.brainpixel.cletracker.views.activities.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

import static com.brainpixel.cletracker.R.id.loginPasswordEditor;

/**
 * Created by nadirhussain on 01/04/2017.
 */

public class SignUpActivity extends BaseActivity {
    @BindView(R.id.fNameEditor)
    EditText fNameEditor;
    @BindView(R.id.lNameEditor)
    EditText lNameEditor;
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(loginPasswordEditor)
    EditText passwordEditor;

    private long mLastClickTime = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.activity_signup, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();

    }

    @OnEditorAction(loginPasswordEditor)
    public boolean passwordEditAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doSignUp();
            return true;
        }
        return false;
    }

    @OnClick(R.id.SignUpButton)
    public void signUpClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        doSignUp();
    }

    private void doSignUp() {
        if (performInternetCheck()) {
            String email = emailEditor.getText().toString();
            String password = passwordEditor.getText().toString();
            String fName = fNameEditor.getText().toString();
            String lName = lNameEditor.getText().toString();

            if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.fill_all_fields_msg));
                return;
            }
            if (!GlobalUtil.isEmailValid(email)) {
                GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.invalid_email_msg));
                return;
            }

            createNewUser(email, password);
        }
    }

    private void createNewUser(String email, String password) {
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            GlobalUtil.showMessageAlertWithOkButton(SignUpActivity.this, getString(R.string.alert_message_title), getErrorMessage(task));
                        } else {
                            goToProfileScreen();
                        }
                    }
                });
    }

    private String getErrorMessage(Task<AuthResult> task) {
        String error = "";
        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            error = getString(R.string.weak_password_error);
        } catch (FirebaseAuthInvalidCredentialsException e) {
            error = getString(R.string.invalid_email_error);
        } catch (FirebaseAuthUserCollisionException e) {
            error = getString(R.string.user_exists_error);
        } catch (Exception e) {
            error = getString(R.string.signup_failed);
        }
        return error;

    }

    private void goToProfileScreen() {
        String fName = fNameEditor.getText().toString();
        String lName = lNameEditor.getText().toString();
        UserProfileDataModel userProfileDataModel = new UserProfileDataModel();
        userProfileDataModel.setFirstName(fName);
        userProfileDataModel.setLastName(lName);

        Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_USER_PROFILE_DATA, userProfileDataModel);
        startActivity(intent);
        finish();
    }
}

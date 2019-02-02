package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.storage.PreferenceManager;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 15/07/2018.
 */

public class CodeVerificationActivity extends Activity {
    public static final String KEY_MOBILE_NUMBER = "KEY_MOBILE_NUMBER";

    @BindView(R.id.guideView)
    TextView guideTextView;
    @BindView(R.id.digitOneEditor)
    EditText digitOneEditor;
    @BindView(R.id.digitTwoEditor)
    EditText digitTwoEditor;
    @BindView(R.id.digitThreeEditor)
    EditText digitThreeEditor;
    @BindView(R.id.digitFourEditor)
    EditText digitFourEditor;
    @BindView(R.id.digitFiveEditor)
    EditText digitFiveEditor;
    @BindView(R.id.digitSixEditor)
    EditText digitSixEditor;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    @BindView(R.id.resendStaticTextView)
    TextView resendCodeTextView;
    @BindView(R.id.timerTextView)
    TextView timerTextView;


    private String mobileNumber;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private boolean isNumberBeingUpdated = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);
        setFocusListenerToCodeEditors();

        if (getIntent() != null) {
            mobileNumber = getIntent().getStringExtra(KEY_MOBILE_NUMBER);
            isNumberBeingUpdated = getIntent().getBooleanExtra(NumberEditorActivity.KEY_UPDATE_FLAG, false);
        }
        mAuth = FirebaseAuth.getInstance();
        startPhoneVerificationProcess(mobileNumber);
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.code_verification_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void setFocusListenerToCodeEditors() {
        digitOneEditor.addTextChangedListener(new CodeTextWatcher(digitOneEditor));
        digitTwoEditor.addTextChangedListener(new CodeTextWatcher(digitTwoEditor));
        digitThreeEditor.addTextChangedListener(new CodeTextWatcher(digitThreeEditor));
        digitFourEditor.addTextChangedListener(new CodeTextWatcher(digitFourEditor));
        digitFiveEditor.addTextChangedListener(new CodeTextWatcher(digitFiveEditor));

        digitSixEditor.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onDone();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @OnClick(R.id.editNumberTextView)
    public void onEditNumberClick() {
        goBack();
    }

    @OnClick(R.id.backClickAreaLayout)
    public void onBackClick() {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, NumberEditorActivity.class);
        intent.putExtra(NumberEditorActivity.KEY_UPDATE_FLAG, isNumberBeingUpdated);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.resendStaticTextView)
    public void onResendClick() {
        resendVerificationCode(mobileNumber, mResendToken);
    }

    @OnClick(R.id.doneButton)
    public void onDone() {
        String digitOne = digitOneEditor.getText().toString();
        String digitTwo = digitTwoEditor.getText().toString();
        String digitThree = digitThreeEditor.getText().toString();
        String digitFour = digitFourEditor.getText().toString();
        String digitFive = digitFiveEditor.getText().toString();
        String digitSix = digitSixEditor.getText().toString();

        String enteredCodeText = "" + digitOne + "" + digitTwo + "" + digitThree + "" + digitFour + "" + digitFive + "" + digitSix;
        if (enteredCodeText.length() < 6) {
            GlobalUtil.showToastMessage(this, getString(R.string.incomplete_code_error), GlobalConstants.TOAST_RED);
            return;
        }
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }
        verifyCode(mVerificationId, enteredCodeText);
    }

    private void startPhoneVerificationProcess(String phoneNumber) {
        String guideText = getString(R.string.four_digit_text) + " <b>" + mobileNumber + "</b>";
        guideTextView.setText(Html.fromHtml(guideText));

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, GlobalConstants.NUMBER_VERIFICATION_TIME_INSEC, TimeUnit.SECONDS, this,
                phoneVerificationStateCallback);

        resetResendCounter();

    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                GlobalConstants.NUMBER_VERIFICATION_TIME_INSEC,
                TimeUnit.SECONDS,
                this,
                phoneVerificationStateCallback,
                token);

        resetResendCounter();

    }

    private void verifyCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        onCodeVerificationDone(credential);
    }

    private void resetResendCounter() {
        int totalTimeOfCounter = GlobalConstants.NUMBER_VERIFICATION_TIME_INSEC * 1000;

        resendCodeTextView.setClickable(false);
        ResendCounter resendCounter = new ResendCounter(totalTimeOfCounter, 1000);
        resendCounter.start();
    }

    OnVerificationStateChangedCallbacks phoneVerificationStateCallback = new OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            onCodeVerificationDone(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                GlobalUtil.showCustomizedAlert(CodeVerificationActivity.this, getString(R.string.invalid_mobile_number_title), getString(R.string.invalid_mobile_number_msg), getString(R.string.ok_label));
            } else if (e instanceof FirebaseTooManyRequestsException) {
                GlobalUtil.showCustomizedAlert(CodeVerificationActivity.this, getString(R.string.quota_exceeded_title), getString(R.string.quota_exceeded_msg), getString(R.string.ok_label));
            }
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId = verificationId;
            mResendToken = token;

            GlobalUtil.showToastMessage(CodeVerificationActivity.this, getString(R.string.code_sent_msg), GlobalConstants.TOAST_DARK);
        }
    };

    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void onCodeVerificationDone(PhoneAuthCredential credential) {
        if (isNumberBeingUpdated) {
            FirebaseAuth.getInstance().getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    PreferenceManager.updateUserMobile(CodeVerificationActivity.this, mobileNumber);
                    GlobalUtil.showToastMessage(CodeVerificationActivity.this, "Your mobile number has been updated successfully.", GlobalConstants.TOAST_DARK);
                    finish();
                }
            });
        } else {
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showProgressBarLayout();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBarLayout();
                        if (!task.isSuccessful()) {
                            GlobalUtil.showCustomizedAlert(CodeVerificationActivity.this, "Error", task.getException().getMessage(), getString(R.string.ok_label));
                            return;
                        }

                        FirebaseUser user = task.getResult().getUser();
                        String savedUserEmail = user.getEmail();
                        if (savedUserEmail == null || savedUserEmail.isEmpty()) {
                            launchProfileScreen();
                        } else {
                            PreferenceManager.saveUserInfo(CodeVerificationActivity.this, user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
                            launchPasswordScreen();
                        }

                    }
                });
    }

    private void launchProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void launchPasswordScreen() {
        Intent intent = new Intent(this, PasswordInputActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private class ResendCounter extends CountDownTimer {

        public ResendCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerTextView.setText(" in " + getRemainingTimeInSeconds(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            timerTextView.setText("");
            resendCodeTextView.setTextColor(Color.parseColor("#3F7FA9"));
            resendCodeTextView.setClickable(true);
        }
    }

    private String getRemainingTimeInSeconds(long millisUntilFinished) {
        long seconds = millisUntilFinished / 1000;
        if (seconds >= 10) {
            return "00:" + seconds;
        } else {
            return "00:0" + seconds;
        }
    }

    private class CodeTextWatcher implements TextWatcher {
        private EditText editText;

        public CodeTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() >= 1) {
                switch (editText.getId()) {
                    case R.id.digitOneEditor:
                        digitTwoEditor.requestFocus();
                        break;
                    case R.id.digitTwoEditor:
                        digitThreeEditor.requestFocus();
                        break;
                    case R.id.digitThreeEditor:
                        digitFourEditor.requestFocus();
                        break;
                    case R.id.digitFourEditor:
                        digitFiveEditor.requestFocus();
                        break;
                    default:
                        digitSixEditor.requestFocus();
                        break;

                }
            }
        }
    }
}


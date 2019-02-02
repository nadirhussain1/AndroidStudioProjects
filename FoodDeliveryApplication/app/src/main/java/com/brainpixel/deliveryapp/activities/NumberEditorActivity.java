package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by nadirhussain on 15/07/2018.
 */

public class NumberEditorActivity extends Activity {
    public static final String KEY_UPDATE_FLAG="KEY_UPDATE_FLAG";

    @BindView(R.id.guideView)
    TextView guideInfoTextView;
    @BindView(R.id.numberInputEditor)
    EditText numberEditor;
    @BindView(R.id.doneButton)
    Button doneButton;

    private int previousLengthOfNumber = 0;
    private boolean isNumberBeingUpdated=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null){
            isNumberBeingUpdated=getIntent().getBooleanExtra(KEY_UPDATE_FLAG,false);
        }
        attachView();
        handleViewChanges();

    }
    private void attachView(){
        View view = LayoutInflater.from(this).inflate(R.layout.number_editor_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this);
    }
    private void handleViewChanges(){
        if(isNumberBeingUpdated){
            guideInfoTextView.setText("Enter new mobile number");
            doneButton.setText("Update");
        }
        numberEditor.addTextChangedListener(new DigitTextWatcher());
    }

    @OnClick(R.id.backClickAreaLayout)
    public void onBackClick() {
        finish();
    }

    @OnEditorAction(R.id.numberInputEditor)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onDone();
            return true;
        }
        return false;
    }

    @OnClick(R.id.doneButton)
    public void onDone() {
        String mobileNumber = numberEditor.getText().toString();

        if (mobileNumber.length() != 11) {
            GlobalUtil.showToastMessage(this, getString(R.string.mobile_number_length_error), GlobalConstants.TOAST_RED);
            return;
        }
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showToastMessage(this, getString(R.string.connectivity_error), GlobalConstants.TOAST_RED);
            return;
        }
        launchCodeVerificationScreen(mobileNumber);
    }

    private void launchCodeVerificationScreen(String mobileNumber) {
        String numberWithCode = "+92 " + mobileNumber;

        Intent intent = new Intent(this, CodeVerificationActivity.class);
        intent.putExtra(CodeVerificationActivity.KEY_MOBILE_NUMBER, numberWithCode);
        intent.putExtra(KEY_UPDATE_FLAG, isNumberBeingUpdated);
        startActivity(intent);

        finish();
    }

    private class DigitTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 3 && previousLengthOfNumber == 2) {
                numberEditor.append(" ");
            }
            if (s.length() == 11) {
                doneButton.setBackgroundResource(R.drawable.green_rounded_bg);
                doneButton.setTextColor(Color.WHITE);
            } else {
                doneButton.setBackgroundResource(R.drawable.disabled_green_rounded_bg);
                doneButton.setTextColor(Color.parseColor("#AAFFFFFF"));
            }
            previousLengthOfNumber = s.length();
        }
    }


}

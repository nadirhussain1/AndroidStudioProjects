package com.brainpixel.valetapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainpixel.valetapp.BaseActivity;
import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.interfaces.AlertDialogSingleButtonListener;
import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.storage.ValetPreferences;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 11/03/2017.
 */

public class RegisterActivity extends BaseActivity implements AlertDialogSingleButtonListener {
    @BindView(R.id.flagIconView)
    ImageView flagIconView;
    @BindView(R.id.mobileNumberEditor)
    EditText mobileNumberEdiotr;
    @BindView(R.id.fNameEditor)
    EditText fNameEditor;
    @BindView(R.id.lNameEditor)
    EditText lNameEditor;
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(R.id.mobileCodeTexView)
    TextView mobileCodeTextView;
    @BindView(R.id.passwordEditor)
    EditText passworEditor;
    @BindView(R.id.errorCodeTextView)
    TextView errorMessageView;

    private String selectedCountryDialCode = "+92";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_register, null);
        new ScalingUtility(this).scaleRootView(view);
        ButterKnife.bind(this, view);
        setContentView(view);
        initCountryValues();
    }

    private void initCountryValues() {
        CountryPicker picker = CountryPicker.newInstance("Select a Country");
        Country country = picker.getUserCountryInfo(this);
        updateLabels(country.getDialCode(), country.getFlag());

    }

    private void updateLabels(String dialCode, int flagResID) {
        selectedCountryDialCode = dialCode;
        flagIconView.setImageResource(flagResID);
        mobileCodeTextView.setText(selectedCountryDialCode);
    }


    public void openCountryPicker() {
        final CountryPicker picker = CountryPicker.newInstance("Select a Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                picker.dismiss();
                updateLabels(dialCode, flagDrawableResID);
            }
        });
    }

    @OnClick(R.id.uberLogoView)
    public void uberLogoClicked() {
        backClicked();
    }

    @OnClick(R.id.backArrowHeadView)
    public void backArrowClicked() {
        backClicked();
    }

    @OnClick(R.id.flagIconView)
    public void flagIconClicked() {
        openCountryPicker();
    }

    @OnClick(R.id.downArrow)
    public void downArrowClicked() {
        openCountryPicker();
    }

    @OnFocusChange(R.id.emailEditor)
    public void emailFocus() {
        hideErrorMessage();
    }

    @OnFocusChange(R.id.fNameEditor)
    public void fNameFocus() {
        hideErrorMessage();
    }

    @OnFocusChange(R.id.lNameEditor)
    public void lNameFocus() {
        hideErrorMessage();
    }

    @OnFocusChange(R.id.mobileNumberEditor)
    public void mobileFocus() {
        hideErrorMessage();
    }

    @OnFocusChange(R.id.passwordEditor)
    public void passFocus() {
        hideErrorMessage();
    }
    @OnEditorAction(R.id.passwordEditor)
    public boolean onDoneAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doRegister();
            return true;
        }
        return false;
    }

    private void hideErrorMessage() {
        errorMessageView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(String message) {
        errorMessageView.setText(message);
        errorMessageView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.nextButton)
    public void registerButtonClicked() {
        doRegister();
    }
    private void doRegister(){
        String fName = fNameEditor.getText().toString();
        String lName = lNameEditor.getText().toString();
        String email = emailEditor.getText().toString();
        String password = passworEditor.getText().toString();
        String mobileCode = mobileCodeTextView.getText().toString();
        String mobileNumberInput = mobileNumberEdiotr.getText().toString();


        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || password.isEmpty() || mobileNumberInput.isEmpty()) {
            showErrorMessage(getString(R.string.empty_field_error));
            return;
        }
        if (!GlobalUtil.isEmailValid(email)) {
            showErrorMessage(getString(R.string.invalid_email));
            return;
        }
        if (password.length() < 5) {
            showErrorMessage(getString(R.string.password_length_error));
            return;
        }

        if (performInternetCheck()) {
            String mobile = mobileCode + "-" + mobileNumberInput;
            doRegistrationEndPointCall(fName, lName, email, password, mobile, GlobalDataManager.PASSENGER_ROLE_ID);
        }
    }

    public void backClicked() {
        finish();
    }

    private void doRegistrationEndPointCall(String fName, String lName, String email, String pass, String mobileNUmber, int role) {
        showProgressDialog();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.registerWebUser(fName, lName, email, pass, mobileNUmber, role);
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<GeneralServerResponse>() {
            @Override
            public void onResponse(Call<GeneralServerResponse> call, Response<GeneralServerResponse> response) {
                hideProgressDialog();
                GeneralServerResponse generalServerResponse = response.body();
                if (generalServerResponse.getError()) {
                    displayMessageAlert(generalServerResponse.getMessage());
                    return;
                }

                ValetPreferences.savedLoggedInStatus(RegisterActivity.this, 1);
                showSuccessMessage(generalServerResponse.getMessage());
            }

            @Override
            public void onFailure(Call<GeneralServerResponse> call, Throwable t) {
                hideProgressDialog();
                if (t != null) {
                    displayMessageAlert(t.toString());
                }
            }
        });
    }

    private void displayMessageAlert(String error) {
        GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_error_title), error);
    }

    private void showSuccessMessage(String message) {
        GlobalUtil.showAlertMessageWithSingleButton(this, getString(R.string.alert_message_title), message, this, 100);
    }


    @Override
    public void alertOkPressed(int code) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.brainpixel.valetapp.views.activities;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainpixel.valetapp.BaseActivity;
import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.core.PickDocumentSourceHandler;
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
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 11/03/2017.
 */

public class RegisterActivity extends BaseActivity implements AlertDialogSingleButtonListener {
    private String[] PERMISSIONS_REQUIRED = new String[]{permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String ID_CARD_PREFIX = "identity_card";
    private static final String DRIVING_LICENSE_PREFIX = "driving_license";
    private static final int PERMISSION_REQUEST_CODE = 150;
    private static final int ALERT_SUCCESS_CODE = 90;
    private static final int ALERT_PERMISSION_CODE = 100;

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
    @BindView(R.id.cnicPictureIconView)
    ImageView idCardImageView;
    @BindView(R.id.licenseIconView)
    ImageView licenseIconView;

    private String selectedCountryDialCode = "+92";
    private PickDocumentSourceHandler pickDocumentSourceHandler;
    private long mLastClickTime = 0;

    private Uri imageOutputUri = null;
    private String idCardFilePath = null;
    private String licenseFilePath = null;
    private int clickedIconViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_register, null);
        new ScalingUtility(this).scaleRootView(view);
        ButterKnife.bind(this, view);
        setContentView(view);
        initCountryValues();
        pickDocumentSourceHandler = new PickDocumentSourceHandler(this);
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

    private void doRegister() {
        String fName = fNameEditor.getText().toString();
        String lName = lNameEditor.getText().toString();
        String email = emailEditor.getText().toString();
        String password = passworEditor.getText().toString();
        String mobileCode = mobileCodeTextView.getText().toString();
        String mobileNumberInput = mobileNumberEdiotr.getText().toString();


        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || password.isEmpty() || mobileNumberInput.isEmpty() || TextUtils.isEmpty(idCardFilePath) || TextUtils.isEmpty(licenseFilePath)) {
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
            String mobile = mobileCode + "" + mobileNumberInput;
            doRegistrationEndPointCall(fName, lName, email, password, mobile, GlobalDataManager.DRIVER_ROLE_ID);
        }
    }

    public void backClicked() {
        finish();
    }


    private void doRegistrationEndPointCall(String fName, String lName, String email, String pass, String mobileNUmber, int role) {
        showProgressDialog();


        File file = new File(licenseFilePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part licensePicBody = MultipartBody.Part.createFormData("u_driving_license_pic", file.getName(), reqFile);

        file = new File(idCardFilePath);
        reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part idCardBody = MultipartBody.Part.createFormData("u_identity_card_pic", file.getName(), reqFile);


        RequestBody fNameBody = RequestBody.create(MediaType.parse("text/plain"), fName);
        RequestBody lNameBody = RequestBody.create(MediaType.parse("text/plain"), lName);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), pass);
        RequestBody mobileNumberBody = RequestBody.create(MediaType.parse("text/plain"), mobileNUmber);
        RequestBody roleBody = RequestBody.create(MediaType.parse("text/plain"), "" + role);

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralServerResponse> call = apiService.registerWebUser(idCardBody, licensePicBody, fNameBody, lNameBody, emailBody, passBody, mobileNumberBody, roleBody);
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
        GlobalUtil.showAlertMessageWithSingleButton(this, getString(R.string.alert_message_title), message, this, ALERT_SUCCESS_CODE);
    }

    @OnClick(R.id.licenseIconView)
    public void licenseIconViewClicked() {
        clickedIconViewId = R.id.licenseIconView;
        startImagePickerTask();
    }

    @OnClick(R.id.cnicPictureIconView)
    public void CnicIconViewClicked() {
        clickedIconViewId = R.id.cnicPictureIconView;
        startImagePickerTask();
    }

    private void startImagePickerTask() {
        if (hasPermissions(PERMISSIONS_REQUIRED)) {
            initImagePickerDialog();
        } else {
            GlobalUtil.showAlertMessageWithSingleButton(this, getString(R.string.alert_message_title), getString(R.string.general_permissions_msg), this, ALERT_PERMISSION_CODE);
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSION_REQUEST_CODE);
    }

    private void initImagePickerDialog() {
        String prefix = ID_CARD_PREFIX;
        if (clickedIconViewId == R.id.licenseIconView) {
            prefix = DRIVING_LICENSE_PREFIX;
        }
        imageOutputUri = getPicUrl(prefix);
        pickDocumentSourceHandler.openImageSourceChooseDialog(imageOutputUri);
    }

    private Uri getPicUrl(String imagePrefix) {
        final File imageDirecotry = GlobalUtil.createImageTempFile(this, imagePrefix, ".png");
        return Uri.fromFile(imageDirecotry);
    }

    private boolean hasPermissions(String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void alertOkPressed(int code) {
        if (code == ALERT_SUCCESS_CODE) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            requestPermissions();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length==2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                initImagePickerDialog();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PickDocumentSourceHandler.PICK_DOCUMENT_REQ_CODE) {
            String filePath = handlePictureIntentResults(data);
            if (clickedIconViewId == R.id.licenseIconView) {
                licenseFilePath = filePath;
            } else {
                idCardFilePath = filePath;
            }
        }
    }

    private String handlePictureIntentResults(Intent data) {
        String selectedImageFilePath = null;
        Uri imageUri = null;
        if (data == null || data.getData() == null || isResultFromCamera(data)) {
            imageUri = imageOutputUri;
            selectedImageFilePath = imageOutputUri.getPath();
        } else if (data != null) {
            imageUri = data.getData();
            selectedImageFilePath = getRealPathOfGalleryImage(data.getData());
        }

        if (selectedImageFilePath != null) {
            if (clickedIconViewId == R.id.licenseIconView) {
                resizeImageThread(imageUri, selectedImageFilePath, licenseIconView);
            } else {
                resizeImageThread(imageUri, selectedImageFilePath, idCardImageView);
            }
        }
        return selectedImageFilePath;
    }

    private void resizeImageThread(final Uri imageUri, final String selectedImageFilePath, final ImageView imageView) {
        Thread resizeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GlobalUtil.resizeImageFile(selectedImageFilePath, 400, 400);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int width = (int) ScalingUtility.resizeDimension(400);
                        Picasso.with(RegisterActivity.this).load(imageUri).into(imageView);
                    }
                });
            }
        });
        resizeThread.start();
    }

    private boolean isResultFromCamera(Intent data) {
        boolean isCamera;
        if (data != null && data.getAction() == null) {
            isCamera = false;
        } else if (data == null) {
            isCamera = true;
        } else {
            isCamera = data.getAction().equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera;
    }

    private String getRealPathOfGalleryImage(Uri selectedImageUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }
}

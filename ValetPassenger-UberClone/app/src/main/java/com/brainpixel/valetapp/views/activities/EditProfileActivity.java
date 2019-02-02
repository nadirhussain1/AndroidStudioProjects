package com.brainpixel.valetapp.views.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainpixel.valetapp.BaseActivity;
import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.core.PickDocumentSourceHandler;
import com.brainpixel.valetapp.interfaces.AlertDialogSingleButtonListener;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.model.login.LoggedInUserData;
import com.brainpixel.valetapp.model.login.LoginResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.storage.ValetDbManager;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.PiccasoCircleTransformation;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 14/05/2017.
 */

public class EditProfileActivity extends BaseActivity implements AlertDialogSingleButtonListener {
    private String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_REQUEST_CODE = 150;
    private static final String PROFILE_PIC = "profile_pic";

    @BindView(R.id.profileIconView)
    ImageView profileImageView;
    @BindView(R.id.fNameEditor)
    EditText fNameEditor;
    @BindView(R.id.lNameEditor)
    EditText lNameEditor;
    @BindView(R.id.emailEditor)
    EditText emailEditor;
    @BindView(R.id.flagIconView)
    ImageView flagIconView;
    @BindView(R.id.mobileNumberEditor)
    EditText mobileNumberEditor;
    @BindView(R.id.mobileCodeTexView)
    TextView mobileCodeTextView;
    @BindView(R.id.passwordEditor)
    EditText passworEditor;

    private String selectedCountryDialCode = "+92";
    private Uri imageOutputUri = null;
    private PickDocumentSourceHandler pickDocumentSourceHandler;
    private String selectedFilePath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.edit_profile, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);
        setValuesToViews();
        pickDocumentSourceHandler = new PickDocumentSourceHandler(this);
    }

    private void setValuesToViews() {
        LoggedInUserData loggedInUserData = GlobalDataManager.getInstance().loggedInUserData;
        if (!TextUtils.isEmpty(loggedInUserData.getProfileImageUrl())) {
            Picasso.with(this).load(loggedInUserData.getProfileImageUrl()).placeholder(R.drawable.default_profile_icon).transform(new PiccasoCircleTransformation()).into(profileImageView);
        } else {
            Picasso.with(this).load(R.drawable.default_profile_icon).transform(new PiccasoCircleTransformation()).into(profileImageView);
        }

        fNameEditor.append(loggedInUserData.getUFirstName());
        lNameEditor.append(loggedInUserData.getULastName());
        emailEditor.append(loggedInUserData.getUEmailAddress());
        passworEditor.append(loggedInUserData.getuPassword());

        CountryPicker picker = CountryPicker.newInstance("Select a Country");
        Country country = picker.getUserCountryInfo(this);
        updateMobileLabels(country.getDialCode(), country.getFlag());
        if (loggedInUserData.getUMobileNo().contains("-")) {
            int dashIndex = loggedInUserData.getUMobileNo().indexOf("-");
            mobileNumberEditor.setText(loggedInUserData.getUMobileNo().substring(dashIndex + 1));
        }
    }


    private void updateMobileLabels(String dialCode, int flagResID) {
        selectedCountryDialCode = dialCode;
        flagIconView.setImageResource(flagResID);
        mobileCodeTextView.setText(selectedCountryDialCode);
    }

    @OnClick(R.id.backClickArea)
    public void backArrowClicked() {
        finish();
    }

    @OnClick(R.id.profileIconView)
    public void onProfileIconClicked() {
        startImagePickerTask();
    }

    @OnClick(R.id.editCirlceIconView)
    public void profileEditCircleClicked() {
        startImagePickerTask();
    }

    @OnClick(R.id.flagIconView)
    public void flagIconClicked() {
        openCountryPicker();
    }

    @OnClick(R.id.downArrow)
    public void downArrowClicked() {
        openCountryPicker();
    }

    @OnClick(R.id.saveButton)
    public void saveButtonClicked() {
        String fName = fNameEditor.getText().toString();
        String lName = lNameEditor.getText().toString();
        String email = emailEditor.getText().toString();
        String password = passworEditor.getText().toString();
        String mobileCode = mobileCodeTextView.getText().toString();
        String mobileNumberInput = mobileNumberEditor.getText().toString();


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
            String uId = GlobalDataManager.getInstance().loggedInUserData.getUserId();
            String mobile = mobileCode + "-" + mobileNumberInput;
            doUpdateProfileApiCall(uId, fName, lName, email, password, mobile);
        }


    }


    private void showErrorMessage(String message) {
        GlobalUtil.displayToastInfoMessage(this, message);
    }

    public boolean performInternetCheck() {
        if (!GlobalUtil.isInternetConnected(this)) {
            GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.no_connection_title), getString(R.string.no_connection_msg));
            return false;
        }
        return true;
    }

    public void openCountryPicker() {
        final CountryPicker picker = CountryPicker.newInstance("Select a Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                picker.dismiss();
                updateMobileLabels(dialCode, flagDrawableResID);
            }
        });
    }

    private void startImagePickerTask() {
        if (hasPermissions(PERMISSIONS_REQUIRED)) {
            initImagePickerDialog();
        } else {
            GlobalUtil.showAlertMessageWithSingleButton(this, getString(R.string.alert_message_title), getString(R.string.general_permissions_msg), this, 100);
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSION_REQUEST_CODE);
    }

    private void initImagePickerDialog() {
        imageOutputUri = getPicUrl(PROFILE_PIC);
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
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initImagePickerDialog();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PickDocumentSourceHandler.PICK_DOCUMENT_REQ_CODE) {
            selectedFilePath = handlePictureIntentResults(data);
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
            resizeImageThread(imageUri, selectedImageFilePath, profileImageView);

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
                        Picasso.with(EditProfileActivity.this).load(imageUri).resize(width, width).transform(new PiccasoCircleTransformation()).into(imageView);
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

    private void displayMessageAlert(String error) {
        GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_error_title), error);
    }

    private void onProfileUpdated(LoginResponse loginResponse) {
        ValetDbManager.deleteLoggedInUser(GlobalDataManager.getInstance().loggedInUserData.getUserId());
        ValetDbManager.saveLoggedInUser(loginResponse.getData());
        GlobalDataManager.getInstance().loggedInUserData = loginResponse.getData();
        finish();
    }

    private void doUpdateProfileApiCall(String uId, String fName, String lName, String email, String pass, String mobileNUmber) {
        showProgressDialog();

        MultipartBody.Part imageBody = null;
        if (selectedFilePath != null) {
            File file = new File(selectedFilePath);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            imageBody = MultipartBody.Part.createFormData("user_image", file.getName(), reqFile);
        }

        RequestBody uIdBody = RequestBody.create(MediaType.parse("text/plain"), uId);
        RequestBody fNameBody = RequestBody.create(MediaType.parse("text/plain"), fName);
        RequestBody lNameBody = RequestBody.create(MediaType.parse("text/plain"), lName);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), pass);
        RequestBody mobileNumberBody = RequestBody.create(MediaType.parse("text/plain"), mobileNUmber);


        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<LoginResponse> call = apiService.editUserProfile(imageBody, uIdBody, fNameBody, lNameBody, passBody, mobileNumberBody);
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideProgressDialog();
                LoginResponse loginResponse = response.body();
                if (loginResponse.getError()) {
                    displayMessageAlert(loginResponse.getMessage());
                    return;
                }
                onProfileUpdated(loginResponse);


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideProgressDialog();
                if (t != null) {
                    displayMessageAlert(t.toString());
                }
            }
        });
    }


}

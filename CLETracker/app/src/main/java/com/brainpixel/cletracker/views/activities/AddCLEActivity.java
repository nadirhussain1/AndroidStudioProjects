package com.brainpixel.cletracker.views.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.core.PickDocumentSourceHandler;
import com.brainpixel.cletracker.interfaces.AlertDialogSingleButtonListener;
import com.brainpixel.cletracker.interfaces.NewCleAdded;
import com.brainpixel.cletracker.model.CLEDataModel;
import com.brainpixel.cletracker.model.Cycle;
import com.brainpixel.cletracker.utils.GlobalConstants;
import com.brainpixel.cletracker.utils.GlobalUtil;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 05/04/2017.
 */

public class AddCLEActivity extends BaseActivity implements AlertDialogSingleButtonListener {
    public static final String KEY_USER_CYCLE = "KEY_USER_CYCLE";

    private String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String PROFILE_PIC_PREFIX = "cle_certificate";
    private static final int PERMISSION_REQUEST = 150;
    private Uri imageOutputUri = null;
    private String selectedImageFilePath = null;

    @BindView(R.id.titleEditor)
    EditText cleTitleEditor;
    @BindView(R.id.dateValueTextView)
    TextView dateValueTextView;
    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;
    @BindView(R.id.creditsEditor)
    EditText creditsEditor;
    @BindView(R.id.certificateImageView)
    ImageView certificateImageView;
    @BindView(R.id.addCertificateTextView)
    TextView addCertificateButton;

    private int sYear, sMonth, sDay = -1;
    private String dateValue = "";
    private PickDocumentSourceHandler pickDocumentSourceHandler;
    private long mLastClickTime = 0;

    private Cycle userCurrentCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            userCurrentCycle = (Cycle) getIntent().getSerializableExtra(KEY_USER_CYCLE);
        }

        View view = View.inflate(this, R.layout.add_new_cle, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);

        initDate();
        initCategoryAdapter();
        pickDocumentSourceHandler = new PickDocumentSourceHandler(this);
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        sYear = calendar.get(Calendar.YEAR);
        sMonth = calendar.get(Calendar.MONTH) + 1;
        sDay = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void initCategoryAdapter() {
        CategoriesSpinnerAdapter categoriesSpinnerAdapter = new CategoriesSpinnerAdapter(this, userCurrentCycle.getRequirements().getRequiredCategories());
        categorySpinner.setAdapter(categoriesSpinnerAdapter);
    }

    @OnClick(R.id.backIconView)
    public void backIconClicked() {
        finish();
    }

    @OnClick(R.id.dateValueTextView)
    public void dateClicked() {
        showDateDialog();
    }

    private void showDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, sYear, sMonth, sDay);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!TextUtils.isEmpty(userCurrentCycle.getMinCalendarDate())) {
            Date minDate = GlobalUtil.parseStringWithoutTimeToDate(userCurrentCycle.getMinCalendarDate());
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
        }
        Date endDate = GlobalUtil.parseStringWithoutTimeToDate(userCurrentCycle.getEndDate());
        datePickerDialog.getDatePicker().setMaxDate(endDate.getTime());
        datePickerDialog.show();
    }


    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            sYear = year;
            sMonth = month;
            sDay = day;

            dateValue = (sMonth + 1) + "/" + sDay + "/" + sYear;
            dateValueTextView.setText(dateValue);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            initImagePickerDialog();
        }
    }

    private void startImagePickerTask() {
        if (hasPermissions(PERMISSIONS_REQUIRED)) {
            initImagePickerDialog();
        } else {
            GlobalUtil.showAlertMessageWithSingleButton(this, getString(R.string.alert_message_title), getString(R.string.general_permissions_msg), this, 100);
        }
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

    private void initImagePickerDialog() {
        imageOutputUri = getProfilePicUri();
        pickDocumentSourceHandler.openImageSourceChooseDialog(imageOutputUri);
    }


    @Override
    protected void onDestroy() {
        deleteProfilePicLocalFiles(GlobalUtil.getStorageDirectory(this));
        super.onDestroy();
    }

    private void deleteProfilePicLocalFiles(File file) {
        if (file.isDirectory()) {
            for (File c : file.listFiles()) {
                deleteProfilePicLocalFiles(c);
            }
        } else if (file.getAbsolutePath().contains(PROFILE_PIC_PREFIX)) {
            if (!file.delete()) {
                new FileNotFoundException("Failed to delete file: " + file);
            }
        }
    }

    private Uri getProfilePicUri() {
        final File imageDirecotry = GlobalUtil.createImageTempFile(this, PROFILE_PIC_PREFIX, ".png");
        return Uri.fromFile(imageDirecotry);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PickDocumentSourceHandler.PICK_DOCUMENT_REQ_CODE) {
            handlePictureIntentResults(data);
        }
    }

    private void handlePictureIntentResults(Intent data) {
        Uri imageUri = null;
        if (data == null || data.getData() == null || isResultFromCamera(data)) {
            imageUri = imageOutputUri;
            selectedImageFilePath = imageOutputUri.getPath();
        } else if (data != null) {
            imageUri = data.getData();
            selectedImageFilePath = getRealPathOfGalleryImage(data.getData());
        }

        if (selectedImageFilePath != null) {
            resizeImageThread(imageUri);
        }
    }

    private void resizeImageThread(final Uri imageUri) {
        Thread resizeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GlobalUtil.resizeImageFile(selectedImageFilePath, GlobalConstants.DEFAULT_IMAGE_SIZE, GlobalConstants.DEFAULT_IMAGE_SIZE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addCertificateButton.setVisibility(View.GONE);
                        certificateImageView.setVisibility(View.VISIBLE);
                        int width = (int) ScalingUtility.resizeDimension(1000);
                        int height = (int) ScalingUtility.resizeDimension(1500);
                        Picasso.with(AddCLEActivity.this).load(imageUri).resize(width, height).into(certificateImageView);
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

    @Override
    public void alertOkPressed(int code) {
        requestPermissions();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSION_REQUEST);
    }

    @OnClick(R.id.addCertificateTextView)
    public void addCertificateButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startImagePickerTask();
    }

    @OnClick(R.id.certificateImageView)
    public void ceritficateImageViewClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startImagePickerTask();
    }


    @OnClick(R.id.saveButton)
    public void saveButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < GlobalUtil.MULTIPLE_CLICK_STOP_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        String cleTitle = cleTitleEditor.getText().toString();
        int position = categorySpinner.getSelectedItemPosition();
        String category = userCurrentCycle.getRequirements().getRequiredCategories().get(position).getName();
        String creditsHoursString = creditsEditor.getText().toString();
        if (TextUtils.isEmpty(cleTitle) || TextUtils.isEmpty(creditsHoursString)) {
            GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.fill_all_fields_msg));
            return;
        }

        CLEDataModel cleDataModel = new CLEDataModel();
        cleDataModel.setTitle(cleTitle);
        cleDataModel.setHours(Integer.valueOf(creditsHoursString));
        cleDataModel.setCategory(category);
        cleDataModel.setDate(dateValue);

        showProgressDialog();
        if (!TextUtils.isEmpty(selectedImageFilePath)) {
            uploadCertificateAndAddCle(cleDataModel);
        } else {
            String userIdKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdKey).child("cles").push();
            addCleToFirebaseDb(cleDataModel, databaseReference);
        }

    }

    @SuppressWarnings("VisibleForTests")
    private void uploadCertificateAndAddCle(final CLEDataModel cleDataModel) {
        try {

            String userIdKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
            InputStream stream = new FileInputStream(new File(selectedImageFilePath));
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdKey).child("cles").push();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(userIdKey).child("certificates").child(databaseReference.getKey());
            UploadTask uploadTask = storageReference.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    hideProgressDialog();
                    GlobalUtil.showMessageAlertWithOkButton(AddCLEActivity.this, getString(R.string.alert_message_title), getString(R.string.unable_to_save_data));
                }
            }).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    cleDataModel.setCertificatePath(downloadUrl);
                    addCleToFirebaseDb(cleDataModel, databaseReference);
                }
            });
        } catch (Exception e) {
            hideProgressDialog();
            GlobalUtil.showMessageAlertWithOkButton(AddCLEActivity.this, getString(R.string.alert_message_title), getString(R.string.unable_to_save_data));
        }

    }

    private void addCleToFirebaseDb(final CLEDataModel cleDataModel, DatabaseReference databaseReference) {
        databaseReference.setValue(cleDataModel, new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                if (databaseError == null) {
                    NewCleAdded newCleAdded = new NewCleAdded();
                    newCleAdded.cleDataModel = cleDataModel;
                    EventBus.getDefault().post(newCleAdded);
                    finish();
                } else {
                    hideProgressDialog();
                    GlobalUtil.showMessageAlertWithOkButton(AddCLEActivity.this, getString(R.string.alert_message_title), databaseError.getMessage());
                }
            }
        });
    }


}

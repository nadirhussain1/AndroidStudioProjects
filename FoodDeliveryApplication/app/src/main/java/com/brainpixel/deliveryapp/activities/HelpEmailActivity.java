package com.brainpixel.deliveryapp.activities;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.OnNegativeButtonClickListener;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 25/11/2018.
 */

public class HelpEmailActivity extends Activity {
    private static final int GALLERY = 1, CAMERA = 2, PERMISSION_REQUEST_CODE = 8;
    private static final String IMAGE_DIRECTORY = "/delivery";


    @BindView(R.id.contactReasonsSpinner)
    Spinner contactReasonsSpinner;
    @BindView(R.id.userInputEditor)
    EditText userInputEditor;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.uploadTextView)
    TextView uploadTextView;
    @BindView(R.id.submitButton)
    Button submitButton;


    private String attachedImagePath = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);

        userInputEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    submitButton.setBackgroundResource(R.drawable.green_rounded_bg);
                } else {
                    submitButton.setBackgroundResource(R.drawable.disabled_green_rounded_bg);
                }
            }
        });
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.contact_email_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    @OnClick(R.id.attachImageLayout)
    public void attachImageLayoutClicked() {
        handlePermissions();
    }

    @OnClick(R.id.uploadTextView)
    public void uploadTextViewClicked() {
        handlePermissions();
    }

    @OnClick(R.id.imageView)
    public void imageViewClicked() {
        handlePermissions();
    }


    @OnClick(R.id.backIconView)
    public void onArrowClicked() {
        finish();
    }

    @OnClick(R.id.titleView)
    public void onTitleClicked() {
        finish();
    }

    @OnClick(R.id.submitButton)
    public void submitEmailButtonClick() {
        String body = userInputEditor.getText().toString();
        if (body.length() == 0) {
            GlobalUtil.showToastMessage(this, "Please write message that you want to convey.", GlobalConstants.TOAST_RED);
        } else {
            String email = GlobalDataManager.getInstance().configData.getAdminEmail();
            String subject = (String) contactReasonsSpinner.getSelectedItem();
            sendEmail(email, subject, body, attachedImagePath);
        }
    }

    private void sendEmail(String email, String subject, String body, String attachmentPath) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        if (attachmentPath != null) {
            File file = new File(attachmentPath);
            if (file.exists() && file.canRead()) {
                Uri uri = Uri.fromFile(file);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            }
        }

        startActivity(Intent.createChooser(emailIntent, "Choose"));
    }

    private boolean hasPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private void handlePermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE, permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            showImagePickerDialog();
        }
    }

    private void showImagePickerDialog() {
        OnPositiveButtonClickListener galleryClickListener = new OnPositiveButtonClickListener() {
            @Override
            public void onButtonClick() {
                choosePhotoFromGallary();
            }
        };
        OnNegativeButtonClickListener cameraClickListener = new OnNegativeButtonClickListener() {
            @Override
            public void onButtonClick() {
                if (ActivityCompat.checkSelfPermission(HelpEmailActivity.this, permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera();
                } else {
                    GlobalUtil.showToastMessage(HelpEmailActivity.this, "Camera cannot be launched as you have denied required permission", GlobalConstants.TOAST_RED);
                }

            }
        };
        GlobalUtil.showCustomizedAlert(this, "Take Picture", "Take a new photo or select one from gallery.",
                "GALLERY", galleryClickListener, "CAMERA", cameraClickListener);
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void displaySelectedImage(String path) {
        attachedImagePath = path;
        uploadTextView.setVisibility(View.GONE);
        imageView.setBackgroundResource(0);
        Picasso.get().load("file://" + path).config(Bitmap.Config.RGB_565).fit().centerCrop().into(imageView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasPermissions()) {
            showImagePickerDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case GALLERY:
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        String path = saveImage(bitmap);
                        displaySelectedImage(path);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMERA:
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                String path = saveImage(thumbnail);
                displaySelectedImage(path);
                break;
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}

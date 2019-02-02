package com.brainpixel.valetapp.core;

/**
 * Created by nadirhussain on 15/03/2017.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 08/11/2016.
 */

public final class PickDocumentSourceHandler {
    public static final int PICK_DOCUMENT_REQ_CODE = 75;
    private Activity activity = null;


    public PickDocumentSourceHandler(Activity activity) {
        this.activity = activity;
    }

    public void openImageSourceChooseDialog(Uri outputFileUri) {
        List<Intent> cameraIntents = getCameraIntents(outputFileUri);
        Intent galleryIntent = getGalleryIntent();

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        activity.startActivityForResult(chooserIntent, PICK_DOCUMENT_REQ_CODE);
    }

    public void openDocumentSourceChooserDialog(Uri outputFileUri) {
        List<Intent> cameraIntents = getCameraIntents(outputFileUri);
        Intent documentsIntent = getDocumentsIntent();

        final Intent chooserIntent = Intent.createChooser(documentsIntent, "Select Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        activity.startActivityForResult(chooserIntent, PICK_DOCUMENT_REQ_CODE);
    }

    private List<Intent> getCameraIntents(Uri outputFileUri) {
        List<Intent> cameraIntents = new ArrayList<Intent>();
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }
        return cameraIntents;
    }

    private Intent getGalleryIntent() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        return galleryIntent;
    }

    public Intent getDocumentsIntent() {
        Intent documentsIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        documentsIntent.setType("*/*");
        documentsIntent.addCategory(Intent.CATEGORY_OPENABLE);
        return documentsIntent;
    }


}


package com.brainpixel.cletracker.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.interfaces.AlertDialogSingleButtonListener;
import com.brainpixel.cletracker.utils.ResizingImageUtil.ScalingLogic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nadirhussain on 21/09/2016.
 */
public final class GlobalUtil {
    public static final long MULTIPLE_CLICK_STOP_INTERVAL = 400;
    private static final String PNG_EXT = ".png";


    private GlobalUtil() {

    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivity.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static void showMessageAlertWithOkButton(final Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.ok_label), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.show();

        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public static void showAlertMessageWithSingleButton(final Activity activity, String title, String message, final AlertDialogSingleButtonListener alertDialogOkBackListener, final int reqCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertDialogOkBackListener.alertOkPressed(reqCode);
            }
        });


        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public static void printLog(String tag, String message) {
        Log.d(tag, message);
    }

    public static boolean isEmailValid(String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void resizeImageFile(String sourceFilePath, int reqWidth, int reqHeight) {
        Bitmap unscaledBitmap = ResizingImageUtil.decodeFile(sourceFilePath, reqWidth, reqHeight, ScalingLogic.FIT);
        if (unscaledBitmap != null) {
            Bitmap scaledBitmap = ResizingImageUtil.createScaledBitmap(unscaledBitmap, reqWidth, reqHeight, ScalingLogic.FIT);
            unscaledBitmap.recycle();
            Bitmap rotatedBitmap = rotateScaledBitmap(scaledBitmap, sourceFilePath);
            scaledBitmap.recycle();
            storeBitmapToFilePath(rotatedBitmap, sourceFilePath);
        }

    }

    public static File createImageTempFile(Activity activity, String imageFileName, String ext) {
        try {
            File tempImageFile = File.createTempFile(imageFileName, ext, getStorageDirectory(activity));
            return tempImageFile;
        } catch (Exception exception) {
            printLog("File", "" + exception);
        }
        return null;
    }

    public static File getStorageDirectory(Activity activity) {
        if ((ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && isExternalStorageWritable()) {
            File rctFolderDirectory = Environment.getExternalStoragePublicDirectory("CLETracker");
            if (!rctFolderDirectory.exists()) {
                rctFolderDirectory.mkdir();
            }
            return rctFolderDirectory;

        } else {
            return activity.getFilesDir();
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public static void storeBitmapToFilePath(Bitmap bitmap, String storedFilePath) {
        File imageFile = new File(storedFilePath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            if (storedFilePath.endsWith(PNG_EXT)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } else {
                bitmap.compress(CompressFormat.JPEG, 100, fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateScaledBitmap(Bitmap scaledBitmap, String photoFilePath) {

        // Read EXIF EarningDetailsData
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) scaledBitmap.getWidth() / 2, (float) scaledBitmap.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        // Return result
        return rotatedBitmap;
    }

    public static void displayToastInfoMessage(Activity activity, String toastMessage) {
        Toast toast = Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, dpToPx(activity, 100));
        toast.show();
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static Date parseStringWithoutTimeToDate(String dateString) {
        Date parsedDate = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            parsedDate = format.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

}

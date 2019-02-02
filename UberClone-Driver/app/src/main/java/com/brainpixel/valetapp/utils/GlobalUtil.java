package com.brainpixel.valetapp.utils;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.interfaces.AlertDialogSingleButtonListener;
import com.brainpixel.valetapp.interfaces.AlertDialogTwoButtonsListener;
import com.brainpixel.valetapp.utils.ResizingImageUtil.ScalingLogic;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nadirhussain on 21/09/2016.
 */
public final class GlobalUtil {
    private static final String PNG_EXT = ".png";
    public static final int MULTIPLE_CLICK_STOP_INTERVAL = 500;

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
        builder.setCustomTitle(getAlertCustomTitle(activity, title));
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
        builder.setCustomTitle(getAlertCustomTitle(activity, title));
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.ok_label), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertDialogOkBackListener.alertOkPressed(reqCode);
            }
        });


        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public static void showMessageAlertWithTwoButtons(final Activity activity, String title, String message, final AlertDialogTwoButtonsListener alertDialogTwoButtonsListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setCustomTitle(getAlertCustomTitle(activity, title));
        builder.setNegativeButton(activity.getString(R.string.no_label), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
                alertDialogTwoButtonsListener.onNegativeClick();
            }
        });
        builder.setPositiveButton(activity.getString(R.string.yes_label), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertDialogTwoButtonsListener.onPositiveClick();
            }
        });

        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private static TextView getAlertCustomTitle(Activity activity, String title) {
        TextView titleView = new TextView(activity);
        titleView.setText(title);
        titleView.setGravity(Gravity.CENTER_HORIZONTAL);
        titleView.setTextSize(25);

        if (title.contentEquals(activity.getString(R.string.alert_error_title))) {
            titleView.setTextColor(Color.RED);
        } else {
            titleView.setTextColor(Color.BLACK);
        }
        return titleView;
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static boolean isEmailValid(String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static Date parseStringWithTimeToDate(String dateString) {
        Date parsedDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            parsedDate = format.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    public static Date parseStringWithoutTimeToDate(String dateString) {
        Date parsedDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            parsedDate = format.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;
    }


    public static String formatDateToStringWithDisplayFormat(Date date) {
        String formattedDateString = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        try {
            formattedDateString = formatter.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return formattedDateString;
    }


    public static String formatLongTimeToString(long time) {
        Date date = new Date(time);
        return formatDateToStringWithDisplayFormat(date);

    }


    public static void printLog(String tag, String message) {
        Log.d(tag, message);
    }


    public static void displayToastInfoMessage(Activity activity, String toastMessage) {
        Toast toast = Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, dpToPx(activity, 100));
        toast.show();
    }


    public static String formatAmount(Double amount) {
        String formattedAmount = "0.00";
        if (amount != null) {
            formattedAmount = String.format("%.2f", amount);
        }
        return formattedAmount;
    }

    public static LatLng getCenter(List<LatLng> points) {
        double avgX = 0;
        double avgY = 0;
        double avgZ = 0;
        for (LatLng point : points) {
            double lat = Math.toRadians(point.latitude);
            double lon = Math.toRadians(point.longitude);

            double x = Math.cos(lat) * Math.cos(lon);
            double y = Math.cos(lat) * Math.sin(lon);
            double z = Math.sin(lat);

            avgX += x;
            avgY += y;
            avgZ += z;
        }

        avgX /= points.size();
        avgY /= points.size();
        avgZ /= points.size();

        double averageLon = Math.atan2(avgY, avgX);
        double hyp = Math.sqrt(avgX * avgX + avgY * avgY);
        double averageLat = Math.atan2(avgZ, hyp);

        return new LatLng(Math.toDegrees(averageLat), Math.toDegrees(averageLon));
    }

    public static List<LatLng> decodePoly(String poly) {

        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new LinkedList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng((lat / 1E5), (lng / 1E5)));
        }

        return decoded;
    }

    public static Snackbar showRedSnackBar(View rootView, String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        snackbar.show();
        return snackbar;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
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

    public static String getFileStoragePath(Activity activity, String fileNameWithExtension) {
        String mFileName = getStorageDirectory(activity) + File.separator + fileNameWithExtension;
        return mFileName;
    }

    public static File getStorageDirectory(Activity activity) {
        if ((ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(activity, permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && isExternalStorageWritable()) {
            File rctFolderDirecotry = Environment.getExternalStoragePublicDirectory("Valet");
            if (!rctFolderDirecotry.exists()) {
                rctFolderDirecotry.mkdir();
            }
            return rctFolderDirecotry;

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


}

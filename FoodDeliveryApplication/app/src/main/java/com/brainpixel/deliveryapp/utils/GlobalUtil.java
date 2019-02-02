package com.brainpixel.deliveryapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.OnNegativeButtonClickListener;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by nadirhussain on 04/05/2018.
 */

public final class GlobalUtil {


    private GlobalUtil() {

    }

    public static void printLog(String tag, String message) {
        Log.d(tag, message);
    }

    public static String getUniqueId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivity.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static void showCustomizedAlert(final Activity activity, String title, String message, String singleButtonLabel) {
        showCustomizedAlert(activity, title, message, singleButtonLabel, null);
    }

    public static void showCustomizedAlert(final Activity activity, String title, String message, String positiveLabel, OnPositiveButtonClickListener positiveClickListener) {
        showCustomizedAlert(activity, title, message, positiveLabel, positiveClickListener, null, null);
    }

    public static void showCustomizedAlert(final Activity activity, String title, String message, String positiveLabel, final OnPositiveButtonClickListener positiveClickListener, String negativeLabel, final OnNegativeButtonClickListener negativeClickListener) {
        View parentAlertView = View.inflate(activity, R.layout.custom_alert_dark_layout, null);


        new ScalingUtility(activity).scaleRootView(parentAlertView);
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(parentAlertView);

        TextView titleView = parentAlertView.findViewById(R.id.alertTitleView);
        TextView messageView = parentAlertView.findViewById(R.id.messageTextView);
        TextView singleButtonView = parentAlertView.findViewById(R.id.singleButtonView);
        LinearLayout twoButtonsLayout = parentAlertView.findViewById(R.id.twoButtonsLayout);
        TextView positiveButtonView = parentAlertView.findViewById(R.id.positiveButtonView);
        TextView negativeButtonView = parentAlertView.findViewById(R.id.negativeButtonView);

        titleView.setText(title);
        messageView.setText(message);


        if (negativeLabel != null) {
            singleButtonView.setVisibility(View.GONE);
            twoButtonsLayout.setVisibility(View.VISIBLE);

            positiveButtonView.setText(positiveLabel);
            negativeButtonView.setText(negativeLabel);

            positiveButtonView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (positiveClickListener != null) {
                        positiveClickListener.onButtonClick();
                    }
                }
            });

            negativeButtonView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (negativeClickListener != null) {
                        negativeClickListener.onButtonClick();
                    }
                }
            });


        } else {
            singleButtonView.setVisibility(View.VISIBLE);
            twoButtonsLayout.setVisibility(View.GONE);

            singleButtonView.setText(positiveLabel);
            singleButtonView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (positiveClickListener != null) {
                        positiveClickListener.onButtonClick();
                    }
                }
            });

        }
        dialog.show();


    }


    public static void showToastMessage(Activity activity, String message, int toastbackground) {
        Toast toast = new Toast(activity);
        View customToastView = View.inflate(activity, R.layout.custom_toast_layout, null);
        RelativeLayout bgLayout = customToastView.findViewById(R.id.toastBackgroundLayout);
        TextView messageTextView = customToastView.findViewById(R.id.messageTextView);

        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setView(customToastView);

        switch (toastbackground) {
            case GlobalConstants.TOAST_RED:
                bgLayout.setBackgroundResource(R.drawable.error_red_rounded_bg);
                messageTextView.setTextColor(Color.WHITE);
                break;
            case GlobalConstants.TOAST_DARK:
                bgLayout.setBackgroundResource(R.drawable.custom_toast_black_bg);
                messageTextView.setTextColor(Color.WHITE);
                break;
            default:
                bgLayout.setBackgroundResource(R.drawable.custom_alert_white_bg);
                messageTextView.setTextColor(Color.BLACK);
                break;
        }
        messageTextView.setText(message);
        toast.show();

    }

    public static Dialog showProgressDialog(Activity activity, String message) {
        View progressView = View.inflate(activity, R.layout.custom_progress_dialog, null);
        TextView messageTextView = progressView.findViewById(R.id.messageTextView);
        messageTextView.setText(message);

        new ScalingUtility(activity).scaleRootView(progressView);
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(progressView);
        dialog.show();

        return dialog;
    }

    public static int getIntValue(String stringInput) {
        int intValue = 0;
        try {
            intValue = Integer.valueOf(stringInput);
        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return intValue;
    }

    public static float getFloatValue(String stringInput) {
        float floatValue = 0;
        try {
            floatValue = Float.valueOf(stringInput);
        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return floatValue;
    }

    public static String getValue(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        String formattedDate = dateFormatter.format(date);
        return formattedDate;
    }

    public static Date parseToDate(String date) {
        Date parsedDate = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        try {
            parsedDate = dateFormatter.parse(date);
        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return parsedDate;
    }
}


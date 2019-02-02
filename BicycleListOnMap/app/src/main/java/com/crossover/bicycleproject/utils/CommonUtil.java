package com.crossover.bicycleproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.crossover.bicycleproject.R;

import java.util.regex.Pattern;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class CommonUtil {
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    // This method checks state of internet connectivity.
    // This facilitates whether we can proceed in our app or not.
    public static boolean isInternetConnected(Context ctx) {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }

    // This validates email pattern and format.
    public static boolean isValidEmail(CharSequence email) {
        //return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
   // Show Alert messages when we need focus of user.
    public static void showAlertDialog(String message,final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

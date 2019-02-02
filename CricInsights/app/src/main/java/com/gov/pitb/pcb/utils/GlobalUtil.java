package com.gov.pitb.pcb.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.navigators.AlertDialogTwoButtonsListener;

/**
 * Created by nadirhussain on 21/06/2017.
 */

public final class GlobalUtil {
    private GlobalUtil() {

    }

    public static boolean isInternetConnected(Context activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static String roundOfDecimalNumber(float num) {
        return String.format("%.2f", num);
    }

    public static void showOkAlertDialog(final Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
    public static void showMessageAlertWithTwoButtons(final Activity activity, String title, String message, final AlertDialogTwoButtonsListener alertDialogTwoButtonsListener, final int reqCode) {
        showAlertWithTwoCustomButtons(activity, title, message, "Yes", "No", alertDialogTwoButtonsListener, reqCode);
    }

    public static void showAlertWithTwoCustomButtons(final Activity activity, String title, String message, String positiveTitle, String negativeTitle, final AlertDialogTwoButtonsListener alertDialogTwoButtonsListener, final int reqCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton(negativeTitle, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
                alertDialogTwoButtonsListener.onNegativeClick(reqCode);
            }
        });
        builder.setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertDialogTwoButtonsListener.onPositiveClick(reqCode);
            }
        });

        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
    public static void printLog(String tag,String logMessage){
        Log.d(tag,logMessage);
    }
}

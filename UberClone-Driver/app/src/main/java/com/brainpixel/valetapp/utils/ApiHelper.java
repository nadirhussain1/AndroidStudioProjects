package com.brainpixel.valetapp.utils;

/**
 * Created by nadirhussain on 06/03/2017.
 */

import android.app.Activity;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.interfaces.AlertDialogSingleButtonListener;
import com.brainpixel.valetapp.network.RetryableCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nadirhussain on 08/10/2016.
 */

public final class ApiHelper {
    private static final int INVALID_AUTH_STATUS_CODE = 403;
    private static final String UNKNOWN_HOST_MSG = "Unable to resolve host";
    private static boolean areCancelled = false;


    private ApiHelper() {

    }

    public static boolean isCallSuccess(Response response) {
        int code = response.code();
        return (code >= 200 && code < 400);
    }

    public static void cancelCalls() {
        areCancelled = true;
    }

    public static <T> void enqueueWithRetry(final Activity activity, Call<T> call, final Callback<T> callback) {
        areCancelled = false;
        call.enqueue(new RetryableCallback<T>(call) {

            @Override
            public void onFinalResponse(Call<T> call, Response<T> response) {
                GlobalUtil.printLog("NetworkDebugCode", " pass=");
                if (!areCancelled) {
                    if (isValidResponse(response)) {
                        callback.onResponse(call, response);
                    } else {
                        callback.onResponse(call, null);
                        showErrorAlert(activity, response);
                    }
                }
            }

            @Override
            public void onFinalFailure(Call<T> call, Throwable t) {
                GlobalUtil.printLog("NetworkDebugCode", "Failed call=" + t.getMessage());
                if (!areCancelled) {
                    if (activity != null && t != null && isUnknowHostError(t.getMessage())) {
                        callback.onFailure(call, null);
                        GlobalUtil.showMessageAlertWithOkButton(activity, activity.getString(R.string.alert_error_title), activity.getString(R.string.alert_server_not_accessible_msg));
                    } else {
                        callback.onFailure(call, t);
                    }
                }
            }
        });
    }

    private static boolean isUnknowHostError(String message) {
        if (message != null && message.toLowerCase().trim().contains(UNKNOWN_HOST_MSG.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    public static <T> void enqueueOfflineCallsWithRetry(Call<T> call, final Callback<T> callback) {
        call.enqueue(new RetryableCallback<T>(call) {

            @Override
            public void onFinalResponse(Call<T> call, Response<T> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFinalFailure(Call<T> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    private static boolean isValidResponse(final Response response) {
        if (response == null || response.body() == null || response.code() == INVALID_AUTH_STATUS_CODE) {
            return false;
        }
        return true;
    }

    private static void showErrorAlert(final Activity activity, final Response response) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response == null || response.body() == null) {
                   // GlobalUtil.showMessageAlertWithOkButton(activity, activity.getString(R.string.alert_error_title), activity.getString(R.string.somehow_request_failed));
                    return;
                }
                if (response.code() == INVALID_AUTH_STATUS_CODE) {
                    showInvalidAuthAlert(activity);
                    return;
                }

               // GlobalUtil.showMessageAlertWithOkButton(activity, activity.getString(R.string.alert_error_title), activity.getString(R.string.alert_server_not_accessible_msg));

            }
        });
    }

    private static void showInvalidAuthAlert(final Activity activity) {
        GlobalUtil.showAlertMessageWithSingleButton(activity, activity.getString(R.string.alert_message_title), activity.getString(R.string.session_expired_msg), new AlertDialogSingleButtonListener() {
            @Override
            public void alertOkPressed(int code) {
                 activity.finish();
            }
        }, 100);
    }


}

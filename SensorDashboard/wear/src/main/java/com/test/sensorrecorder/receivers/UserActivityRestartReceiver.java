package com.test.sensorrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.sensorrecorder.services.LaunchDetectorService;

/**
 * Created by nadirhussain on 26/04/2018.
 */

public class UserActivityRestartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ActivityRestartReceiver", "LaunchDetectorService restarted");
        restartService(context);
    }

    private void restartService(Context context) {
        context.startService(new Intent(context, LaunchDetectorService.class));
    }
}

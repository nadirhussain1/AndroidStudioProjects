package com.test.sensorrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.sensorrecorder.services.SensorService;

/**
 * Created by nadirhussain on 25/04/2018.
 * This receiver is invoked when SensorService dies when application is closed or removed.
 * This receiver restarts SensorService to keep it working in the background.
 */

public class SensorServiceRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SensorServiceReceiver", "SensorService restarted");
        restartService(context);
    }

    private void restartService(Context context) {
        context.startService(new Intent(context, SensorService.class));
    }

}

package com.test.sensorrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.sensorrecorder.services.GlobalTouchService;

/**
 * Created by nadirhussain on 06/05/2018.
 */

public class GlobalTouchRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SensorServiceReceiver", "SensorService restarted");
        restartService(context);
    }
    private void restartService(Context context) {
        context.startService(new Intent(context, GlobalTouchService.class));
    }
}

package com.test.sensorrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.sensorrecorder.services.PhoneLocMonitoringService;

/**
 * Created by nadirhussain on 30/04/2018.
 * This is receiver which is called when Phone boots. It then restarts PhoneLocMonitoringService service
 * which initiates process of fetching gps locations on phone.
 */

public class PhoneLocServiceRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RestartReceiver", "PhoneLocMonitoringService restarted");
        restartService(context);
    }

    private void restartService(Context context) {
        context.startService(new Intent(context, PhoneLocMonitoringService.class));
    }
}

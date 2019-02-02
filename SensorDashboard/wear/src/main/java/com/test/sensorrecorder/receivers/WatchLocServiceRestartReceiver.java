package com.test.sensorrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nadirhussain on 25/04/2018.
 */

public class WatchLocServiceRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocationServiceReceiver", "WatchLocMonitoringService restarted");
        restartService(context);
    }

    private void restartService(Context context) {
        //context.startService(new Intent(context, WatchLocMonitoringService.class));
    }
}

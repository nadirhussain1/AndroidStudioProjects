package com.test.sensorrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.sensorrecorder.services.TestService;

/**
 * Created by nadirhussain on 10/05/2018.
 */

public class TestRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RestartReceiver", "TestService restarted");
        restartService(context);
    }

    private void restartService(Context context) {
        context.startService(new Intent(context, TestService.class));
    }
}

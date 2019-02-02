package com.brainpixel.valetapp.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.brainpixel.valetapp.interfaces.EventBusClasses.NetworkStateChanged;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nadirhussain on 08/03/2017.
 */


public class NetworkChangeReceiver extends BroadcastReceiver {
    private static long lastClickedTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (System.currentTimeMillis() - lastClickedTime > 500) {
            EventBus.getDefault().post(new NetworkStateChanged());
        }
    }
}

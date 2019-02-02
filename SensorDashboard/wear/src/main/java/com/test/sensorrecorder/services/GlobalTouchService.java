package com.test.sensorrecorder.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.test.sensorrecorder.DataMapKeys;
import com.test.sensorrecorder.DeviceClient;
import com.test.sensorrecorder.utils.GlobalConstants;


/**
 * Created by nadirhussain on 12/04/2018.
 * This service detects user touches on screen.
 * If user touches screen 15 times in 10 seconds then it send unique event trigger to Phone
 */

public class GlobalTouchService extends Service implements OnTouchListener {
    private static final String SERVICE_BROAD_ACTION = "com.test.GTouchServiceReceiver.Action";

    private String TAG = this.getClass().getSimpleName();
    private long firstTouchTime = 0; // This stores time , in milliseconds,  of first touch on screen.
    private int touchCount = 0; // It maintains count of user touches


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Global Touch service created ");

        /* Creating invisible view which will lie at top of all applications.
           All touches outside its area (which is full screen) will be detected.
        * */

        WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams(0, 0, 0, 0, 2003, 0x40028, -3);
        View mView = new View(this);

        mView.setOnTouchListener(this);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView, params);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchCount++;
        //This is the first click of user and start of a minute
        if (touchCount == 1) {
            firstTouchTime = System.currentTimeMillis();
        } else {
            long diff = System.currentTimeMillis() - firstTouchTime;
            if (isLessThanTimeThreshold(diff)) {

                //First the event if touch count has reached desired count within a minute
                if (touchCount >= GlobalConstants.TOUCH_EVENT_THRESHOLD) {
                    Log.d(TAG, "Location touch unique event ");
                    touchCount = 0;
                    DeviceClient.getInstance(this).sendTouchEventData(DataMapKeys.UNIQUE_TOUCH_EVENT_ID, null, 0, System.currentTimeMillis());
                }
            } else {
                //As time has exceeded 1 minute so we need to reset
                touchCount = 0;
            }

        }


        return false;
    }

    private boolean isLessThanTimeThreshold(long diff) {
        return diff < GlobalConstants.TOUCH_TIME_THRESHOLD;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadCast();
    }

    // This broadcast is triggered when service dies.
    private void sendBroadCast() {
        Intent broadcastIntent = new Intent(SERVICE_BROAD_ACTION);
        sendBroadcast(broadcastIntent);
    }
}


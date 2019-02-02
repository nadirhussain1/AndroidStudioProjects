package com.test.sensorrecorder.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognitionClient;

/**
 * Created by nadirhussain on 20/04/2018.
 * This service launches request for detecting "DetectActivityService"
 * which in turn detects user activity.
 */

public class LaunchDetectorService extends Service {
    private static final String SERVICE_BROAD_ACTION = "com.test.LaunchDetectorServiceReceiver.Action";
    private static final long ACTIVITY_UPDATE_INTERVAL = 5 * 60 * 1000; // 5 minutes;

    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    private ActivityRecognitionClient mActivityRecognitionClient;

    IBinder mBinder = new LaunchDetectorService.LocalBinder();

    public class LocalBinder extends Binder {
        public LaunchDetectorService getServerInstance() {
            return LaunchDetectorService.this;
        }
    }

    public LaunchDetectorService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityRecognitionClient = new ActivityRecognitionClient(this);
        mIntentService = new Intent(this, DetectActivityService.class);
        mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        requestActivityUpdates();
        return START_STICKY;
    }

    // This is the method which initiates request of starting DetectActivityService at particular periods.
    public void requestActivityUpdates() {
        mActivityRecognitionClient.requestActivityUpdates(ACTIVITY_UPDATE_INTERVAL, mPendingIntent);
    }

    public void removeActivityUpdatesButtonHandler() {
        mActivityRecognitionClient.removeActivityUpdates(mPendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeActivityUpdatesButtonHandler();
        sendBroadCast();
    }

    // This broadcast is triggered when service dies.
    private void sendBroadCast() {
        Intent broadcastIntent = new Intent(SERVICE_BROAD_ACTION);
        sendBroadcast(broadcastIntent);
    }
}

package com.test.sensorrecorder.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.test.sensorrecorder.DataMapKeys;
import com.test.sensorrecorder.DeviceClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by nadirhussain on 12/04/2018.
 * This service fetches location of gps from watch if available, if gps on watch is not available
 * then Android implementation gets location from connected phone.
 */

public class WatchLocMonitoringService extends Service {
    private static final String TAG = "LocMonitoringService";
    private static final String SERVICE_BROAD_ACTION = "com.test.watchLocationService.Action";

    private static final long UPDATE_INTERVAL_MS = TimeUnit.MINUTES.toMillis(5);
    private static final long FASTEST_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand");
        requestLocation();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        sendBroadCast();
    }


    //This method requests location after each "UPDATE_INTERVAL_MS" which is 5 seconds.
    private void requestLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_INTERVAL_MS);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Location Requested ");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

    }

    private void sendBroadCast() {
        Intent broadcastIntent = new Intent(SERVICE_BROAD_ACTION);
        sendBroadcast(broadcastIntent);
    }

    /*This method is invoked when Android System returns location object.

      Then this data is sent to phone.
     */

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG, "Inside  onLocationResult ");
            Location location = locationResult.getLastLocation();
            float[] values = new float[2];
            values[0] = (float) location.getLatitude();
            values[1] = (float) location.getLongitude();

            DeviceClient.getInstance(WatchLocMonitoringService.this).sendSensorData(DataMapKeys.GPS_WATCH_ID, values, (int) location.getAccuracy(), System.currentTimeMillis());
        }

    };


}

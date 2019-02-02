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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.test.sensorrecorder.DataMapKeys;
import com.test.sensorrecorder.data.DataManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by nadirhussain on 30/04/2018.
 * This service keeps running in the background to fetch location from gps of phone
 */

public class PhoneLocMonitoringService extends Service {
    private static final String SERVICE_BROAD_ACTION = "com.test.phoneLocServiceReceiver.Action";

    private static final long UPDATE_INTERVAL_MS = TimeUnit.MINUTES.toMillis(2);
    private static final long FASTEST_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // This method is invoked to invoke the main task that needs to be performed by service.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        requestLocation();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadCast();
    }


    // This method initiates request to Android System.
    private void requestLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_INTERVAL_MS);

        //Check if User has provided permission of location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

    }

    // This method sends broadcast message when service is going to die. This broadcast is received by PhoneLocServiceRestartReceiver
    // Which initiates service again.
    private void sendBroadCast() {
        Intent broadcastIntent = new Intent(SERVICE_BROAD_ACTION);
        sendBroadcast(broadcastIntent);
    }

    // This is the method which is invoked when Android System returns with values of location
    // Then SensorLogger is called to print data.
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            float[] values = new float[2];
            values[0] = (float) location.getLatitude();
            values[1] = (float) location.getLongitude();

            DataManager.getInstance().addUpdateSensorData(DataMapKeys.GPS_PHONE_ID, values, (int) location.getAccuracy());
        }

    };


}


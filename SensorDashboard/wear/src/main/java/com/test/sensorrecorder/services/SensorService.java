package com.test.sensorrecorder.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.sensorrecorder.DataMapKeys;
import com.test.sensorrecorder.DeviceClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
* This service gets list of all available sensors.
* Register them to report their values.
* Then sends those values to Phone Receiver service
* */

public class SensorService extends Service implements SensorEventListener {
    private static final String SENSOR_SERVICE_BROAD_ACTION = "com.test.SensorServiceReceiver.Action";
    private static final String TAG = "SensorService";


    private static final int OTHER_SENSORS_MEASUREMENT_INTERVAL = 20 * 1000;// this time is in milliseconds.. 20*1000(milliseconds) =20 seconds
    private static final int OTHER_SENSORS_BREAK_INTERVAL = 3 * 60 * 1000;// This time is in milliseconds. It equals to 3 minutes

    private static final int HEART_RATE_MEASUREMENT_INTERVAL = 30 * 1000;// this time is in milliseconds.. 30*1000(milliseconds) =30 seconds
    private static final int HEART_RATE_BREAK_INTERVAL = 5 * 60 * 1000;// This time is in milliseconds. It equals to 5 minutes

    SensorManager mSensorManager;

    private DeviceClient client;
    private ScheduledExecutorService otherSensorsScheduler;
    private ScheduledExecutorService heartSensorScheduler;
    private List<Sensor> availableSensorsList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        client = DeviceClient.getInstance(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "Inside onStartCommand");
        startMeasurement();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // Destroy method is called when service ends. I have called broadcast in this method which will restart service.
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy message");
        super.onDestroy();
        stopMeasurement();
        sendBroadCast();
    }


    // This is core method of this service. It first get list of all available sensors and then register the,

    protected void startMeasurement() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        ArrayList<Integer> availableSensorsIdsList = new ArrayList<>();
        Sensor sensor;
        for (int count = 1; count < 36; count++) { // I am looping through 36 because sensors in which we are interested have ids in range of 1-35
            sensor = mSensorManager.getDefaultSensor(count);
            if (sensor != null && count != Sensor.TYPE_HEART_RATE) {
                availableSensorsList.add(sensor);
                availableSensorsIdsList.add(count);
            }
        }


        // This piece of code deals with all sensors except heart rate sensors.
        otherSensorsScheduler = Executors.newScheduledThreadPool(availableSensorsList.size());
        otherSensorsScheduler.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Started registration");

                        for (int count = 0; count < availableSensorsList.size(); count++) {
                            Sensor availSensorItem = availableSensorsList.get(count);
                            if (availSensorItem.getReportingMode() == Sensor.REPORTING_MODE_ONE_SHOT) {
                                mSensorManager.requestTriggerSensor(new TriggerEventListener() {
                                    @Override
                                    public void onTrigger(TriggerEvent event) {
                                        client.sendSensorData(event.sensor.getType(), event.values, 100, event.timestamp); // Here I have set accuracy as fixed because TriggerEvent has not accuracy
                                    }
                                }, availSensorItem);

                            } else {
                                mSensorManager.registerListener(SensorService.this, availSensorItem, SensorManager.SENSOR_DELAY_NORMAL);
                            }
                        }

                        Log.d(TAG, "Going to sleep");
                        //Sleep thread to continue the measurement.
                        try {
                            Thread.sleep(OTHER_SENSORS_MEASUREMENT_INTERVAL);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "Interrupted while waitting to unregister Heartrate Sensor");
                        }

                        // Now unregister all sensors to have a break;
                        for (int count = 0; count < availableSensorsList.size(); count++) {
                            Sensor availSensorItem = availableSensorsList.get(count);
                            mSensorManager.unregisterListener(SensorService.this, availSensorItem);
                        }

                        Log.d(TAG, "unregistered all sensors");
                    }
                }, 10*1000, OTHER_SENSORS_BREAK_INTERVAL, TimeUnit.MILLISECONDS); // 10 seconds is initial delay


        // This piece of code deals with heart rate sensors

        final Sensor heartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (heartRateSensor != null) {
            availableSensorsIdsList.add(Sensor.TYPE_HEART_RATE);
            heartSensorScheduler = Executors.newScheduledThreadPool(1);
            heartSensorScheduler.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mSensorManager.registerListener(SensorService.this, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
                            //Sleep thread to continue the measurement.
                            try {
                                Thread.sleep(HEART_RATE_MEASUREMENT_INTERVAL);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "Interrupted while waitting to unregister Heartrate Sensor");
                            }

                            // Now unregister heart rate sensor
                            mSensorManager.unregisterListener(SensorService.this, heartRateSensor);


                        }
                    }, 10 * 1000, HEART_RATE_BREAK_INTERVAL, TimeUnit.MILLISECONDS); // 10 is initial delay

        }


        //Sending list of type of all available sensors to phone side.
        float[] values = new float[availableSensorsIdsList.size()];
        for (int count = 0; count < availableSensorsIdsList.size(); count++) {
            values[count] = availableSensorsIdsList.get(count);
        }
        DeviceClient.getInstance(getApplicationContext()).sendSensorData(DataMapKeys.LOG_AVAILABLE_SENSORS_EVENT, values, 100, System.currentTimeMillis());

    }

    // This method stops measurement when service is destroyed. It releases resources.
    private void stopMeasurement() {
        mSensorManager.unregisterListener(this);
        if (otherSensorsScheduler != null && !otherSensorsScheduler.isTerminated()) {
            otherSensorsScheduler.shutdown();
        }
        if (heartSensorScheduler != null && !heartSensorScheduler.isTerminated()) {
            heartSensorScheduler.shutdown();
        }
    }

    private void sendBroadCast() {
        Intent broadcastIntent = new Intent(SENSOR_SERVICE_BROAD_ACTION);
        sendBroadcast(broadcastIntent);
    }

    /*This is the method which is invoked by Android System when there is some latest values of sensor reported.
    * Then that value is sent to phone side.
    * */

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged " + event.sensor.getName());
        client.sendSensorData(event.sensor.getType(), event.values, event.accuracy, event.timestamp);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}

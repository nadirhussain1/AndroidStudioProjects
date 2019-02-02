package com.test.sensorrecorder.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.sensorrecorder.data.DataManager;
import com.test.sensorrecorder.model.SensorItem;
import com.test.sensorrecorder.model.SensorValue;
import com.test.sensorrecorder.storage.SensorPreferences;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nadirhussain on 10/05/2018.
 */

public class TestService extends Service {
    private static final String SERVICE_BROAD_ACTION = "com.test.TestService.Action";
    private static final String TestService = "TestService";

    private ScheduledExecutorService mScheduler;
    private static final long PERIOD_INTERVAL = TimeUnit.MINUTES.toSeconds(4); // This represents minutes


    @Override
    public void onCreate() {
        super.onCreate();

        String availableSensorsString=SensorPreferences.getAvailableSensorsList(getApplicationContext());
        if(availableSensorsString!=null){
            DataManager.getInstance().resetAvailableSensors(availableSensorsString);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        initiatePeriodicLoggingToConsole();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mScheduler != null && !mScheduler.isTerminated()) {
            mScheduler.shutdown();
        }
        sendBroadCast();
    }

    private void sendBroadCast() {
        Intent broadcastIntent = new Intent(SERVICE_BROAD_ACTION);
        sendBroadcast(broadcastIntent);
    }

    private void initiatePeriodicLoggingToConsole() {
        mScheduler = Executors.newScheduledThreadPool(1);
        mScheduler.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        // First get all available sensors and then print their latest value
                        List<SensorItem> availableSensors = DataManager.getInstance().getAvailableSensors();
                        if(availableSensors!=null && availableSensors.size()>0) {
                            for (SensorItem sensor : availableSensors) {
                                SensorValue sensorValue = DataManager.getInstance().getSensorData(sensor.getSensorId());
                                if (sensorValue != null) {
                                    Log.d(TestService, "" + sensorValue.getSensorName() + "  Values=" + Arrays.toString(sensorValue.getValues()));
                                }
                            }
                        }else{
                            Log.d(TestService, "List of available sensors is missing on phone side." );
                        }


                        // Getting value of a specific sensors
                        SensorValue specificSensorValue = DataManager.getInstance().getSensorData(Sensor.TYPE_ACCELEROMETER);
                        if(specificSensorValue!=null && specificSensorValue.getValues() !=null) {
                            Log.d(TestService, "Values of specific sensor Accelerometer are" );
                            float[] valuesArray = specificSensorValue.getValues();
                            for (int count = 0; count < valuesArray.length; count++) {
                                Log.d(TestService, "valuesArray[" + count + "]" + " =" + valuesArray[count]);
                            }
                        }else{
                            Log.d(TestService, "Specific sensor value was null" );
                        }


                        //Get value of GPS phone and print it.
                        SensorValue sensorValue = DataManager.getInstance().getGPSFromPhone();
                        if (sensorValue != null) {
                            Log.d(TestService, "" + sensorValue.getSensorName() + "  Values=" + Arrays.toString(sensorValue.getValues()));
                        }



                        //Get user activity
                        Log.d(TestService, "UserActivity=" + DataManager.getInstance().getUserActivityState());

                        //Get time of touch event
                        Log.d(TestService, "UniqueTouchEvent=" + DataManager.getInstance().getTimeOfUniqueTouchEvent());

                        Log.d(TestService, "***********************************************************************");

                    }
                }, 10, PERIOD_INTERVAL, TimeUnit.SECONDS);
    }

}

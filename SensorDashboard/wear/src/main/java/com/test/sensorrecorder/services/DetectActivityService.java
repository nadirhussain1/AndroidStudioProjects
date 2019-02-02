package com.test.sensorrecorder.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.test.sensorrecorder.DataMapKeys;
import com.test.sensorrecorder.DeviceClient;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 20/04/2018.
 * This service detects user activity state to know when user is still or running.
 */

public class DetectActivityService extends IntentService {
    public static final String TAG = "DetectActivityService";

    public DetectActivityService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
        DetectedActivity probableActivity = findMostProbableActivity(detectedActivities);

        if (probableActivity != null) {
            float[] values = new float[2];
            values[0] = probableActivity.getType();

            // Send detected activity to Phone
            DeviceClient.getInstance(getApplicationContext()).sendSensorData(DataMapKeys.USER_ACTIVITY_EVENT,
                    values,
                    probableActivity.getConfidence(),
                    System.currentTimeMillis());
        }
    }

    /*
      This method finds that activity which has highest confidence value.
      That will be high probable activity.
     */
    private DetectedActivity findMostProbableActivity(ArrayList<DetectedActivity> detectedActivities) {
        if (detectedActivities == null) {
            return null;
        }
        if (detectedActivities.size() == 1) {
            return detectedActivities.get(0);
        }

        DetectedActivity mostProbableActivity = detectedActivities.get(0);
        for (DetectedActivity activity : detectedActivities) {
            if (activity.getConfidence() > mostProbableActivity.getConfidence()) {
                mostProbableActivity = activity;
            }
        }

        return mostProbableActivity;
    }
}

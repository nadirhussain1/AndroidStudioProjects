package com.test.sensorrecorder;

import android.content.Context;
import android.util.SparseLongArray;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
  This class contains technical code of composing requests.
  It is all according to guidelines of Android development forum.
  This encapsulates data being sent to phone into a request and then
  that request is sent in background thread.
 */

public class DeviceClient {
    private static final String TAG = "DeviceClient";
    private static final int CLIENT_CONNECTION_TIMEOUT = 15000;

    public static DeviceClient instance;

    public static DeviceClient getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceClient(context.getApplicationContext());
        }

        return instance;
    }

    private Context context;
    private GoogleApiClient googleApiClient;
    private ExecutorService executorService;
    //private int filterId;

    private SparseLongArray lastSensorData;

    private DeviceClient(Context context) {
        this.context = context;

        googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();

        executorService = Executors.newCachedThreadPool(); // This creates pool of threads which will be used to send data.
        lastSensorData = new SparseLongArray();
    }

    /*
      This method checks if data of same sensor has been sent within last 3 seconds.
      If yes then discard it. If time delay has been more than 3 second then send data to phone.
      This approach helps to avoid load on watch by creating many threads.
     */
    public void sendSensorData(final int sensorType, final float[] values, final int accuracy, final long timeStamp) {
        long t = System.currentTimeMillis();

        long lastTimestamp = lastSensorData.get(sensorType);
        long timeAgo = t - lastTimestamp;

        if (lastTimestamp != 0 && timeAgo < 3000) {
            return;
        }

      //  Log.d(TAG, "Sensor=" + sensorType);
        lastSensorData.put(sensorType, t);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendSensorDataInBackground(sensorType, values, accuracy, timeStamp);
            }
        });
    }

    public void sendTouchEventData(final int sensorType, final float[] values, final int accuracy, final long timeStamp) {
        lastSensorData.put(sensorType, System.currentTimeMillis());
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendSensorDataInBackground(sensorType, values, accuracy, timeStamp);
            }
        });
    }

    private void sendSensorDataInBackground(int sensorType, float[] values, int accuracy, long timeStamp) {
        //   Log.d(TAG, "Sensor " + sensorType + " = " + Arrays.toString(values));


        PutDataMapRequest dataMap = PutDataMapRequest.create("/sensors/" + sensorType);

        dataMap.getDataMap().putInt(DataMapKeys.ACCURACY, accuracy);
        dataMap.getDataMap().putFloatArray(DataMapKeys.VALUES, values);
        dataMap.getDataMap().putLong(DataMapKeys.TIMESTAMP, timeStamp);

        PutDataRequest putDataRequest = dataMap.asPutDataRequest();
        send(putDataRequest);
    }

    private boolean validateConnection() {
        if (googleApiClient.isConnected()) {
            return true;
        }

        ConnectionResult result = googleApiClient.blockingConnect(CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        return result.isSuccess();
    }

    private void send(PutDataRequest putDataRequest) {
        if (validateConnection()) {
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    //  Log.v(TAG, "Sending sensor data: " + dataItemResult.getStatus().isSuccess());
                }
            });
        }
    }
}

package com.test.sensorrecorder.services;

import android.net.Uri;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;
import com.test.sensorrecorder.DataMapKeys;
import com.test.sensorrecorder.data.DataManager;
import com.test.sensorrecorder.model.UserState;

/*
    This service is interaction point with Watch code. Whenever watch sends any data
    it is received by this service. It then sends data to DataManager class.
 */

public class SensorReceiverService extends WearableListenerService {



    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*
       This is the method which is invoked when watch application sends data to phone.
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {

            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();

                if (path.startsWith("/sensors/")) {
                    unpackSensorData(Integer.parseInt(uri.getLastPathSegment()), DataMapItem.fromDataItem(dataItem).getDataMap());
                }
            }
        }
    }

    /*
       This method fetches values values, accuracy fields sent by watch.
       Then it takes appropriate methods based on sensor type.
     */
    private void unpackSensorData(int sensorType, DataMap dataMap) {
        float[] values = dataMap.getFloatArray(DataMapKeys.VALUES);
        int accuracy = dataMap.getInt(DataMapKeys.ACCURACY);

        switch (sensorType) {
            case DataMapKeys.LOG_AVAILABLE_SENSORS_EVENT:
                DataManager.getInstance().initAvailableSensors(getApplicationContext(),values);
                break;
            case DataMapKeys.USER_ACTIVITY_EVENT:
                int index = (int) values[0];
                DataManager.getInstance().setUserActivityState(UserState.getUserState(index));
                break;
            case DataMapKeys.UNIQUE_TOUCH_EVENT_ID:
                DataManager.getInstance().setLastTimeOfUniqueTouchEvent(System.currentTimeMillis());
                break;
            default:
                DataManager.getInstance().addUpdateSensorData(sensorType, values, accuracy);
                break;

        }

    }
}

package com.test.sensorrecorder.data;

import android.content.Context;
import android.text.format.DateFormat;

import com.test.sensorrecorder.DataMapKeys;
import com.test.sensorrecorder.model.SensorItem;
import com.test.sensorrecorder.model.SensorValue;
import com.test.sensorrecorder.model.UserState;
import com.test.sensorrecorder.storage.SensorPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nadirhussain on 10/04/2018.
 * <p>
 * This class stores sensors' values, GPS data, user activity.
 * Orlando you will have to access getter methods
 */

public class DataManager {
    private static final String TAG = "DataManager";
    private static DataManager dataManager = null;

    // This hashmap maintains values of sensors against their type(id).
    private HashMap<Long, SensorValue> sensorValues;
    // This is list of all available sensors. SensorItem has two values in it (name,id)
    private List<SensorItem> availableSensors;

    // this will be true when watch is connected with phone and will be false when they are disconnected.
    private boolean isWearDeviceConnected = false;

    // this variable stores user state. It can be (IN_VEHICLE,ON_BICYCLE,ON_FOOT,STILL,TILTING,WALKING,RUNNING)
    private UserState userActivityState = UserState.NOT_FOUND_YET;
    private long lastTimeOfUniqueTouchEvent = 0;


    private DataManager() {
        sensorValues = new HashMap<>();
    }

    // Orlando will access this class through this method. This is singleton class.
    public static DataManager getInstance() {
        synchronized (DataManager.class) {
            if (dataManager == null) {
                dataManager = new DataManager();
            }
        }
        return dataManager;
    }

    //Invoke this method to get list of available sensors. Each item of list will contain two fields(id,name)
    public List<SensorItem> getAvailableSensors() {
        return availableSensors;
    }

    public void initAvailableSensors(Context context, float[] availableSensorTypes) {
        if (availableSensorTypes != null && availableSensorTypes.length > 0) {
            SensorPreferences.saveAvailableSensorsIds(context, Arrays.toString(availableSensorTypes));
            availableSensors = new ArrayList<>();
            long sensorId;
            String sensorName;
            for (int count = 0; count < availableSensorTypes.length; count++) {
                sensorId = (long) availableSensorTypes[count];
                sensorName = getSensorName(sensorId);

                SensorItem sensorItem = new SensorItem(sensorId, sensorName);
                availableSensors.add(sensorItem);
            }
        }
    }

    public void resetAvailableSensors(String availableSensorTypes) {
        availableSensors = new ArrayList<>();
        long sensorId;
        String sensorName;

        availableSensorTypes=availableSensorTypes.substring(1,availableSensorTypes.length()-1);
        String[] splittedArray=availableSensorTypes.split(",");

        for (int count = 0; count < splittedArray.length; count++) {
            sensorId = Float.valueOf(splittedArray[count]).longValue();
            sensorName = getSensorName(sensorId);

            SensorItem sensorItem = new SensorItem(sensorId, sensorName);
            availableSensors.add(sensorItem);
        }

    }

    //This method is invoked when new values of sensor is received from watch.
    public void addUpdateSensorData(int sensorType, float[] values, int accuracy) {
        String sensorName = getSensorName(sensorType);
        SensorValue sensorValue = new SensorValue(sensorType, sensorName, accuracy, values);
        sensorValues.put(sensorValue.getSensorId(), sensorValue);
    }

    //Call this method to get recent value of any sensor. Input param need to be Type(Id).
    public SensorValue getSensorData(long sensorType) {
        return sensorValues.get(sensorType);
    }

    // This method returns SensorValue which has four fields(float[] values,int accuracy,sensorId,sensor Name)
    public SensorValue getGPSFromWatch() {
        return sensorValues.get(Long.valueOf(DataMapKeys.GPS_WATCH_ID));
    }

    public SensorValue getGPSFromPhone() {
        return sensorValues.get(Long.valueOf(DataMapKeys.GPS_PHONE_ID));
    }

    public void updatedConnectionStatus(boolean isWearConnected) {
        this.isWearDeviceConnected = isWearConnected;
    }

    // Call this method to check if wear device is connected with phone or not.
    public boolean isWearDeviceConnected() {
        return isWearDeviceConnected;
    }

    // Call this method to get user state.
    public UserState getUserActivityState() {
        return userActivityState;
    }

    public void setUserActivityState(UserState userActivityState) {
        this.userActivityState = userActivityState;
    }

    public void setLastTimeOfUniqueTouchEvent(long timeInMillis) {
        lastTimeOfUniqueTouchEvent = timeInMillis;
    }

    public String getTimeOfUniqueTouchEvent() {
        if (lastTimeOfUniqueTouchEvent == 0) {
            return "No event reported yet";
        }
        return DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(lastTimeOfUniqueTouchEvent)).toString();
    }

    private String getSensorName(long id) {
        SensorNames sensorNames = new SensorNames();
        return sensorNames.getName((int) id);
    }


}

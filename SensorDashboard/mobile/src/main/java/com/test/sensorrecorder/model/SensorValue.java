package com.test.sensorrecorder.model;

/**
 * Created by nadirhussain on 30/04/2018.
 */

public class SensorValue extends SensorItem {
    private int accuracy;
    private float[] values; // This parameter contains value of sensor returned by watch

    public SensorValue(long id, String name, int accuracy, float[] values) {
        super(id, name);
        this.accuracy = accuracy;
        this.values = values;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}

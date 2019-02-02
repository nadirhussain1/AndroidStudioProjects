package com.test.sensorrecorder.model;

/**
 * Created by nadirhussain on 10/04/2018.
 */

public class SensorItem {
    private long sensorId;
    private String sensorName;


    public SensorItem() {

    }

    public SensorItem(long id, String name) {
        this.sensorId = id;
        this.sensorName = name;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }


}

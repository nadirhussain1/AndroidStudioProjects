package com.test.sensorrecorder;

/*
  This class contains constant keys that are used by both (wear and mobile module)
  to put and extract data.
 */
public class DataMapKeys {
    public static final String ACCURACY = "accuracy";
    public static final String VALUES = "values";
    public static final String TIMESTAMP = "timestamp";


    public static final int GPS_WATCH_ID = 100;
    public static final int GPS_PHONE_ID = 101;
    public static final int UNIQUE_TOUCH_EVENT_ID = 102;
    public static final int USER_ACTIVITY_EVENT = 103;
    public static final int LOG_AVAILABLE_SENSORS_EVENT = 104;

    public static final String UNIQUE_TOUCH_EVENT_KEY = "UniqueTouchEvent";
    public static final String GPS_WATCH_KEY = "GPS_WATCH";
    public static final String GPS_PHONE_KEY = "GPS_PHONE";
    public static final String USER_ACTIVITY_KEY = "UserActivityState";

}

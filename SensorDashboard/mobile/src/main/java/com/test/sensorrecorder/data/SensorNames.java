package com.test.sensorrecorder.data;

import android.util.SparseArray;

import com.test.sensorrecorder.DataMapKeys;


public class SensorNames {
    public SparseArray<String> names;

    // This class defines keys of all possible sensors and events
    public SensorNames() {
        names = new SparseArray<String>();

        names.append(1, "Accelerometer");
        names.append(2, "Magnetometer");
        names.append(3, "OrientationSensor");
        names.append(4, "Gyroscope");
        names.append(5, "LightSensor");
        names.append(6, "PressureSensor");
        names.append(7, "TemperatureSensor");
        names.append(8, "ProximitySensor");
        names.append(9, "GravitySensor");
        names.append(10, "LinearAccelerationSensor");
        names.append(11, "RotationVectorSensor");
        names.append(12, "RelativeHumiditySensor");
        names.append(13, "AmbientTemperatureSensor");
        names.append(14, "MagneticFieldUnCalibratedSensor");
        names.append(15, "GameRotationVectorSensor");
        names.append(16, "GyroscopeUncalibratedSensor");
        names.append(17, "SignificantMotionSensor");
        names.append(18, "StepDetectorSensor");
        names.append(19, "StepCounterSensor");
        names.append(20, "GeoMagneticRotationVectorSensor");
        names.append(21, "HeartRateSensor");
        names.append(22, "TiltDetectorSensor");
        names.append(23, "WakeGestureSensor");
        names.append(24, "GlanceGestureSensor");
        names.append(25, "PickUpGestureSensor");
        names.append(26, "WristTiltGesture");
        names.append(28, "Pose6DOFSensor");
        names.append(29, "StationaryDetectSensor");
        names.append(30, "MotionDetectSensor");
        names.append(31, "HeartBeatSensor");
        names.append(34, "LowLatencyOffBodySensor");
        names.append(35, "AccelerometerUncalibratedSensor");

        names.append(65537, "AFE4405LightSensor");
        names.append(65538, "MotionHealthSensor");
        names.append(65539, "CapacitiveProximitySensor");
        names.append(65540, "ActivityReminderSensor");
        names.append(65541, "MotionGoalSensor");
        names.append(65542, "WearStateSensor");
        names.append(65543, "GPSFLPSensor");
        names.append(65544, "WorkoutSensor");
        names.append(65545, "WorkoutDetailSensor");
        names.append(65546, "WorkoutReminderSensor");
        names.append(65547, "WorkoutResultSensor");
        names.append(65548, "ThermalProtectionSensor");
        names.append(65549, "StressMonitorSensor");
        names.append(65550, "BreathMonitorSensor");
        names.append(65551, "DetailSleepSensor");
        names.append(65552, "PostureSensor");

        names.append(33171001, "Reserve Sensor A");
        names.append(33171025, "CustomizedPedometer");
        names.append(33171098, "Info");

        names.append(DataMapKeys.GPS_WATCH_ID, DataMapKeys.GPS_WATCH_KEY);
        names.append(DataMapKeys.GPS_PHONE_ID, DataMapKeys.GPS_PHONE_KEY);
        names.append(DataMapKeys.UNIQUE_TOUCH_EVENT_ID, DataMapKeys.UNIQUE_TOUCH_EVENT_KEY);

    }

    // Call this method to get name of any sensor. Input parameter will be its unique Id(type)
    public String getName(int sensorId) {
        String name = names.get(sensorId);
        if (name == null) {
            name = "Unknown";
        }

        return name;
    }
}

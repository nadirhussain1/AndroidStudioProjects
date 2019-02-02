package com.test.sensorrecorder.model;

/**
 * Created by nadirhussain on 30/04/2018.
 * This class defines all possible states of user activity.
 */

public enum UserState {
    NOT_FOUND_YET(-1),
    IN_VEHICLE(0),
    ON_BICYCLE(1),
    ON_FOOT(2),
    STILL(3),
    TILTING(5),
    WALKING(7),
    RUNNING(8);

    private final int state;

    UserState(int state) {
        this.state = state;
    }

    public static UserState getUserState(int id) {
        switch (id) {
            case 0:
                return UserState.IN_VEHICLE;
            case 1:
                return UserState.ON_BICYCLE;
            case 2:
                return UserState.ON_FOOT;
            case 3:
                return UserState.STILL;
            case 5:
                return UserState.TILTING;
            case 7:
                return UserState.WALKING;
            case 8:
                return UserState.RUNNING;
            default:
                return UserState.NOT_FOUND_YET;

        }
    }
}

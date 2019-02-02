package com.brainpixel.valetapp.model;

/**
 * Created by nadirhussain on 25/10/2016.
 */

public class CustomLocation {
    public static final String DEVICE_SOURCE = "Device";
    public static final String NO_LOC_SOURCE = "NA";
    private double latitude, longitude = 0;
    private String locationSource;
    private String timeSource;
    private long time;

    public CustomLocation(long time, String timeSource) {
        this.time = time;
        this.timeSource = timeSource;
        this.locationSource = NO_LOC_SOURCE;
    }

    public CustomLocation(long time, String timeSource, double latitude, double longitude, String locationSource) {
        this(time, timeSource);
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationSource = locationSource;
    }

    public void updateLocation(double latitude, double longitude, String locationSource) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationSource = locationSource;
    }

    public void updateTime(long time, String timeSource) {
        this.time = time;
        this.timeSource = timeSource;
    }
    public static CustomLocation copyInstance(CustomLocation newLocation){
       return new CustomLocation(newLocation.getTime(),newLocation.getTimeSource(),newLocation.getLatitude(),newLocation.getLongitude(),newLocation.locationSource);
    }
    public boolean isLocationAvailable(){
        return !locationSource.contentEquals(NO_LOC_SOURCE);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationSource() {
        return locationSource;
    }

    public void setLocationSource(String locationSource) {
        this.locationSource = locationSource;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeSource() {
        return timeSource;
    }

    public void setTimeSource(String timeSource) {
        this.timeSource = timeSource;
    }
}

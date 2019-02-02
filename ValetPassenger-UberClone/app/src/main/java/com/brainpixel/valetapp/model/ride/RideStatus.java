package com.brainpixel.valetapp.model.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 18/04/2017.
 */

public class RideStatus {
    @SerializedName("r_id")
    @Expose
    private Integer rideId;

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
    }

    @SerializedName("r_status")
    @Expose
    private String rideStatus;
}

package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 19/04/2017.
 */

public class GetRideStatusResponse extends GeneralServerResponse {
    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(RideStatus rideStatus) {
        this.rideStatus = rideStatus;
    }

    @SerializedName("data")
    @Expose
    private RideStatus rideStatus = null;
}

package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 18/04/2017.
 */


public class GetStartedRideResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private RideDriverInfoObject data = null;

    public RideDriverInfoObject getData() {
        return data;
    }

    public void setData(RideDriverInfoObject data) {
        this.data = data;
    }

}

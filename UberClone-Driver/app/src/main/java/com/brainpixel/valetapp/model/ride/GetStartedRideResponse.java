package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 19/04/2017.
 */

public class GetStartedRideResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private PassengerRideData data = null;

    public PassengerRideData getData() {
        return data;
    }

    public void setData(PassengerRideData data) {
        this.data = data;
    }

}

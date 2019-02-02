package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 18/04/2017.
 */

public class GetDriverLocationResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private DriverLocation driverLocation = null;

    public DriverLocation getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(DriverLocation driverLocation) {
        this.driverLocation = driverLocation;
    }
}

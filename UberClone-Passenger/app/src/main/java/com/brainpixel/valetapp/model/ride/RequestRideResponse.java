
package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestRideResponse extends GeneralServerResponse{
    @SerializedName("data")
    @Expose
    private List<RideDriverInfoObject> data = null;

    public List<RideDriverInfoObject> getData() {
        return data;
    }

    public void setData(List<RideDriverInfoObject> data) {
        this.data = data;
    }

}

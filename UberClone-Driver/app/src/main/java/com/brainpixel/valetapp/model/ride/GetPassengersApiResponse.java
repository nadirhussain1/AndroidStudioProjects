
package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPassengersApiResponse extends GeneralServerResponse {

    @SerializedName("data")
    @Expose
    private List<PassengerRideData> data = null;


    public List<PassengerRideData> getData() {
        return data;
    }

    public void setData(List<PassengerRideData> data) {
        this.data = data;
    }

}


package com.brainpixel.valetapp.model.trips;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripsListResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private List<TripData> data = null;

    public List<TripData> getData() {
        return data;
    }

    public void setData(List<TripData> data) {
        this.data = data;
    }

}

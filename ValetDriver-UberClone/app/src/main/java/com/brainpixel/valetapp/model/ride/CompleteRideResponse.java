
package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteRideResponse extends GeneralServerResponse{

    @SerializedName("data")
    @Expose
    private CompletedRideData data;

    public CompletedRideData getData() {
        return data;
    }

    public void setData(CompletedRideData data) {
        this.data = data;
    }

}

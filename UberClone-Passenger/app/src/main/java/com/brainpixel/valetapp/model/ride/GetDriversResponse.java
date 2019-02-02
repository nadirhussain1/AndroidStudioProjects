
package com.brainpixel.valetapp.model.ride;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDriversResponse extends GeneralServerResponse{

    @SerializedName("data")
    @Expose
    private List<DriverInfoObject> data = null;

    public List<DriverInfoObject> getData() {
        return data;
    }

    public void setData(List<DriverInfoObject> data) {
        this.data = data;
    }

}

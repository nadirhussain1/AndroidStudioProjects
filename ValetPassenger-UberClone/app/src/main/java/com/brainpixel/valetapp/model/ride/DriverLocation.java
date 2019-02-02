package com.brainpixel.valetapp.model.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 18/04/2017.
 */

public class DriverLocation {
    @SerializedName("u_id")
    @Expose
    private Integer uId;
    @SerializedName("u_latest_lat")
    @Expose
    private Double uLatestLat;
    @SerializedName("u_latest_lng")
    @Expose
    private Double uLatestLng;


    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Double getuLatestLat() {
        return uLatestLat;
    }

    public void setuLatestLat(Double uLatestLat) {
        this.uLatestLat = uLatestLat;
    }

    public Double getuLatestLng() {
        return uLatestLng;
    }

    public void setuLatestLng(Double uLatestLng) {
        this.uLatestLng = uLatestLng;
    }

}

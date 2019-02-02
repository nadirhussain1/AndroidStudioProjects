
package com.brainpixel.valetapp.model.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompletedRideData {

    @SerializedName("r_id")
    @Expose
    private Integer rId;
    @SerializedName("r_base_price")
    @Expose
    private Double rBasePrice;
    @SerializedName("r_per_km_charge")
    @Expose
    private Double rPerKmCharge;
    @SerializedName("r_per_min_charge")
    @Expose
    private Double rPerMinCharge;
    @SerializedName("r_total_kms")
    @Expose
    private Double rTotalKms;
    @SerializedName("r_total_mins")
    @Expose
    private Double rTotalMins;
    @SerializedName("r_total_charged")
    @Expose
    private Double rTotalCharged;

    public Integer getRId() {
        return rId;
    }

    public void setRId(Integer rId) {
        this.rId = rId;
    }

    public Double getRBasePrice() {
        return rBasePrice;
    }

    public void setRBasePrice(Double rBasePrice) {
        this.rBasePrice = rBasePrice;
    }

    public Double getRPerKmCharge() {
        return rPerKmCharge;
    }

    public void setRPerKmCharge(Double rPerKmCharge) {
        this.rPerKmCharge = rPerKmCharge;
    }

    public Double getRPerMinCharge() {
        return rPerMinCharge;
    }

    public void setRPerMinCharge(Double rPerMinCharge) {
        this.rPerMinCharge = rPerMinCharge;
    }

    public Double getRTotalKms() {
        return rTotalKms;
    }

    public void setRTotalKms(Double rTotalKms) {
        this.rTotalKms = rTotalKms;
    }

    public Double getRTotalMins() {
        return rTotalMins;
    }

    public void setRTotalMins(Double rTotalMins) {
        this.rTotalMins = rTotalMins;
    }

    public Double getRTotalCharged() {
        return rTotalCharged;
    }

    public void setRTotalCharged(Double rTotalCharged) {
        this.rTotalCharged = rTotalCharged;
    }

}

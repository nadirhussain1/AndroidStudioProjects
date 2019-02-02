
package com.brainpixel.valetapp.model.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassengerRideData {

    @SerializedName("r_id")
    @Expose
    private Integer rId;
    @SerializedName("r_rider_id")
    @Expose
    private Integer rRiderId;
    @SerializedName("r_driver_id")
    @Expose
    private Integer rDriverId;
    @SerializedName("r_from_lat")
    @Expose
    private Double rFromLat;
    @SerializedName("r_from_lng")
    @Expose
    private Double rFromLng;
    @SerializedName("r_from_location_name")
    @Expose
    private String rFromLocationName;
    @SerializedName("r_to_lat")
    @Expose
    private Double rToLat;
    @SerializedName("r_to_lng")
    @Expose
    private Double rToLng;
    @SerializedName("r_to_location_name")
    @Expose
    private String rToLocationName;
    @SerializedName("r_actual_from_lat")
    @Expose
    private Double rActualFromLat;
    @SerializedName("r_actual_from_lng")
    @Expose
    private Double rActualFromLng;
    @SerializedName("r_actual_from_location_name")
    @Expose
    private Object rActualFromLocationName;
    @SerializedName("r_actual_to_lat")
    @Expose
    private Object rActualToLat;
    @SerializedName("r_actual_to_lng")
    @Expose
    private Object rActualToLng;
    @SerializedName("r_actual_to_location_name")
    @Expose
    private Object rActualToLocationName;
    @SerializedName("r_base_price")
    @Expose
    private Object rBasePrice;
    @SerializedName("r_per_km_charge")
    @Expose
    private Object rPerKmCharge;
    @SerializedName("r_per_min_charge")
    @Expose
    private Object rPerMinCharge;
    @SerializedName("r_total_kms")
    @Expose
    private Object rTotalKms;
    @SerializedName("r_total_mins")
    @Expose
    private Object rTotalMins;
    @SerializedName("r_total_charged")
    @Expose
    private Object rTotalCharged;
    @SerializedName("r_status")
    @Expose
    private String rStatus;
    @SerializedName("r_cancelled_by_id")
    @Expose
    private Object rCancelledById;
    @SerializedName("r_cancelled_datetime")
    @Expose
    private Object rCancelledDatetime;
    @SerializedName("r_accept_datetime")
    @Expose
    private Object rAcceptDatetime;
    @SerializedName("r_start_datetime")
    @Expose
    private Object rStartDatetime;
    @SerializedName("r_complete_datetime")
    @Expose
    private Object rCompleteDatetime;
    @SerializedName("r_expire_datetime")
    @Expose
    private Object rExpireDatetime;
    @SerializedName("r_create_datetime")
    @Expose
    private String rCreateDatetime;
    @SerializedName("distance_in_km")
    @Expose
    private Double distanceInKm;

    public Integer getRId() {
        return rId;
    }

    public void setRId(Integer rId) {
        this.rId = rId;
    }

    public Integer getRRiderId() {
        return rRiderId;
    }

    public void setRRiderId(Integer rRiderId) {
        this.rRiderId = rRiderId;
    }

    public Integer getRDriverId() {
        return rDriverId;
    }

    public void setRDriverId(Integer rDriverId) {
        this.rDriverId = rDriverId;
    }

    public Double getRFromLat() {
        return rFromLat;
    }

    public void setRFromLat(Double rFromLat) {
        this.rFromLat = rFromLat;
    }

    public Double getRFromLng() {
        return rFromLng;
    }

    public void setRFromLng(Double rFromLng) {
        this.rFromLng = rFromLng;
    }

    public String getRFromLocationName() {
        return rFromLocationName;
    }

    public void setRFromLocationName(String rFromLocationName) {
        this.rFromLocationName = rFromLocationName;
    }

    public Double getRToLat() {
        return rToLat;
    }

    public void setRToLat(Double rToLat) {
        this.rToLat = rToLat;
    }

    public Double getRToLng() {
        return rToLng;
    }

    public void setRToLng(Double rToLng) {
        this.rToLng = rToLng;
    }

    public String getRToLocationName() {
        return rToLocationName;
    }

    public void setRToLocationName(String rToLocationName) {
        this.rToLocationName = rToLocationName;
    }

    public Double getRActualFromLat() {
        return rActualFromLat;
    }

    public void setRActualFromLat(Double rActualFromLat) {
        this.rActualFromLat = rActualFromLat;
    }

    public Double getRActualFromLng() {
        return rActualFromLng;
    }

    public void setRActualFromLng(Double rActualFromLng) {
        this.rActualFromLng = rActualFromLng;
    }

    public Object getRActualFromLocationName() {
        return rActualFromLocationName;
    }

    public void setRActualFromLocationName(Object rActualFromLocationName) {
        this.rActualFromLocationName = rActualFromLocationName;
    }

    public Object getRActualToLat() {
        return rActualToLat;
    }

    public void setRActualToLat(Object rActualToLat) {
        this.rActualToLat = rActualToLat;
    }

    public Object getRActualToLng() {
        return rActualToLng;
    }

    public void setRActualToLng(Object rActualToLng) {
        this.rActualToLng = rActualToLng;
    }

    public Object getRActualToLocationName() {
        return rActualToLocationName;
    }

    public void setRActualToLocationName(Object rActualToLocationName) {
        this.rActualToLocationName = rActualToLocationName;
    }

    public Object getRBasePrice() {
        return rBasePrice;
    }

    public void setRBasePrice(Object rBasePrice) {
        this.rBasePrice = rBasePrice;
    }

    public Object getRPerKmCharge() {
        return rPerKmCharge;
    }

    public void setRPerKmCharge(Object rPerKmCharge) {
        this.rPerKmCharge = rPerKmCharge;
    }

    public Object getRPerMinCharge() {
        return rPerMinCharge;
    }

    public void setRPerMinCharge(Object rPerMinCharge) {
        this.rPerMinCharge = rPerMinCharge;
    }

    public Object getRTotalKms() {
        return rTotalKms;
    }

    public void setRTotalKms(Object rTotalKms) {
        this.rTotalKms = rTotalKms;
    }

    public Object getRTotalMins() {
        return rTotalMins;
    }

    public void setRTotalMins(Object rTotalMins) {
        this.rTotalMins = rTotalMins;
    }

    public Object getRTotalCharged() {
        return rTotalCharged;
    }

    public void setRTotalCharged(Object rTotalCharged) {
        this.rTotalCharged = rTotalCharged;
    }

    public String getRStatus() {
        return rStatus;
    }

    public void setRStatus(String rStatus) {
        this.rStatus = rStatus;
    }

    public Object getRCancelledById() {
        return rCancelledById;
    }

    public void setRCancelledById(Object rCancelledById) {
        this.rCancelledById = rCancelledById;
    }

    public Object getRCancelledDatetime() {
        return rCancelledDatetime;
    }

    public void setRCancelledDatetime(Object rCancelledDatetime) {
        this.rCancelledDatetime = rCancelledDatetime;
    }

    public Object getRAcceptDatetime() {
        return rAcceptDatetime;
    }

    public void setRAcceptDatetime(Object rAcceptDatetime) {
        this.rAcceptDatetime = rAcceptDatetime;
    }

    public Object getRStartDatetime() {
        return rStartDatetime;
    }

    public void setRStartDatetime(Object rStartDatetime) {
        this.rStartDatetime = rStartDatetime;
    }

    public Object getRCompleteDatetime() {
        return rCompleteDatetime;
    }

    public void setRCompleteDatetime(Object rCompleteDatetime) {
        this.rCompleteDatetime = rCompleteDatetime;
    }

    public Object getRExpireDatetime() {
        return rExpireDatetime;
    }

    public void setRExpireDatetime(Object rExpireDatetime) {
        this.rExpireDatetime = rExpireDatetime;
    }

    public String getRCreateDatetime() {
        return rCreateDatetime;
    }

    public void setRCreateDatetime(String rCreateDatetime) {
        this.rCreateDatetime = rCreateDatetime;
    }

    public Double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(Double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

}

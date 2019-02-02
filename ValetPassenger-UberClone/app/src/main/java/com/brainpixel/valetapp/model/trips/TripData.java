
package com.brainpixel.valetapp.model.trips;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripData {

    @SerializedName("r_id")
    @Expose
    private Integer rId;
    @SerializedName("r_rider_id")
    @Expose
    private Integer rRiderId;
    @SerializedName("r_driver_id")
    @Expose
    private Object rDriverId;
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
    private String rActualFromLocationName;
    @SerializedName("r_actual_to_lat")
    @Expose
    private Double rActualToLat;
    @SerializedName("r_actual_to_lng")
    @Expose
    private Double rActualToLng;
    @SerializedName("r_actual_to_location_name")
    @Expose
    private String rActualToLocationName;
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
    @SerializedName("r_rating")
    @Expose
    private Double rRating;
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
    private String rStartDatetime;
    @SerializedName("r_complete_datetime")
    @Expose
    private String rCompleteDatetime;
    @SerializedName("r_expire_datetime")
    @Expose
    private String rExpireDatetime;
    @SerializedName("r_create_datetime")
    @Expose
    private String rCreateDatetime;
    @SerializedName("r_schedule_time")
    @Expose
    private String rScheduleTime;
    @SerializedName("r_map_screenshot_img")
    @Expose
    private String rMapScreenshotImg;

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

    public Object getRDriverId() {
        return rDriverId;
    }

    public void setRDriverId(Object rDriverId) {
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

    public String getRActualFromLocationName() {
        return rActualFromLocationName;
    }

    public void setRActualFromLocationName(String rActualFromLocationName) {
        this.rActualFromLocationName = rActualFromLocationName;
    }

    public Double getRActualToLat() {
        return rActualToLat;
    }

    public void setRActualToLat(Double rActualToLat) {
        this.rActualToLat = rActualToLat;
    }

    public Double getRActualToLng() {
        return rActualToLng;
    }

    public void setRActualToLng(Double rActualToLng) {
        this.rActualToLng = rActualToLng;
    }

    public String getRActualToLocationName() {
        return rActualToLocationName;
    }

    public void setRActualToLocationName(String rActualToLocationName) {
        this.rActualToLocationName = rActualToLocationName;
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

    public Object getRRating() {
        return rRating;
    }

    public void setRRating(Double rRating) {
        this.rRating = rRating;
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

    public String getRStartDatetime() {
        return rStartDatetime;
    }

    public void setRStartDatetime(String rStartDatetime) {
        this.rStartDatetime = rStartDatetime;
    }

    public Object getRCompleteDatetime() {
        return rCompleteDatetime;
    }

    public void setRCompleteDatetime(String rCompleteDatetime) {
        this.rCompleteDatetime = rCompleteDatetime;
    }

    public Object getRExpireDatetime() {
        return rExpireDatetime;
    }

    public void setRExpireDatetime(String rExpireDatetime) {
        this.rExpireDatetime = rExpireDatetime;
    }

    public String getRCreateDatetime() {
        return rCreateDatetime;
    }

    public void setRCreateDatetime(String rCreateDatetime) {
        this.rCreateDatetime = rCreateDatetime;
    }

    public String getRScheduleTime() {
        return rScheduleTime;
    }

    public void setRScheduleTime(String rScheduleTime) {
        this.rScheduleTime = rScheduleTime;
    }

    public String getRMapScreenshotImg() {
        return rMapScreenshotImg;
    }

    public void setRMapScreenshotImg(String rMapScreenshotImg) {
        this.rMapScreenshotImg = rMapScreenshotImg;
    }

}


package com.brainpixel.valetapp.model.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideDriverInfoObject {

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
    private String rAcceptDatetime;
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
    @SerializedName("u_id")
    @Expose
    private Integer uId;
    @SerializedName("u_first_name")
    @Expose
    private String uFirstName;
    @SerializedName("u_last_name")
    @Expose
    private String uLastName;
    @SerializedName("u_profile_image")
    @Expose
    private String uProfileImage;
    @SerializedName("u_email")
    @Expose
    private String uEmail;
    @SerializedName("u_password")
    @Expose
    private String uPassword;
    @SerializedName("u_mobile_no")
    @Expose
    private String uMobileNo;
    @SerializedName("u_home")
    @Expose
    private Object uHome;
    @SerializedName("u_license_pic")
    @Expose
    private String uLicensePic;
    @SerializedName("u_nric_pic")
    @Expose
    private String uNricPic;
    @SerializedName("u_latest_lat")
    @Expose
    private Double uLatestLat;
    @SerializedName("u_latest_lng")
    @Expose
    private Double uLatestLng;
    @SerializedName("u_location_updated_at")
    @Expose
    private String uLocationUpdatedAt;
    @SerializedName("u_promotion_code_share")
    @Expose
    private String uPromotionCodeShare;
    @SerializedName("u_role_idFk")
    @Expose
    private Integer uRoleIdFk;
    @SerializedName("u_activation_code")
    @Expose
    private String uActivationCode;
    @SerializedName("u_email_verified")
    @Expose
    private Integer uEmailVerified;
    @SerializedName("u_contact_no_verified")
    @Expose
    private Integer uContactNoVerified;
    @SerializedName("u_status")
    @Expose
    private Integer uStatus;
    @SerializedName("u_create_datetime")
    @Expose
    private String uCreateDatetime;
    @SerializedName("u_update_datetime")
    @Expose
    private String uUpdateDatetime;
    @SerializedName("u_updated_by_idFk")
    @Expose
    private Object uUpdatedByIdFk;

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

    public  String getRActualFromLocationName() {
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

    public String getRAcceptDatetime() {
        return rAcceptDatetime;
    }

    public void setRAcceptDatetime(String rAcceptDatetime) {
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

    public Integer getUId() {
        return uId;
    }

    public void setUId(Integer uId) {
        this.uId = uId;
    }

    public String getUFirstName() {
        return uFirstName;
    }

    public void setUFirstName(String uFirstName) {
        this.uFirstName = uFirstName;
    }

    public String getULastName() {
        return uLastName;
    }

    public void setULastName(String uLastName) {
        this.uLastName = uLastName;
    }

    public String getUProfileImage() {
        return uProfileImage;
    }

    public void setUProfileImage(String uProfileImage) {
        this.uProfileImage = uProfileImage;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getUPassword() {
        return uPassword;
    }

    public void setUPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getUMobileNo() {
        return uMobileNo;
    }

    public void setUMobileNo(String uMobileNo) {
        this.uMobileNo = uMobileNo;
    }

    public Object getUHome() {
        return uHome;
    }

    public void setUHome(Object uHome) {
        this.uHome = uHome;
    }

    public String getULicensePic() {
        return uLicensePic;
    }

    public void setULicensePic(String uLicensePic) {
        this.uLicensePic = uLicensePic;
    }

    public String getUNricPic() {
        return uNricPic;
    }

    public void setUNricPic(String uNricPic) {
        this.uNricPic = uNricPic;
    }

    public Double getULatestLat() {
        return uLatestLat;
    }

    public void setULatestLat(Double uLatestLat) {
        this.uLatestLat = uLatestLat;
    }

    public Double getULatestLng() {
        return uLatestLng;
    }

    public void setULatestLng(Double uLatestLng) {
        this.uLatestLng = uLatestLng;
    }

    public String getULocationUpdatedAt() {
        return uLocationUpdatedAt;
    }

    public void setULocationUpdatedAt(String uLocationUpdatedAt) {
        this.uLocationUpdatedAt = uLocationUpdatedAt;
    }

    public String getUPromotionCodeShare() {
        return uPromotionCodeShare;
    }

    public void setUPromotionCodeShare(String uPromotionCodeShare) {
        this.uPromotionCodeShare = uPromotionCodeShare;
    }

    public Integer getURoleIdFk() {
        return uRoleIdFk;
    }

    public void setURoleIdFk(Integer uRoleIdFk) {
        this.uRoleIdFk = uRoleIdFk;
    }

    public String getUActivationCode() {
        return uActivationCode;
    }

    public void setUActivationCode(String uActivationCode) {
        this.uActivationCode = uActivationCode;
    }

    public Integer getUEmailVerified() {
        return uEmailVerified;
    }

    public void setUEmailVerified(Integer uEmailVerified) {
        this.uEmailVerified = uEmailVerified;
    }

    public Integer getUContactNoVerified() {
        return uContactNoVerified;
    }

    public void setUContactNoVerified(Integer uContactNoVerified) {
        this.uContactNoVerified = uContactNoVerified;
    }

    public Integer getUStatus() {
        return uStatus;
    }

    public void setUStatus(Integer uStatus) {
        this.uStatus = uStatus;
    }

    public String getUCreateDatetime() {
        return uCreateDatetime;
    }

    public void setUCreateDatetime(String uCreateDatetime) {
        this.uCreateDatetime = uCreateDatetime;
    }

    public String getUUpdateDatetime() {
        return uUpdateDatetime;
    }

    public void setUUpdateDatetime(String uUpdateDatetime) {
        this.uUpdateDatetime = uUpdateDatetime;
    }

    public Object getUUpdatedByIdFk() {
        return uUpdatedByIdFk;
    }

    public void setUUpdatedByIdFk(Object uUpdatedByIdFk) {
        this.uUpdatedByIdFk = uUpdatedByIdFk;
    }

}

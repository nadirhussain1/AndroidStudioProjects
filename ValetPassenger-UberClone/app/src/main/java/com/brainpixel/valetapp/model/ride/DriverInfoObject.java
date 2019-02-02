
package com.brainpixel.valetapp.model.ride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverInfoObject {

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
    private Object uProfileImage;
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
    private Object uLocationUpdatedAt;
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

    public Object getUProfileImage() {
        return uProfileImage;
    }

    public void setUProfileImage(Object uProfileImage) {
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

    public Object getULocationUpdatedAt() {
        return uLocationUpdatedAt;
    }

    public void setULocationUpdatedAt(Object uLocationUpdatedAt) {
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

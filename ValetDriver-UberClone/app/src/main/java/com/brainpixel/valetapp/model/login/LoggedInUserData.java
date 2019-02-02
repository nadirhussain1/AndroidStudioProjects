
package com.brainpixel.valetapp.model.login;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Table(name = "logged_user")
public class LoggedInUserData extends Model {
    @SerializedName("u_id")
    @Expose
    @Column(name = "u_id")
    private String userId;

    @SerializedName("u_first_name")
    @Expose
    @Column(name = "u_first_name")
    private String uFirstName;

    @SerializedName("u_last_name")
    @Expose
    @Column(name = "u_last_name")
    private String uLastName;

    @SerializedName("u_profile_image")
    @Expose
    @Column(name = "u_profile_image")
    private String profileImageUrl;

    @SerializedName("u_email")
    @Expose
    @Column(name = "u_email")
    private String uEmailAddress;

    @SerializedName("u_password_orig")
    @Expose
    @Column(name = "u_password_orig")
    private String uPassword;

    @SerializedName("u_mobile_no")
    @Expose
    @Column(name = "u_mobile_no")
    private String uMobileNo;

    @SerializedName("u_license_pic")
    @Expose
    @Column(name = "u_license_pic")
    private String uLicensePicUrl;

    @SerializedName("u_nric_pic")
    @Expose
    @Column(name = "u_nric_pic")
    private String uCnicPicUrl;

    @SerializedName("u_promotion_code_share")
    @Expose
    @Column(name = "u_promotion_code_share")
    private String uPromotionCode;

    @SerializedName("u_email_verified")
    @Expose
    @Column(name = "u_email_verified")
    private Integer uEmailVerified;

    @SerializedName("u_contact_no_verified")
    @Expose
    @Column(name = "u_contact_no_verified")
    private Integer uContactVerified;

    @SerializedName("license_accept_status")
    @Expose
    @Column(name = "license_accept_status")
    private String licenseAcceptStatus;

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuLicensePicUrl() {
        return uLicensePicUrl;
    }

    public void setuLicensePicUrl(String uLicensePicUrl) {
        this.uLicensePicUrl = uLicensePicUrl;
    }

    public String getuCnicPicUrl() {
        return uCnicPicUrl;
    }

    public void setuCnicPicUrl(String uCnicPicUrl) {
        this.uCnicPicUrl = uCnicPicUrl;
    }

    public String getuPromotionCode() {
        return uPromotionCode;
    }

    public void setuPromotionCode(String uPromotionCode) {
        this.uPromotionCode = uPromotionCode;
    }

    public Integer getuEmailVerified() {
        return uEmailVerified;
    }

    public void setuEmailVerified(Integer uEmailVerified) {
        this.uEmailVerified = uEmailVerified;
    }

    public Integer getuContactVerified() {
        return uContactVerified;
    }

    public void setuContactVerified(Integer uContactVerified) {
        this.uContactVerified = uContactVerified;
    }

    public String getLicenseAcceptStatus() {
        return licenseAcceptStatus;
    }

    public void setLicenseAcceptStatus(String licenseAcceptStatus) {
        this.licenseAcceptStatus = licenseAcceptStatus;
    }

    public String getCnicAcceptStatus() {
        return cnicAcceptStatus;
    }

    public void setCnicAcceptStatus(String cnicAcceptStatus) {
        this.cnicAcceptStatus = cnicAcceptStatus;
    }

    @SerializedName("nric_accept_status")
    @Expose
    @Column(name = "nric_accept_status")
    private String cnicAcceptStatus;

    @SerializedName("u_status")
    @Expose
    @Column(name = "u_status")
    private String uStatus;


    /**
     * @return The uEmailAddress
     */
    public String getUEmailAddress() {
        return uEmailAddress;
    }

    /**
     * @param uEmailAddress The u_email_address
     */
    public void setUEmailAddress(String uEmailAddress) {
        this.uEmailAddress = uEmailAddress;
    }

    /**
     * @return The uFirstName
     */
    public String getUFirstName() {
        return uFirstName;
    }

    /**
     * @param uFirstName The u_first_name
     */
    public void setUFirstName(String uFirstName) {
        this.uFirstName = uFirstName;
    }

    /**
     * @return The uLastName
     */
    public String getULastName() {
        return uLastName;
    }

    /**
     * @param uLastName The u_last_name
     */
    public void setULastName(String uLastName) {
        this.uLastName = uLastName;
    }


    /**
     * @return The uMobileNo
     */
    public String getUMobileNo() {
        return uMobileNo;
    }

    /**
     * @param uMobileNo The u_mobile_no
     */
    public void setUMobileNo(String uMobileNo) {
        this.uMobileNo = uMobileNo;
    }


    /**
     * @return The uStatus
     */
    public String getUStatus() {
        return uStatus;
    }

    /**
     * @param uStatus The u_status
     */
    public void setUStatus(String uStatus) {
        this.uStatus = uStatus;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}

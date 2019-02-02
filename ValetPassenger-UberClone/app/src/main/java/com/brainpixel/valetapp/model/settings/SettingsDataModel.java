
package com.brainpixel.valetapp.model.settings;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Table(name = "general_settings")
public class SettingsDataModel extends Model{

    @SerializedName("g_id")
    @Expose
    @Column(name = "g_id")
    private Integer gId;

    @SerializedName("g_zoom_level")
    @Expose
    @Column(name = "g_zoom_level")
    private Integer gZoomLevel;

    @SerializedName("g_per_min_charges")
    @Expose
    @Column(name = "g_per_min_charges")
    private Double gPerMinCharges;

    @SerializedName("g_base_price")
    @Expose
    @Column(name = "g_base_price")
    private Double gBasePrice;

    @SerializedName("g_per_km_charge")
    @Expose
    @Column(name = "g_per_km_charge")
    private Double gPerKmCharge;

    @SerializedName("g_free_rides_per_promotional_code")
    @Expose
    @Column(name = "g_free_rides_per_promotional_code")
    private Integer gFreeRidesPerPromotionalCode;

    @SerializedName("g_price_off_per_promotional_code")
    @Expose
    @Column(name = "g_price_off_per_promotional_code")
    private Integer gPriceOffPerPromotionalCode;

    @SerializedName("g_company_share")
    @Expose
    @Column(name = "g_company_share")
    private Integer gCompanyShare;

    public Integer getGId() {
        return gId;
    }

    public void setGId(Integer gId) {
        this.gId = gId;
    }

    public Integer getGZoomLevel() {
        return gZoomLevel;
    }

    public void setGZoomLevel(Integer gZoomLevel) {
        this.gZoomLevel = gZoomLevel;
    }

    public Double getGPerMinCharges() {
        return gPerMinCharges;
    }

    public void setGPerMinCharges(Double gPerMinCharges) {
        this.gPerMinCharges = gPerMinCharges;
    }

    public Double getGBasePrice() {
        return gBasePrice;
    }

    public void setGBasePrice(Double gBasePrice) {
        this.gBasePrice = gBasePrice;
    }

    public Double getGPerKmCharge() {
        return gPerKmCharge;
    }

    public void setGPerKmCharge(Double gPerKmCharge) {
        this.gPerKmCharge = gPerKmCharge;
    }

    public Integer getGFreeRidesPerPromotionalCode() {
        return gFreeRidesPerPromotionalCode;
    }

    public void setGFreeRidesPerPromotionalCode(Integer gFreeRidesPerPromotionalCode) {
        this.gFreeRidesPerPromotionalCode = gFreeRidesPerPromotionalCode;
    }

    public Integer getGPriceOffPerPromotionalCode() {
        return gPriceOffPerPromotionalCode;
    }

    public void setGPriceOffPerPromotionalCode(Integer gPriceOffPerPromotionalCode) {
        this.gPriceOffPerPromotionalCode = gPriceOffPerPromotionalCode;
    }

    public Integer getGCompanyShare() {
        return gCompanyShare;
    }

    public void setGCompanyShare(Integer gCompanyShare) {
        this.gCompanyShare = gCompanyShare;
    }

}

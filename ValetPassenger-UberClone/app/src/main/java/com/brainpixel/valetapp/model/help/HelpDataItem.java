
package com.brainpixel.valetapp.model.help;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpDataItem {

    @SerializedName("h_id")
    @Expose
    private Integer hId;
    @SerializedName("h_parent_id")
    @Expose
    private Integer hParentId;
    @SerializedName("h_title")
    @Expose
    private String hTitle;
    @SerializedName("h_description")
    @Expose
    private String hDescription;
    @SerializedName("h_status")
    @Expose
    private Integer hStatus;
    @SerializedName("h_created_byIdFk")
    @Expose
    private Integer hCreatedByIdFk;
    @SerializedName("h_create_datetime")
    @Expose
    private Object hCreateDatetime;
    @SerializedName("h_updated_byIdFk")
    @Expose
    private Integer hUpdatedByIdFk;
    @SerializedName("h_updated_datetime")
    @Expose
    private String hUpdatedDatetime;

    public Integer getHId() {
        return hId;
    }

    public void setHId(Integer hId) {
        this.hId = hId;
    }

    public Integer getHParentId() {
        return hParentId;
    }

    public void setHParentId(Integer hParentId) {
        this.hParentId = hParentId;
    }

    public String getHTitle() {
        return hTitle;
    }

    public void setHTitle(String hTitle) {
        this.hTitle = hTitle;
    }

    public String getHDescription() {
        return hDescription;
    }

    public void setHDescription(String hDescription) {
        this.hDescription = hDescription;
    }

    public Integer getHStatus() {
        return hStatus;
    }

    public void setHStatus(Integer hStatus) {
        this.hStatus = hStatus;
    }

    public Integer getHCreatedByIdFk() {
        return hCreatedByIdFk;
    }

    public void setHCreatedByIdFk(Integer hCreatedByIdFk) {
        this.hCreatedByIdFk = hCreatedByIdFk;
    }

    public Object getHCreateDatetime() {
        return hCreateDatetime;
    }

    public void setHCreateDatetime(Object hCreateDatetime) {
        this.hCreateDatetime = hCreateDatetime;
    }

    public Integer getHUpdatedByIdFk() {
        return hUpdatedByIdFk;
    }

    public void setHUpdatedByIdFk(Integer hUpdatedByIdFk) {
        this.hUpdatedByIdFk = hUpdatedByIdFk;
    }

    public String getHUpdatedDatetime() {
        return hUpdatedDatetime;
    }

    public void setHUpdatedDatetime(String hUpdatedDatetime) {
        this.hUpdatedDatetime = hUpdatedDatetime;
    }

}

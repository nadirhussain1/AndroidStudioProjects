
package com.gov.pitb.pcb.data.server.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("u_full_name")
    @Expose
    private String uFullName;
    @SerializedName("u_user_name")
    @Expose
    private String uUserName;
    @SerializedName("u_email")
    @Expose
    private String uEmail;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("u_type")
    @Expose
    private String uType;
    @SerializedName("u_token")
    @Expose
    private String uToken;

    public String getUFullName() {
        return uFullName;
    }

    public void setUFullName(String uFullName) {
        this.uFullName = uFullName;
    }

    public String getUUserName() {
        return uUserName;
    }

    public void setUUserName(String uUserName) {
        this.uUserName = uUserName;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUType() {
        return uType;
    }

    public void setUType(String uType) {
        this.uType = uType;
    }

    public String getUToken() {
        return uToken;
    }

    public void setUToken(String uToken) {
        this.uToken = uToken;
    }

}

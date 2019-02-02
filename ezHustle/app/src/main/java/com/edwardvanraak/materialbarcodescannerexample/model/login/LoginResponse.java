
package com.edwardvanraak.materialbarcodescannerexample.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("offline_db")
    @Expose
    private String offlineDb;
    @SerializedName("scouter")
    @Expose
    private String scouter;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("account_active")
    @Expose
    private String accountActive;
    @SerializedName("error")
    @Expose
    private String error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOfflineDb() {
        return offlineDb;
    }

    public void setOfflineDb(String offlineDb) {
        this.offlineDb = offlineDb;
    }

    public String getScouter() {
        return scouter;
    }

    public void setScouter(String scouter) {
        this.scouter = scouter;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAccountActive() {
        return accountActive;
    }

    public void setAccountActive(String accountActive) {
        this.accountActive = accountActive;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}

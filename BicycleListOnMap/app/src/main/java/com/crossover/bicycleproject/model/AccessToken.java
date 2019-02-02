
package com.crossover.bicycleproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AccessToken {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    /**
     * 
     * @return
     *     The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 
     * @param accessToken
     *     The accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}

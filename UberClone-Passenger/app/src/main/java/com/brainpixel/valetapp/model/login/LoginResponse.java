
package com.brainpixel.valetapp.model.login;

import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class LoginResponse extends GeneralServerResponse {

    @SerializedName("data")
    @Expose
    private LoggedInUserData data;



    public LoggedInUserData getData() {
        return data;
    }

    public void setData(LoggedInUserData data) {
        this.data = data;
    }

}

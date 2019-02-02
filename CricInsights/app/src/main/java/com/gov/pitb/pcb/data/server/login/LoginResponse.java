
package com.gov.pitb.pcb.data.server.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;

public class LoginResponse extends GeneralServerResponse{
    @SerializedName("data")
    @Expose
    private LoginData data;


    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

}

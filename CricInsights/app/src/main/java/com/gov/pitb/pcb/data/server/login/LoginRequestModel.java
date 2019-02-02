package com.gov.pitb.pcb.data.server.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 28/07/2017.
 */

public class LoginRequestModel {
    public LoginRequestModel(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("user_password")
    @Expose
    public String userPassword;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


}

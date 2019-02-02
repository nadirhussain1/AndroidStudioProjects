package com.brainpixel.valetapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 07/10/2016.
 */

public class GeneralServerResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("success")
    @Expose
    private Boolean success;


    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     *     The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     *
     * @param error
     *     The error
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     *
     * @return
     *     The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     *     The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

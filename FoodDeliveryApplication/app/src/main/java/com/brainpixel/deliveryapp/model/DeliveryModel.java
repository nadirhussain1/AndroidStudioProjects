package com.brainpixel.deliveryapp.model;

import com.brainpixel.deliveryapp.utils.GlobalConstants;

import java.io.Serializable;

/**
 * Created by nadirhussain on 24/11/2018.
 */

public class DeliveryModel implements Serializable {
    private String date;
    private String status;

    public DeliveryModel(){
        status= GlobalConstants.STATUS_PENDING;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}

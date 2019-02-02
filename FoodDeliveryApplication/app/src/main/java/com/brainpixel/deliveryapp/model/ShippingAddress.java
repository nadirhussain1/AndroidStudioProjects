package com.brainpixel.deliveryapp.model;

import java.io.Serializable;

/**
 * Created by nadirhussain on 20/11/2018.
 */

public class ShippingAddress implements Serializable {
    private double latitude;
    private double longitude;
    private String description;
    private String receiverName;

    public ShippingAddress(){
        latitude=-1;
        longitude=-1;
        description="";
        receiverName="";
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


}

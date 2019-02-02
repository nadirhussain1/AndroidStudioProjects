package com.brainpixel.deliveryapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nadirhussain on 20/11/2018.
 */

public class Order implements Serializable {
    private String userId;
    private String totalBill;
    private String creationDate;
    private String orderNumber;
    private String orderStatus;
    private ShippingAddress shippingAddress;
    private List<DeliveryModel> deliverySchedule;
    private List<ShippingItem> shippingItems;



    public Order(){
       totalBill="";
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<DeliveryModel> getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule(List<DeliveryModel> deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

    public List<ShippingItem> getShippingItem() {
        return shippingItems;
    }

    public void setShippingItem(List<ShippingItem> shippingItem) {
        this.shippingItems = shippingItem;
    }
    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

}

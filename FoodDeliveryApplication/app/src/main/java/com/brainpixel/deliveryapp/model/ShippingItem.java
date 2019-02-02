package com.brainpixel.deliveryapp.model;

import java.io.Serializable;

/**
 * Created by nadirhussain on 21/11/2018.
 */

public class ShippingItem implements Serializable {
    private String mainItemId;
    private String quantity;
    private String itemPrice;


    public ShippingItem() {
        mainItemId = "";
        quantity = "";
        itemPrice = "";
    }

    public String getMainItemId() {
        return mainItemId;
    }

    public void setMainItemId(String mainItemId) {
        this.mainItemId = mainItemId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }


}

package com.brainpixel.deliveryapp.handlers;

import com.brainpixel.deliveryapp.model.ShippingAddress;

/**
 * Created by nadirhussain on 20/11/2018.
 */

public class OnLatLngDecodeEvent {
    public ShippingAddress selectedAddressItem;

    public OnLatLngDecodeEvent() {
        selectedAddressItem = new ShippingAddress();
    }

}

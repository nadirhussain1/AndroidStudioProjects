package com.brainpixel.deliveryapp.managers;

import android.util.Log;

import com.brainpixel.deliveryapp.model.CartItem;
import com.brainpixel.deliveryapp.model.ConfigData;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.model.ShippingItem;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nadirhussain on 03/10/2018.
 */

public final class GlobalDataManager {
    private static GlobalDataManager instance = null;

    private GlobalDataManager() {
        homeItems = new ArrayList<>();
        cartItems = new ArrayList<>();
        configData = new ConfigData();
    }

    public static GlobalDataManager getInstance() {
        if (instance == null) {
            instance = new GlobalDataManager();
        }
        return instance;
    }

    public void createNewOrder() {
        currentOrder = new Order();
        currentOrder.setCreationDate(GlobalUtil.formatDate(Calendar.getInstance().getTime()));
        currentOrder.setOrderStatus(GlobalConstants.STATUS_ACTIVE);
        int totalBill = 0;
        List<ShippingItem> shippingItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            ShippingItem shippingItem = new ShippingItem();
            shippingItem.setMainItemId(cartItem.getMainItemDocumentId());
            shippingItem.setItemPrice(cartItem.getPrice());
            shippingItem.setQuantity(cartItem.getCartQuantity());

            shippingItems.add(shippingItem);

            totalBill = totalBill + (GlobalUtil.getIntValue(cartItem.getPrice()) * GlobalUtil.getIntValue(cartItem.getCartQuantity()));
        }
        currentOrder.setShippingItem(shippingItems);
        currentOrder.setTotalBill("" + totalBill);
        currentOrder.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public MainItem getMainItem(String itemId) {
        if (itemId != null) {
            for (MainItem item : homeItems) {
                if (item != null && item.getMainItemDocumentId() != null) {
                    if (item.getMainItemDocumentId().contentEquals(itemId)) {
                        return item;
                    }
                } else {
                    Log.d("CatchDebug", "Item=" + item.getMainItemDocumentId());
                }
            }
        } else {
            Log.d("CatchDebug", "ItemId is null");
        }
        return null;
    }


    public ArrayList<MainItem> homeItems;
    public ArrayList<CartItem> cartItems;
    public ConfigData configData;
    public MainItem selectedItem;
    public Order currentOrder;
}

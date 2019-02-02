package com.brainpixel.deliveryapp.model;

/**
 * Created by nadirhussain on 11/09/2018.
 */

public class CartItem extends MainItem {
    private String cartDocumentId;
    private String userId;
    private String cartQuantity;


    public String getCartDocumentId() {
        return cartDocumentId;
    }

    public void setCartDocumentId(String cartDocumentId) {
        this.cartDocumentId = cartDocumentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public void fillMainItemData(MainItem mainItem) {
        this.setItemId(mainItem.getItemId());
        this.setImage(mainItem.getImage());
        this.setPrice(mainItem.getPrice());
        this.setName(mainItem.getName());
    }


}

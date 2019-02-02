package com.brainpixel.deliveryapp.model;

import java.io.Serializable;

/**
 * Created by nadirhussain on 02/09/2018.
 */

public class MainItem implements Serializable{
    private String mainItemDocumentId;
    private String itemId;
    private String name;
    private String price;
    private String image;
    private String avgRating;
    private String totalRatings;
    private String shortDescription;
    private String longDescription;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(String totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getMainItemDocumentId() {
        return mainItemDocumentId;
    }

    public void setMainItemDocumentId(String mainItemDocumentId) {
        this.mainItemDocumentId = mainItemDocumentId;
    }




}

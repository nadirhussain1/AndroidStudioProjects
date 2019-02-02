package com.brainpixel.deliveryapp.model.searchloc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nadirhussain on 06/03/2017.
 */

public class PlacesSearchResponse {
    @SerializedName("predictions")
    @Expose
    private List<SearchedPlaceItem> searchedPlaceItemList;

    public List<SearchedPlaceItem> getSearchedPlaceItemList() {
        return searchedPlaceItemList;
    }

    public void setSearchedPlaceItemList(List<SearchedPlaceItem> searchedPlaceItemList) {
        this.searchedPlaceItemList = searchedPlaceItemList;
    }
}

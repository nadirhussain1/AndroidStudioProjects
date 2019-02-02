package com.brainpixel.valetapp.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 07/03/2017.
 */

public class SearchItemGeometry {
    @SerializedName("location")
    @Expose
    private SearchLocationItem searchLocationItem;

    public SearchLocationItem getSearchLocationItem() {
        return searchLocationItem;
    }

    public void setSearchLocationItem(SearchLocationItem searchLocationItem) {
        this.searchLocationItem = searchLocationItem;
    }


}

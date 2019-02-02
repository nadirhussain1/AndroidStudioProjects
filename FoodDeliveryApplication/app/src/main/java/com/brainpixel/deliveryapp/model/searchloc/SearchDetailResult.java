package com.brainpixel.deliveryapp.model.searchloc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 07/03/2017.
 */

public class SearchDetailResult {
    @SerializedName("geometry")
    @Expose
    private SearchItemGeometry geometry;

    public SearchItemGeometry getSearchItemGeomtry() {
        return geometry;
    }

    public void setSearchItemGeomtry(SearchItemGeometry searchItemGeomtry) {
        geometry = searchItemGeomtry;
    }
}

package com.brainpixel.deliveryapp.model.searchloc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 07/03/2017.
 */

public class SearchDetailResponse {
    @SerializedName("result")
    @Expose
    private SearchDetailResult searchDetailResult;

    public SearchDetailResult getSearchDetailResult() {
        return searchDetailResult;
    }

    public void setSearchDetailResult(SearchDetailResult searchDetailResult) {
        this.searchDetailResult = searchDetailResult;
    }


}

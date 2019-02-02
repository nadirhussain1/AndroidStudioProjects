package com.edwardvanraak.materialbarcodescannerexample.model.offline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 16/04/2017.
 */

public class CountResponse {
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @SerializedName("total")
    @Expose
    private Integer totalCount;
}

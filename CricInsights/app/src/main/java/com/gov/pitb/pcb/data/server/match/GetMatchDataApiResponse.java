package com.gov.pitb.pcb.data.server.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;

import java.util.List;

/**
 * Created by nadirhussain on 04/08/2017.
 */

public class GetMatchDataApiResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private List<Delivery> matchDeliveries;

    public List<Delivery> getMatchDeliveries() {
        return matchDeliveries;
    }

    public void setMatchDeliveries(List<Delivery> matchDeliveries) {
        this.matchDeliveries = matchDeliveries;
    }






}

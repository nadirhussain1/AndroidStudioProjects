package com.gov.pitb.pcb.data.server.match;

import com.gov.pitb.pcb.data.db.dynamic.Delivery;

import java.util.List;

/**
 * Created by nadirhussain on 04/08/2017.
 */

public class MatchDeliveriesData {
    private List<Delivery> matchDeliveries;

    public List<Delivery> getMatchDeliveries() {
        return matchDeliveries;
    }

    public void setMatchDeliveries(List<Delivery> matchDeliveries) {
        this.matchDeliveries = matchDeliveries;
    }

}

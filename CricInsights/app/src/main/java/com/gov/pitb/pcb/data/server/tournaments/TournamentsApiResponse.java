package com.gov.pitb.pcb.data.server.tournaments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class TournamentsApiResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private TournamentsData data;

    public TournamentsData getData() {
        return data;
    }

    public void setData(TournamentsData data) {
        this.data = data;
    }

}

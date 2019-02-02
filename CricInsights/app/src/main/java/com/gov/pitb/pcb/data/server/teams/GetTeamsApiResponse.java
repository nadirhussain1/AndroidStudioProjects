package com.gov.pitb.pcb.data.server.teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class GetTeamsApiResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private TeamsData data;

    public TeamsData getData() {
        return data;
    }

    public void setData(TeamsData data) {
        this.data = data;
    }
}

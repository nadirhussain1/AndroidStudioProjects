package com.gov.pitb.pcb.data.server.permissions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;

/**
 * Created by nadirhussain on 22/08/2017.
 */

public class MatchPermissionsResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private MatchPermData data;


    public MatchPermData getData() {
        return data;
    }

    public void setData(MatchPermData data) {
        this.data = data;
    }
}

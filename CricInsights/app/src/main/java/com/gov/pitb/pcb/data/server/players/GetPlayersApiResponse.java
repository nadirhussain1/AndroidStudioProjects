
package com.gov.pitb.pcb.data.server.players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.server.GeneralServerResponse;

public class GetPlayersApiResponse extends GeneralServerResponse {
    @SerializedName("data")
    @Expose
    private PlayersData data;


    public PlayersData getData() {
        return data;
    }

    public void setData(PlayersData data) {
        this.data = data;
    }

}

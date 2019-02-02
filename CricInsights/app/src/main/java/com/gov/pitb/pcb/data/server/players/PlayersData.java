
package com.gov.pitb.pcb.data.server.players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlayersData implements Serializable {

    @SerializedName("players")
    @Expose
    private List<Player> players = null;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}

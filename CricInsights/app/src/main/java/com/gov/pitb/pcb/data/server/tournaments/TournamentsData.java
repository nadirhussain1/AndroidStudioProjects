package com.gov.pitb.pcb.data.server.tournaments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.db.config.Tournament;

import java.io.Serializable;
import java.util.List;

public class TournamentsData implements Serializable {
    @SerializedName("tournaments")
    @Expose
    private List<Tournament> tournaments = null;

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

}

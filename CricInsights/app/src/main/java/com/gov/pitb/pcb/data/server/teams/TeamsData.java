package com.gov.pitb.pcb.data.server.teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.db.config.Team;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nadirhussain on 07/07/2017.
 */

public class TeamsData implements Serializable{
    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}


package com.gov.pitb.pcb.data.db.config;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nadirhussain on 06/07/2017.
 */

@Table(name = "table_matches")
public class Match extends Model implements Serializable {
    public Match(){
        super();
    }
    @Column(name = "matchId")
    @SerializedName("matchId")
    @Expose
    private String matchId;

    @Column(name = "teamOneId")
    @SerializedName("teamOneId")
    @Expose
    private String teamOneId;

    @Column(name = "teamTwoId")
    @SerializedName("teamTwoId")
    @Expose
    private String teamTwoId;

    @Column(name = "matchDate")
    @SerializedName("matchDate")
    @Expose
    private String matchDate;

    @Column(name = "matchOvers")
    @SerializedName("matchOvers")
    @Expose
    private String matchOvers;


    private Team teamOne, teamTwo;


    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Team getTeamOne() {
        return teamOne;
    }


    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeams(Team teamOne, Team teamTwo) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    public String getTeamOneId() {
        return teamOneId;
    }

    public void setTeamOneId(String teamOneId) {
        this.teamOneId = teamOneId;
    }

    public String getTeamTwoId() {
        return teamTwoId;
    }

    public void setTeamTwoId(String teamTwoId) {
        this.teamTwoId = teamTwoId;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchOvers() {
        return matchOvers;
    }

    public void setMatchOvers(String matchOvers) {
        this.matchOvers = matchOvers;
    }


    public Team getTeamById(String teamId) {
        if (teamId.equalsIgnoreCase(teamOneId)) {
            return teamOne;
        } else {
            return teamTwo;
        }
    }


}

package com.gov.pitb.pcb.data.db.config;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nadirhussain on 06/07/2017.
 */

@Table(name = "table_tournaments")
public class Tournament extends Model implements Serializable {
    public Tournament() {
        super();
    }

    @Column(name = "tournamentId")
    @SerializedName("tournamentId")
    @Expose
    private String tournamentId;

    @Column(name = "tournamentName")
    @SerializedName("tournamentName")
    @Expose
    private String tournamentName;

    @Column(name = "startDate")
    @SerializedName("startDate")
    @Expose
    private String startDate;

    @Column(name = "endDate")
    @SerializedName("endDate")
    @Expose
    private String endDate;

    @Column(name = "matches", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    @SerializedName("matches")
    @Expose
    private List<Match> matches;


    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }


}

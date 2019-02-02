package com.gov.pitb.pcb.data.db.dynamic;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Table;
import com.gov.pitb.pcb.data.db.config.Team;

/**
 * Created by nadirhussain on 06/07/2017.
 */

@Table(name = "table_inning")
public class Inning extends Model {
    public Inning(){
        super();
    }

    public Inning(String matchId, String inningId,int matchOvers) {
        super();
        this.matchId = matchId;
        this.inningId = inningId;
        this.matchOvers=matchOvers;
    }


    @Column(name = "matchId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String matchId;
    @Column(name = "inningId", uniqueGroups = {"uniqueKey"}, onUniqueConflicts = {ConflictAction.FAIL})
    private String inningId;
    @Column(name = "battingTeamId")
    private String battingTeamId;
    @Column(name = "bowlingTeamId")
    private String bowlingTeamId;
    @Column(name = "battingTeamSelPlayersIds")
    private String battingTeamSelPlayersIds;
    @Column(name = "bowlingTeamSelPlayersIds")
    private String bowlingTeamSelPlayersIds;
    @Column(name = "matchOvers")
    private int matchOvers;


    private Team battingTeam, bowlingTeam;

    public void setTeams(Team battingTeam, Team bowlingTeam) {
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;

        battingTeamId = battingTeam.getTeamId();
        bowlingTeamId = bowlingTeam.getTeamId();
        battingTeamSelPlayersIds = battingTeam.getSelectedPlayersIds();
        bowlingTeamSelPlayersIds = bowlingTeam.getSelectedPlayersIds();
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getInningId() {
        return inningId;
    }

    public void setInningId(String inningId) {
        this.inningId = inningId;
    }

    public String getBattingTeamId() {
        return battingTeamId;
    }

    public void setBattingTeamId(String battingTeamId) {
        this.battingTeamId = battingTeamId;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(Team battingTeam) {
        this.battingTeam = battingTeam;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }

    public void setBowlingTeam(Team bowlingTeam) {
        this.bowlingTeam = bowlingTeam;
    }

    public String getBattingTeamSelPlayersIds() {
        return battingTeamSelPlayersIds;
    }

    public void setBattingTeamSelPlayersIds(String battingTeamSelPlayersIds) {
        this.battingTeamSelPlayersIds = battingTeamSelPlayersIds;
    }

    public String getBowlingTeamSelPlayersIds() {
        return bowlingTeamSelPlayersIds;
    }

    public void setBowlingTeamSelPlayersIds(String bowlingTeamSelPlayersIds) {
        this.bowlingTeamSelPlayersIds = bowlingTeamSelPlayersIds;
    }

    public String getBowlingTeamId() {
        return bowlingTeamId;
    }

    public void setBowlingTeamId(String bowlingTeamId) {
        this.bowlingTeamId = bowlingTeamId;
    }

    public int getMatchOvers() {
        return matchOvers;
    }

    public void setMatchOvers(int matchOvers) {
        this.matchOvers = matchOvers;
    }


}

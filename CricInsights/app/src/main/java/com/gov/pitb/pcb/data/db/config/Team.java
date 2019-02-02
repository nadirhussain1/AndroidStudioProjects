package com.gov.pitb.pcb.data.db.config;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gov.pitb.pcb.data.server.players.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nadirhussain on 06/07/2017.
 */

@Table(name = "table_teams")
public class Team extends Model implements Serializable {
    public Team() {
        super();
        teamPlayers=new ArrayList<>();
    }

    @Column(name = "teamId")
    @SerializedName("teamId")
    @Expose
    private String teamId;

    @Column(name = "teamName")
    @SerializedName("teamName")
    @Expose
    private String teamName;

    @Column(name = "teamLogoUrl")
    @SerializedName("teamLogoUrl")
    @Expose
    private String teamLogoUrl;

    @Column(name = "playerIds")
    @SerializedName("playerIds")
    @Expose
    private String playerIds;

    private ArrayList<Player> teamPlayers;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLogoUrl() {
        return teamLogoUrl;
    }

    public void setTeamLogoUrl(String teamLogoUrl) {
        this.teamLogoUrl = teamLogoUrl;
    }


    public String getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(String playerIds) {
        this.playerIds = playerIds;
    }

    public ArrayList<Player> getPlayersOfTeam() {
        return teamPlayers;
    }

    public void setPlayersOfTeam(ArrayList<Player> selectedPlayers) {
        this.teamPlayers = selectedPlayers;
    }

    public ArrayList<Player> getSelectedPlayersOfTeam() {
        ArrayList<Player> selectedPlayers = new ArrayList<>();
        if (teamPlayers != null) {
            for (Player player : teamPlayers) {
                if (player.isSelected()) {
                    selectedPlayers.add(player);
                }
            }
        }
        return selectedPlayers;
    }
    public String getSelectedPlayersIds() {
       String selectedPlayersIds="";
        if (teamPlayers != null) {
            for (Player player : teamPlayers) {
                if (player.isSelected()) {
                    selectedPlayersIds=player.getPlayerId()+","+selectedPlayersIds;
                }
            }
        }
        return selectedPlayersIds;
    }

}

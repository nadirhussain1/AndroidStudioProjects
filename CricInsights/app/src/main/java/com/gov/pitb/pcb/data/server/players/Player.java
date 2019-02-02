
package com.gov.pitb.pcb.data.server.players;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Table(name = "table_player")
public class Player extends Model implements Serializable {
    public Player(){
        super();
    }
    public Player(Player player){
        super();
        this.playerId=player.getPlayerId();
        this.playerName=player.getPlayerName();
        this.playerDateofBirth=player.getPlayerDateofBirth();
        this.playerBattingStyle=player.getPlayerBattingStyle();
        this.playerBowlingStyle=player.getPlayerBowlingStyle();
        this.playerExpertise=player.getPlayerExpertise();
    }
    public Player(String name){
        super();
        this.playerName=name;

    }

    @SerializedName("playerId")
    @Expose
    @Column(name = "playerId")
    private String playerId;
    @SerializedName("playerName")
    @Expose
    @Column(name = "playerName")
    private String playerName;
    @SerializedName("playerDateofBirth")
    @Expose
    private Object playerDateofBirth;
    @SerializedName("playerBattingStyle")
    @Expose
    @Column(name = "playerBattingStyle")
    private String playerBattingStyle;
    @SerializedName("playerBowlingStyle")
    @Expose
    @Column(name = "playerBowlingStyle")
    private String playerBowlingStyle;

    @SerializedName("playerExpertise")
    @Expose
    @Column(name = "playerExpertise")
    private String playerExpertise;

    @SerializedName("playerBowlingType")
    @Expose
    @Column(name = "playerBowlingType")
    private String playerBowlingType;

    private boolean isSelected;




    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Object getPlayerDateofBirth() {
        return playerDateofBirth;
    }

    public void setPlayerDateofBirth(Object playerDateofBirth) {
        this.playerDateofBirth = playerDateofBirth;
    }

    public String getPlayerBattingStyle() {
        return playerBattingStyle;
    }

    public void setPlayerBattingStyle(String playerBattingStyle) {
        this.playerBattingStyle = playerBattingStyle;
    }

    public String getPlayerBowlingStyle() {
        return playerBowlingStyle;
    }

    public void setPlayerBowlingStyle(String playerBowlingStyle) {
        this.playerBowlingStyle = playerBowlingStyle;
    }

    public String getPlayerExpertise() {
        return playerExpertise;
    }

    public void setPlayerExpertise(String playerExpertise) {
        this.playerExpertise = playerExpertise;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPlayerBowlingType() {
        return playerBowlingType;
    }

    public void setPlayerBowlingType(String playerBowlingType) {
        this.playerBowlingType = playerBowlingType;
    }

}

package com.gov.pitb.pcb.data.model;

/**
 * Created by nadirhussain on 03/08/2017.
 */

public class BatScoreCardModel {
    private String playerName;
    private int runs;
    private int balls;
    private int fours;
    private int sixes;
    private boolean hasPlayed;

    public BatScoreCardModel(String name,boolean hasPlayed){
        this.playerName=name;
        this.hasPlayed=hasPlayed;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }
    public double getStrikeRate(){
        return (runs*100)/balls;
    }

    public boolean isHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }



}

package com.gov.pitb.pcb.data.model;

/**
 * Created by nadirhussain on 03/08/2017.
 */

public class BowlScoreCardModel {
    private String playerName;
    private int overs;
    private int ballsBowled;
    private int mOvers;
    private int runs;
    private int wickets;
    private int zeros;
    private int fours;
    private int sixes;
    private boolean hasPlayed;

    public BowlScoreCardModel(String name,boolean hasPlayed){
        this.playerName=name;
        this.hasPlayed=hasPlayed;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getOvers() {
        return overs;
    }

    public void setOvers(int overs) {
        this.overs = overs;
    }

    public int getmOvers() {
        return mOvers;
    }

    public void setmOvers(int mOvers) {
        this.mOvers = mOvers;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public int getZeros() {
        return zeros;
    }

    public void setZeros(int zeros) {
        this.zeros = zeros;
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
    public boolean isHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    public double getEconomy(){
        return (runs/ballsBowled)*6;
    }

    public int getBallsBowled() {
        return ballsBowled;
    }

    public void setBallsBowled(int ballsBowled) {
        this.ballsBowled = ballsBowled;
    }

}

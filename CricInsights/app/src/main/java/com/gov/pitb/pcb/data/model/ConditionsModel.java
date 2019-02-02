package com.gov.pitb.pcb.data.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by nadirhussain on 03/08/2017.
 */

public class ConditionsModel extends Model {
    private String matchId;
    private String inningId;
    private int overId;
    @Column(name = "humidity")
    private String humidity;
    @Column(name = "grass")
    private String grass;
    @Column(name = "hardness")
    private String hardness;
    @Column(name = "overallWeather")
    private String overallWeather;
    @Column(name = "clouds")
    private String clouds;

    public ConditionsModel() {
        super();
    }

    public ConditionsModel(String matchId, String inningId, int overId) {
        super();
        this.matchId = matchId;
        this.inningId = inningId;
        this.overId = overId;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getGrass() {
        return grass;
    }

    public void setGrass(String grass) {
        this.grass = grass;
    }

    public String getHardness() {
        return hardness;
    }

    public void setHardness(String hardness) {
        this.hardness = hardness;
    }

    public String getOverallWeather() {
        return overallWeather;
    }

    public void setOverallWeather(String overallWeather) {
        this.overallWeather = overallWeather;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }


}

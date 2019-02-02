package com.brainpixel.cletracker.model;

import java.io.Serializable;

/**
 * Created by nadirhussain on 16/04/2017.
 */

public class Cycle implements Serializable {
    private String title;
    private String startDate;
    private String endDate;
    private String minCalendarDate;
    private Requirements requirements;
    private int creditsDone;

    public Cycle() {

    }

    public Cycle(String title, String startDate, String endDate, String minCalendarDate, Requirements requirements) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minCalendarDate = minCalendarDate;
        this.requirements = requirements;
        creditsDone = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Requirements getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public int getCreditsDone() {
        return creditsDone;
    }

    public void setCreditsDone(int creditsDone) {
        this.creditsDone = creditsDone;
    }

    public String getMinCalendarDate() {
        return minCalendarDate;
    }

    public void setMinCalendarDate(String minCalendarDate) {
        this.minCalendarDate = minCalendarDate;
    }


}

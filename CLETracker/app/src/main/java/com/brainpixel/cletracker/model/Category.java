package com.brainpixel.cletracker.model;

import java.io.Serializable;

/**
 * Created by nadirhussain on 16/04/2017.
 */

public class Category implements Serializable {
    private String name;
    private int requiredHours;
    private int hoursDone;

    public Category() {

    }

    public Category(String name, int requiredHours) {
        this.name = name;
        this.requiredHours = requiredHours;
    }

    public int getRequiredHours() {
        return requiredHours;
    }

    public void setRequiredHours(int requiredHours) {
        this.requiredHours = requiredHours;
    }

    public int getHoursDone() {
        return hoursDone;
    }

    public void setHoursDone(int hoursDone) {
        this.hoursDone = hoursDone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

package com.brainpixel.cletracker.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nadirhussain on 16/04/2017.
 */

public class Requirements implements Serializable {
    private int totalCreditHours;
    private ArrayList<Category> requiredCategories;

    public Requirements(){

    }
    public Requirements(int totalCreditHours, ArrayList<Category> requiredCategories) {
        this.totalCreditHours = totalCreditHours;
        this.requiredCategories = requiredCategories;
    }

    public int getTotalCreditHours() {
        return totalCreditHours;
    }

    public void setTotalCreditHours(int totalCreditHours) {
        this.totalCreditHours = totalCreditHours;
    }

    public ArrayList<Category> getRequiredCategories() {
        return requiredCategories;
    }

    public void setRequiredCategories(ArrayList<Category> requiredCategories) {
        this.requiredCategories = requiredCategories;
    }


}

package com.brainpixel.valetapp.model.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nadirhussain on 07/03/2017.
 */

public class OverviewPolyline {
    @SerializedName("points")
    @Expose
    private String routePoints;

    public String getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(String routePoints) {
        this.routePoints = routePoints;
    }


}

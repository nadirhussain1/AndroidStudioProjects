package com.brainpixel.valetapp.model.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nadirhussain on 07/03/2017.
 */

public class RouteResultResponse {
    @SerializedName("routes")
    @Expose
    private List<Routes> routes;

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }


}

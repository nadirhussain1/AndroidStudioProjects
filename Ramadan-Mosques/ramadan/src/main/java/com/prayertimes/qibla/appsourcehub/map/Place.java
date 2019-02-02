package com.prayertimes.qibla.appsourcehub.map;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class Place
    implements Serializable
{
    public static class Geometry
        implements Serializable
    {
    	@Key
        public Location location;

        public Geometry()
        {
        }
    }

    public static class Location
        implements Serializable
    {
    	@Key
        public double lat;
    	@Key
        public double lng;

        public Location()
        {
        }
    }

    @Key
    public String formatted_address;
    @Key
    public String formatted_phone_number;
    @Key
    public Geometry geometry;
    @Key
    public String icon;
    @Key
    public String id;
    @Key
    public String name;
    @Key
    public String reference;
    @Key
    public String vicinity;

    public Place()
    {
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(name))).append(" - ").append(id).append(" - ").append(reference).toString();
    }
}

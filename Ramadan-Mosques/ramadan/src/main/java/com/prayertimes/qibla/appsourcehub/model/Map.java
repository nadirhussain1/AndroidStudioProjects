package com.prayertimes.qibla.appsourcehub.model;


public class Map
{

    private String lat;
    private String lng;
    private String name;
    private String reference;

    public Map(String s, String s1, String s2, String s3)
    {
        reference = s;
        lat = s1;
        lng = s2;
        name = s3;
    }

    public String getlat()
    {
        return lat;
    }

    public String getlng()
    {
        return lng;
    }

    public String getname()
    {
        return name;
    }

    public String getreference()
    {
        return reference;
    }

    public void setlat(String s)
    {
        lat = s;
    }

    public void setlng(String s)
    {
        lng = s;
    }

    public void setname(String s)
    {
        name = s;
    }

    public void setreference(String s)
    {
        reference = s;
    }
}

package com.prayertimes.qibla.appsourcehub.model;


public class Events
{

    private String date;
    private String event;
    private String month;

    public Events(String s, String s1, String s2)
    {
        month = s;
        date = s1;
        event = s2;
    }

    public String getDate()
    {
        return date;
    }

    public String getEvent()
    {
        return event;
    }

    public String getMonth()
    {
        return month;
    }

    public void setDate(String s)
    {
        date = s;
    }

    public void setEvent(String s)
    {
        event = s;
    }

    public void setMonth(String s)
    {
        month = s;
    }
}

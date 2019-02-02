package com.prayertimes.qibla.appsourcehub.map;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class PlaceDetails
    implements Serializable
{
	@Key
    public Place result;
	@Key
    public String status;

    public PlaceDetails()
    {
    }

    public String toString()
    {
        if(result != null)
        {
            return result.toString();
        } else
        {
            return super.toString();
        }
    }
}

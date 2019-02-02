package com.prayertimes.qibla.appsourcehub.map;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

public class PlacesList
    implements Serializable
{
	@Key
    public List<Place>	 results;
	@Key
    public String status;

    public PlacesList()
    {
    }
}

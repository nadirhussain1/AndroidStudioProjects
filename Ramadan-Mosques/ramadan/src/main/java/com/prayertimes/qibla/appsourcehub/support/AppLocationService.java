package com.prayertimes.qibla.appsourcehub.support;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;

public class AppLocationService extends Service
    implements LocationListener
{

    private static final long MIN_DISTANCE_FOR_UPDATE = 10L;
    private static final long MIN_TIME_FOR_UPDATE = 0x1d4c0L;
    String bestProvider;
    Location location;
    protected LocationManager locationManager;

    public AppLocationService(Context context)
    {
        locationManager = (LocationManager)context.getSystemService("location");
    }

    public Location getLocation()
    {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(2);
        criteria.setPowerRequirement(1);
        bestProvider = locationManager.getBestProvider(criteria, false);
        if(bestProvider != null)
        {
            location = locationManager.getLastKnownLocation(bestProvider);
            locationManager.requestLocationUpdates(bestProvider, 0x1d4c0L, 10F, this);
            return location;
        } else
        {
            return null;
        }
    }

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onLocationChanged(Location location1)
    {
    }

    public void onProviderDisabled(String s)
    {
    }

    public void onProviderEnabled(String s)
    {
    }

    public void onStatusChanged(String s, int i, Bundle bundle)
    {
    }

    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }
    }
}

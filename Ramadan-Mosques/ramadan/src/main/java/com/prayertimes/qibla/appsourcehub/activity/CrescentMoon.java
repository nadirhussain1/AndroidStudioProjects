package com.prayertimes.qibla.appsourcehub.activity;

public class CrescentMoon extends MoonPhases
{

    EclipticPosition eclipPos;

    public CrescentMoon()
    {
    }

    public double calculatePhase(double d)
    {
        double d1 = EclipticPosition.getMiniSunLongitude(d - 1.5818693436763253E-007D);
        double ad[] = EclipticPosition.getMiniMoon(d);
        double d2 = ad[0];
        double d3 = ad[1];
        double d4 = d2 - d1;
        double d5 = Math.sqrt(d4 * d4 + d3 * d3);
        return (3.1415926535897931D * 8D) / 180D - d5;
    }
}

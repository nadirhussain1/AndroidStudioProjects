package com.prayertimes.qibla.appsourcehub.activity;

public class NewMoon extends MoonPhases
{

    EclipticPosition eclipPos;

    public NewMoon()
    {
        eclipPos = new EclipticPosition();
    }

    private double Frac(double d)
    {
        return d - Math.floor(d);
    }

    private double Modulo(double d, double d1)
    {
        return d1 * Frac(d / d1);
    }

    public double calculatePhase(double d)
    {
        double d1 = EclipticPosition.getMiniSunLongitude(d - 1.5818693436763253E-007D);
        double ad[] = EclipticPosition.getMiniMoon(d);
        double d2 = ad[0];
        double _tmp = ad[1];
        return Modulo(3.1415926535897931D + (d2 - d1), 6.2831853071795862D) - 3.1415926535897931D;
    }
}

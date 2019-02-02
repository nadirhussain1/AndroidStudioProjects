package com.prayertimes.qibla.appsourcehub.activity;

public class EclipticPosition
{

    public EclipticPosition()
    {
    }

    static double[] getMiniMoon(double d)
    {
        double ad[] = new double[2];
        double d1 = APC_Math.Frac(0.606433D + 1336.855225D * d);
        double d2 = 6.2831853071795862D * APC_Math.Frac(0.37489699999999998D + 1325.55241D * d);
        double d3 = 6.2831853071795862D * APC_Math.Frac(0.99313300000000004D + 99.997360999999998D * d);
        double d4 = 6.2831853071795862D * APC_Math.Frac(0.82736100000000001D + 1236.8530860000001D * d);
        double d5 = 6.2831853071795862D * APC_Math.Frac(0.25908599999999998D + 1342.2278249999999D * d);
        double d6 = ((((((22640D * Math.sin(d2) - 4586D * Math.sin(d2 - 2D * d4)) + 2370D * Math.sin(2D * d4) + 769D * Math.sin(2D * d2)) - 668D * Math.sin(d3) - 412D * Math.sin(2D * d5) - 212D * Math.sin(2D * d2 - 2D * d4) - 206D * Math.sin((d2 + d3) - 2D * d4)) + 192D * Math.sin(d2 + 2D * d4)) - 165D * Math.sin(d3 - 2D * d4) - 125D * Math.sin(d4) - 110D * Math.sin(d2 + d3)) + 148D * Math.sin(d2 - d3)) - 55D * Math.sin(2D * d5 - 2D * d4);
        double d7 = d5 + (d6 + 412D * Math.sin(2D * d5) + 541D * Math.sin(d3)) / 206264.80624709636D;
        double d8 = d5 - 2D * d4;
        double d9 = ((((-526D * Math.sin(d8) + 44D * Math.sin(d2 + d8)) - 31D * Math.sin(d8 + -d2) - 23D * Math.sin(d3 + d8)) + 11D * Math.sin(d8 + -d3)) - 25D * Math.sin(d5 + -2D * d2)) + 21D * Math.sin(d5 + -d2);
        ad[0] = 6.2831853071795862D * APC_Math.Frac(d1 + d6 / 1296000D);
        ad[1] = (d9 + 18520D * Math.sin(d7)) / 206264.80624709636D;
        return ad;
    }

    static double getMiniSunLongitude(double d)
    {
        double d1 = 6.2831853071795862D * APC_Math.Frac(0.99313300000000004D + 99.997360999999998D * d);
        return 6.2831853071795862D * APC_Math.Frac(0.78594529999999996D + d1 / 6.2831853071795862D + (6893D * Math.sin(d1) + 72D * Math.sin(2D * d1) + 6191.1999999999998D * d) / 1296000D);
    }
}

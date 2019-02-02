package com.prayertimes.qibla.appsourcehub.activity;

import java.util.Calendar;

public class APC_Time
{

    public APC_Time()
    {
    }

    public static Calendar CalDat(double d)
    {
        Calendar calendar = Calendar.getInstance();
        long l = (long)(2400001D + d);
        long l2;
        long l3;
        long l4;
        long l5;
        int i;
        int j;
        int k;
        double d1;
        int i1;
        int j1;
        if(l < 0x231519L)
        {
            l2 = l + 1524L;
        } else
        {
            long l1 = (long)(((double)l - 1867216.25D) / 36524.25D);
            l2 = 1525L + ((l + l1) - l1 / 4L);
        }
        l3 = (long)(((double)l2 - 122.09999999999999D) / 365.25D);
        l4 = 365L * l3 + l3 / 4L;
        l5 = (long)((double)(l2 - l4) / 30.600100000000001D);
        i = (int)(l2 - l4 - (long)(int)(30.600100000000001D * (double)l5));
        j = (int)(l5 - 1L - 12L * (l5 / 14L));
        k = (int)(l3 - 4715L - (long)((j + 7) / 10));
        d1 = 24D * (d - Math.floor(d));
        i1 = (int)Math.round(60D * (d1 - (double)(int)d1));
        j1 = (int)d1;
        calendar.set(1, k);
        calendar.set(2, j - 1);
        calendar.set(5, i);
        calendar.set(11, j1);
        calendar.set(12, i1);
        return calendar;
    }

    public static double Mjd(int i, int j, int k, int l, int i1, double d)
    {
        if(j <= 2)
        {
            j += 12;
            i--;
        }
        int j1;
        long l1;
        if(10000L * (long)i + 100L * (long)j + (long)k <= 0xf168ccL)
        {
            j1 = -1179 + (-2 + (i + 4716) / 4);
        } else
        {
            j1 = (i / 400 - i / 100) + i / 4;
        }
        l1 = (365L * (long)i - 0xa5c5cL) + (long)j1 + (long)(int)(30.600100000000001D * (double)(j + 1)) + (long)k;
        return APC_Math.Ddd(l, i1, d) / 24D + (double)l1;
    }
}

package com.prayertimes.qibla.appsourcehub.activity;


// Referenced classes of package com.prayertimes.qibla.appsourcehub.activity:
//            MoonPhases

public class APC_Math
{

    public APC_Math()
    {
    }

    static double Ddd(int i, int j, double d)
    {
        double d1;
        if(i < 0 || j < 0 || d < 0.0D)
        {
            d1 = -1D;
        } else
        {
            d1 = 1.0D;
        }
        return d1 * ((double)Math.abs(i) + (double)Math.abs(j) / 60D + Math.abs(d) / 3600D);
    }

    static double Frac(double d)
    {
        return d - (double)(long)d;
    }

    public static double Pegasus(MoonPhases moonphases, double d, double d1, double d2, boolean aflag[])
    {
        double d3 = d;
        double d4 = d1;
        double d5 = moonphases.calculatePhase(d3);
        double d6 = moonphases.calculatePhase(d4);
        aflag[0] = false;
        double d7 = d3;
        int j = 0;
        if(d5 * d6 < 0.0D)
        {
            do
            {
                double d8 = d4 - d6 / ((d6 - d5) / (d4 - d3));
                double d9 = moonphases.calculatePhase(d8);
                boolean flag;
                if(d9 * d6 <= 0.0D)
                {
                    d3 = d4;
                    d5 = d6;
                    d4 = d8;
                    d6 = d9;
                } else
                {
                    d5 = (d5 * d6) / (d6 + d9);
                    d4 = d8;
                    d6 = d9;
                }
                if(Math.abs(d5) < Math.abs(d6))
                {
                    d7 = d3;
                } else
                {
                    d7 = d4;
                }
                if(Math.abs(d4 - d3) <= d2)
                {
                    flag = true;
                } else
                {
                    flag = false;
                }
                aflag[0] = flag;
                j++;
            } while(!aflag[0] && j < 30);
        }
        return d7;
    }
}

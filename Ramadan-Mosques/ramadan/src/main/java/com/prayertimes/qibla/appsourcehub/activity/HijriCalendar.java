package com.prayertimes.qibla.appsourcehub.activity;

import java.util.Calendar;

public class HijriCalendar
{

    final double Acc = 9.5064263442086855E-009D;
    private int Lunation;
    private double MJD;
    final double MJD_J2000 = 51544.5D;
    final double MLunatBase = 23435.903470000001D;
    private Calendar cal;
    private double crescentMoonMoment;
    final double dT = 0.00019164955509924709D;
    final double dTc = 8.2135523613963032E-005D;
    private int hijriDay;
    private int hijriMonth;
    private int hijriYear;
    private boolean isFound[];
    private String ismiSuhiri[] = {
        "\u0645\u062D\u0631\u0645", "\u0635\uFFFD?\u0631", "\u0631\u0628\u064A\u0639 \u0627\u0644\u0623\u0648\u0644", "\u0631\u0628\u064A\u0639 \u0627\u0644\u0622\u062E\u0631", "\u062C\u0645\u0627\u062F\u0649 \u0627\u0644\u0623\u0648\u0644\u0649", "\u062C\u0645\u0627\u062F\u0649 \u0627\u0644\u0622\u062E\u0631\u0629", "\u0631\u062C\u0628", "\u0634\u0639\u0628\u0627\u0646", "\u0631\u0645\u0636\u0627\u0646", "\u0634\u0648\u0627\u0644", 
        "\u0630\u0648 \u0627\u0644\u0642\u0639\u062F\u0629", "\u0630\u0648 \u0627\u0644\u062D\u062C\u0629"
    };
    private double newMoonMoment;
    final double synmonth = 29.530588860999998D;

    public HijriCalendar(int i, int j, int k)
    {
        MJD = APC_Time.Mjd(i, j, k, 0, 0, 0.0D);
        cal = APC_Time.CalDat(MJD);
        double d = (MJD - 51544.5D) / 36525D;
        double d1 = d - 0.00019164955509924709D;
        isFound = new boolean[1];
        isFound[0] = false;
        NewMoon newmoon = new NewMoon();
        CrescentMoon crescentmoon = new CrescentMoon();
        double d2 = newmoon.calculatePhase(d);
        double d3 = newmoon.calculatePhase(d1);
        do
        {
            if(d3 * d2 <= 0.0D && d2 >= d3)
            {
                double d4 = APC_Math.Pegasus(newmoon, d1, d, 9.5064263442086855E-009D, isFound);
                newMoonMoment = 51544.5D + 36525D * d4;
                Lunation = 1 + (int)Math.floor(((7D + newMoonMoment) - 23435.903470000001D) / 29.530588860999998D);
                hijriYear = 1341 + (4 + Lunation) / 12;
                hijriMonth = 1 + (4 + Lunation) % 12;
                if(isFound[0])
                {
                    crescentMoonMoment = 51544.5D + 36525D * APC_Math.Pegasus(crescentmoon, d4, d4 + 8.2135523613963032E-005D, 9.5064263442086855E-009D, isFound);
                }
                hijriDay = 1 + (int)(MJD - (double)Math.round(0.27916666666666701D + crescentMoonMoment));
                if(hijriDay == 0)
                {
                    hijriDay = 30;
                    hijriMonth = -1 + hijriMonth;
                    if(hijriMonth == 0)
                    {
                        hijriMonth = 12;
                    }
                }
                return;
            }
            d = d1;
            d2 = d3;
            d1 -= 0.00019164955509924709D;
            d3 = newmoon.calculatePhase(d1);
        } while(true);
    }

    public String checkIfHolyDay()
    {
        String s = "";
        switch(hijriMonth){
        default:case 2:case 4:case 5:case 6:case 11:
        	break;
        case 1:
        	if(hijriDay == 1)
            {
                return "NEWYEAR";
            }
            if(hijriDay == 10)
            {
                return "ASHURA";
            }
        	break;
        case 3:
        	if(hijriDay == 11 || hijriDay == 12)
            {
                return "MAWLID";
            }
        	break;
        case 7:
        	if(hijriDay == 1 && hijriMonth == 7)
            {
                s = "HOLYMONTHS";
            }
            if(cal.get(7) == 6 && hijriDay < 7)
            {
                s = "RAGHAIB";
            }
            if(hijriDay == 27)
            {
                return "MERAC";
            }
        	break;
        case 8:
        	if(hijriDay == 15)
            {
                return "BARAAT";
            }
        	break;
        case 9:
        	if(hijriDay == 27)
            {
                return "QADR";
            }
        	break;
        case 10:
        	if(hijriDay == 1 || hijriDay == 2 || hijriDay == 3)
            {
                return (new StringBuilder(String.valueOf(hijriDay))).append("DAYOFEIDFITR").toString();
            }
        	break;
        case 12:
        	if(hijriDay == 9)
            {
                s = "AREFE";
            }
            if(hijriDay == 10 || hijriDay == 11 || hijriDay == 12 || hijriDay == 13)
            {
                return (new StringBuilder(String.valueOf(-9 + hijriDay))).append("DAYOFEIDAHDA").toString();
            }
        	break;
        }
        return s;
    }

    public String getDay()
    {
        return (new String[] {
            "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"
        })[-1 + cal.get(7)];
    }

    public String getHicriTakvim()
    {
        return (new StringBuilder(String.valueOf(getHijriDay()))).append(" ").append(getHijriMonthName()).append(" ").append(getHijriYear()).toString();
    }

    public int getHijriDay()
    {
        return hijriDay;
    }

    public int getHijriMonth()
    {
        return hijriMonth;
    }

    public String getHijriMonthName()
    {
        return ismiSuhiri[-1 + hijriMonth];
    }

    public int getHijriYear()
    {
        return hijriYear;
    }
}

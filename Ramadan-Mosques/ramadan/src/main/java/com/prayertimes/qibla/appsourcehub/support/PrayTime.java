package com.prayertimes.qibla.appsourcehub.support;

import java.util.*;

public class PrayTime
{

    public int AngleBased;
    public int Custom;
    public int Egypt;
    public int Floating;
    public int Hanafi;
    public int ISNA;
    public String InvalidTime;
    public double JDate;
    public int Jafari;
    public int Karachi;
    public int MWL;
    public int Makkah;
    public int MidNight;
    public int None;
    public int OneSeventh;
    public int Shafii;
    public int Tehran;
    public int Time12;
    public int Time12NS;
    public int Time24;
    public int adjustHighLats;
    public int asrJuristic;
    public int calcMethod;
    public int dhuhrMinutes;
    public double lat;
    public double lng;
    public HashMap methodParams;
    public int numIterations;
    public int offsets[];
    public double prayerTimesCurrent[];
    public int timeFormat;
    public ArrayList timeNames;
    public double timeZone;

    public PrayTime()
    {
        setCalcMethod(1);
        setAsrJuristic(0);
        setDhuhrMinutes(0);
        setAdjustHighLats(1);
        setTimeFormat(0);
        setJafari(0);
        setKarachi(1);
        setISNA(2);
        setMWL(3);
        setMakkah(4);
        setEgypt(5);
        setTehran(6);
        setCustom(7);
        setShafii(0);
        setHanafi(1);
        setNone(0);
        setMidNight(1);
        setOneSeventh(2);
        setAngleBased(3);
        setTime24(0);
        setTime12(1);
        setTime12NS(2);
        setFloating(3);
        timeNames = new ArrayList();
        timeNames.add("Fajr");
        timeNames.add("Dhuhr");
        timeNames.add("Asr");
        timeNames.add("Maghrib");
        timeNames.add("Isha");
        InvalidTime = "-----";
        setNumIterations(1);
        offsets = new int[7];
        offsets[0] = 0;
        offsets[1] = 0;
        offsets[2] = 0;
        offsets[3] = 0;
        offsets[4] = 0;
        offsets[5] = 0;
        offsets[6] = 0;
        methodParams = new HashMap();
        double ad[] = {
            16D, 0.0D, 4D, 0.0D, 14D
        };
        methodParams.put(Integer.valueOf(getJafari()), ad);
        double ad1[] = {
            18D, 1.0D, 0.0D, 0.0D, 18D
        };
        methodParams.put(Integer.valueOf(getKarachi()), ad1);
        double ad2[] = {
            15D, 1.0D, 0.0D, 0.0D, 15D
        };
        methodParams.put(Integer.valueOf(getISNA()), ad2);
        double ad3[] = {
            18D, 1.0D, 0.0D, 0.0D, 17D
        };
        methodParams.put(Integer.valueOf(getMWL()), ad3);
        double ad4[] = {
            18.5D, 1.0D, 0.0D, 1.0D, 90D
        };
        methodParams.put(Integer.valueOf(getMakkah()), ad4);
        double ad5[] = {
            19.5D, 1.0D, 0.0D, 0.0D, 17.5D
        };
        methodParams.put(Integer.valueOf(getEgypt()), ad5);
        double ad6[] = {
            17.699999999999999D, 0.0D, 4.5D, 0.0D, 14D
        };
        methodParams.put(Integer.valueOf(getTehran()), ad6);
        double ad7[] = {
            18D, 1.0D, 0.0D, 0.0D, 17D
        };
        methodParams.put(Integer.valueOf(getCustom()), ad7);
    }

    public double DegreesToRadians(double d)
    {
        return (3.1415926535897931D * d) / 180D;
    }

    public double[] adjustHighLatTimes(double ad[])
    {
        double d = timeDiff(ad[4], ad[1]);
        double d1 = d * nightPortion(((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[0]);
        if(Double.isNaN(ad[0]) || timeDiff(ad[0], ad[1]) > d1)
        {
            ad[0] = ad[1] - d1;
        }
        double d2;
        double d3;
        double d4;
        double d5;
        if(((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[3] == 0.0D)
        {
            d2 = ((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[4];
        } else
        {
            d2 = 18D;
        }
        d3 = d * nightPortion(d2);
        if(Double.isNaN(ad[6]) || timeDiff(ad[4], ad[6]) > d3)
        {
            ad[6] = d3 + ad[4];
        }
        if(((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[1] == 0.0D)
        {
            d4 = ((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[2];
        } else
        {
            d4 = 4D;
        }
        d5 = d * nightPortion(d4);
        if(Double.isNaN(ad[5]) || timeDiff(ad[4], ad[5]) > d5)
        {
            ad[5] = d5 + ad[4];
        }
        return ad;
    }

    public double[] adjustTimes(double ad[])
    {
        int i = 0;
        do
        {
            if(i >= ad.length)
            {
                ad[2] = ad[2] + (double)(getDhuhrMinutes() / 60);
                if(((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[1] == 1.0D)
                {
                    ad[5] = ad[4] + ((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[2] / 60D;
                }
                if(((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[3] == 1.0D)
                {
                    ad[6] = ad[5] + ((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[4] / 60D;
                }
                if(getAdjustHighLats() != getNone())
                {
                    ad = adjustHighLatTimes(ad);
                }
                return ad;
            }
            ad[i] = ad[i] + (getTimeZone() - getLng() / 15D);
            i++;
        } while(true);
    }

    public ArrayList adjustTimesFormat(double ad[])
    {
        int i;
        ArrayList arraylist;
        int k;
        i = 0;
        arraylist = new ArrayList();
        if(getTimeFormat() != getFloating())
        {
        	int j = 0;
            while(j < 7) 
            {
                if(getTimeFormat() == getTime12())
                {
                    arraylist.add(floatToTime12(ad[j], false));
                } else
                if(getTimeFormat() == getTime12NS())
                {
                    arraylist.add(floatToTime12(ad[j], true));
                } else
                {
                    arraylist.add(floatToTime24(ad[j]));
                }
                j++;
            }
            return arraylist;
        }
        k = ad.length;
        while(i < k){
        	arraylist.add(String.valueOf(ad[i]));
            i++;
        }
        return arraylist;
    }

    public double calcJD(int i, int j, int k)
    {
        return (2440588D + Math.floor((double)(new Date(i, j - 1, k)).getTime() / 86400000D)) - 0.5D;
    }

    public double computeAsr(double d, double d1)
    {
        double d2 = sunDeclination(d1 + getJDate());
        return computeTime(-darccot(d + dtan(Math.abs(getLat() - d2))), d1);
    }

    public ArrayList computeDayTimes()
    {
        double ad[] = {
            5D, 6D, 12D, 13D, 18D, 18D, 18D
        };
        int i = 1;
        do
        {
            if(i > getNumIterations())
            {
                return adjustTimesFormat(tuneTimes(adjustTimes(ad)));
            }
            ad = computeTimes(ad);
            i++;
        } while(true);
    }

    public double computeMidDay(double d)
    {
        return fixhour(12D - equationOfTime(d + getJDate()));
    }

    public double computeTime(double d, double d1)
    {
        double d2 = sunDeclination(d1 + getJDate());
        double d3 = computeMidDay(d1);
        double d4 = darccos((-dsin(d) - dsin(d2) * dsin(getLat())) / (dcos(d2) * dcos(getLat()))) / 15D;
        if(d > 90D)
        {
            d4 = -d4;
        }
        return d3 + d4;
    }

    public double[] computeTimes(double ad[])
    {
        double ad1[] = dayPortion(ad);
        return (new double[] {
            computeTime(180D - ((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[0], ad1[0]), computeTime(179.167D, ad1[1]), computeMidDay(ad1[2]), computeAsr(1 + getAsrJuristic(), ad1[3]), computeTime(0.83299999999999996D, ad1[4]), computeTime(((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[2], ad1[5]), computeTime(((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[4], ad1[6])
        });
    }

    public double darccos(double d)
    {
        return radiansToDegrees(Math.acos(d));
    }

    public double darccot(double d)
    {
        return radiansToDegrees(Math.atan2(1.0D, d));
    }

    public double darcsin(double d)
    {
        return radiansToDegrees(Math.asin(d));
    }

    public double darctan(double d)
    {
        return radiansToDegrees(Math.atan(d));
    }

    public double darctan2(double d, double d1)
    {
        return radiansToDegrees(Math.atan2(d, d1));
    }

    public double[] dayPortion(double ad[])
    {
        int i = 0;
        do
        {
            if(i >= 7)
            {
                return ad;
            }
            ad[i] = ad[i] / 24D;
            i++;
        } while(true);
    }

    public double dcos(double d)
    {
        return Math.cos(DegreesToRadians(d));
    }

    public double detectDaylightSaving()
    {
        return (double)TimeZone.getDefault().getDSTSavings();
    }

    public double dsin(double d)
    {
        return Math.sin(DegreesToRadians(d));
    }

    public double dtan(double d)
    {
        return Math.tan(DegreesToRadians(d));
    }

    public double equationOfTime(double d)
    {
        return sunPosition(d)[1];
    }

    public double fixangle(double d)
    {
        double d1 = d - 360D * Math.floor(d / 360D);
        if(d1 < 0.0D)
        {
            d1 += 360D;
        }
        return d1;
    }

    public double fixhour(double d)
    {
        double d1 = d - 24D * Math.floor(d / 24D);
        if(d1 < 0.0D)
        {
            d1 += 24D;
        }
        return d1;
    }

    public String floatToTime12(double d, boolean flag)
    {
        if(Double.isNaN(d))
        {
            return InvalidTime;
        }
        double d1 = fixhour(0.0083333333333333332D + d);
        int i = (int)Math.floor(d1);
        double d2 = Math.floor(60D * (d1 - (double)i));
        String s;
        int j;
        if(i >= 12)
        {
            s = "pm";
        } else
        {
            s = "am";
        }
        j = 1 + (-1 + (i + 12)) % 12;
        if(!flag)
        {
            if(j >= 0 && j <= 9 && d2 >= 0.0D && d2 <= 9D)
            {
                return (new StringBuilder("0")).append(j).append(":0").append(Math.round(d2)).append(" ").append(s).toString();
            }
            if(j >= 0 && j <= 9)
            {
                return (new StringBuilder("0")).append(j).append(":").append(Math.round(d2)).append(" ").append(s).toString();
            }
            if(d2 >= 0.0D && d2 <= 9D)
            {
                return (new StringBuilder(String.valueOf(j))).append(":0").append(Math.round(d2)).append(" ").append(s).toString();
            } else
            {
                return (new StringBuilder(String.valueOf(j))).append(":").append(Math.round(d2)).append(" ").append(s).toString();
            }
        }
        if(j >= 0 && j <= 9 && d2 >= 0.0D && d2 <= 9D)
        {
            return (new StringBuilder("0")).append(j).append(":0").append(Math.round(d2)).toString();
        }
        if(j >= 0 && j <= 9)
        {
            return (new StringBuilder("0")).append(j).append(":").append(Math.round(d2)).toString();
        }
        if(d2 >= 0.0D && d2 <= 9D)
        {
            return (new StringBuilder(String.valueOf(j))).append(":0").append(Math.round(d2)).toString();
        } else
        {
            return (new StringBuilder(String.valueOf(j))).append(":").append(Math.round(d2)).toString();
        }
    }

    public String floatToTime12NS(double d)
    {
        return floatToTime12(d, true);
    }

    public String floatToTime24(double d)
    {
        if(Double.isNaN(d))
        {
            return InvalidTime;
        }
        double d1 = fixhour(0.0083333333333333332D + d);
        int i = (int)Math.floor(d1);
        double d2 = Math.floor(60D * (d1 - (double)i));
        if(i >= 0 && i <= 9 && d2 >= 0.0D && d2 <= 9D)
        {
            return (new StringBuilder("0")).append(i).append(":0").append(Math.round(d2)).toString();
        }
        if(i >= 0 && i <= 9)
        {
            return (new StringBuilder("0")).append(i).append(":").append(Math.round(d2)).toString();
        }
        if(d2 >= 0.0D && d2 <= 9D)
        {
            return (new StringBuilder(String.valueOf(i))).append(":0").append(Math.round(d2)).toString();
        } else
        {
            return (new StringBuilder(String.valueOf(i))).append(":").append(Math.round(d2)).toString();
        }
    }

    public int getAdjustHighLats()
    {
        return adjustHighLats;
    }

    public int getAngleBased()
    {
        return AngleBased;
    }

    public int getAsrJuristic()
    {
        return asrJuristic;
    }

    public double getBaseTimeZone()
    {
        return (double)TimeZone.getDefault().getRawOffset() / 1000D / 3600D;
    }

    public int getCalcMethod()
    {
        return calcMethod;
    }

    public int getCustom()
    {
        return Custom;
    }

    public ArrayList getDatePrayerTimes(int i, int j, int k, double d, double d1, 
            double d2)
    {
        setLat(d);
        setLng(d1);
        setTimeZone(d2);
        setJDate(julianDate(i, j, k));
        double d3 = d1 / 360D;
        setJDate(getJDate() - d3);
        return computeDayTimes();
    }

    public int getDhuhrMinutes()
    {
        return dhuhrMinutes;
    }

    public int getEgypt()
    {
        return Egypt;
    }

    public int getFloating()
    {
        return Floating;
    }

    public int getHanafi()
    {
        return Hanafi;
    }

    public int getISNA()
    {
        return ISNA;
    }

    public double getJDate()
    {
        return JDate;
    }

    public int getJafari()
    {
        return Jafari;
    }

    public int getKarachi()
    {
        return Karachi;
    }

    public double getLat()
    {
        return lat;
    }

    public double getLng()
    {
        return lng;
    }

    public int getMWL()
    {
        return MWL;
    }

    public int getMakkah()
    {
        return Makkah;
    }

    public int getMidNight()
    {
        return MidNight;
    }

    public int getNone()
    {
        return None;
    }

    public int getNumIterations()
    {
        return numIterations;
    }

    public int getOneSeventh()
    {
        return OneSeventh;
    }

    public ArrayList getPrayerTimes(Calendar calendar, double d, double d1, double d2)
    {
        int i = calendar.get(1);
        int j = calendar.get(2);
        int k = calendar.get(5);
        return getDatePrayerTimes(i, j + 1, k, d, d1, d2);
    }

    public int getShafii()
    {
        return Shafii;
    }

    public int getTehran()
    {
        return Tehran;
    }

    public int getTime12()
    {
        return Time12;
    }

    public int getTime12NS()
    {
        return Time12NS;
    }

    public int getTime24()
    {
        return Time24;
    }

    public int getTimeFormat()
    {
        return timeFormat;
    }

    public ArrayList getTimeNames()
    {
        return timeNames;
    }

    public double getTimeZone()
    {
        return timeZone;
    }

    public double getTimeZone1()
    {
        return (double)TimeZone.getDefault().getRawOffset() / 1000D / 3600D;
    }

    public double julianDate(int i, int j, int k)
    {
        if(j <= 2)
        {
            i--;
            j += 12;
        }
        double d = Math.floor((double)i / 100D);
        return ((2D - d) + Math.floor(d / 4D) + (Math.floor(365.25D * (double)(i + 4716)) + Math.floor(30.600100000000001D * (double)(j + 1)) + (double)k)) - 1524.5D;
    }

    public double nightPortion(double d)
    {
        double d1 = 0.0D;
        if(adjustHighLats == AngleBased)
        {
            d1 = d / 60D;
        } else
        {
            if(adjustHighLats == MidNight)
            {
                return 0.5D;
            }
            if(adjustHighLats == OneSeventh)
            {
                return 0.14285999999999999D;
            }
        }
        return d1;
    }

    public double radiansToDegrees(double d)
    {
        return (180D * d) / 3.1415926535897931D;
    }

    public void setAdjustHighLats(int i)
    {
        adjustHighLats = i;
    }

    public void setAngleBased(int i)
    {
        AngleBased = i;
    }

    public void setAsrJuristic(int i)
    {
        asrJuristic = i;
    }

    public void setCalcMethod(int i)
    {
        calcMethod = i;
    }

    public void setCustom(int i)
    {
        Custom = i;
    }

    public void setCustomParams(double ad[])
    {
        int i = 0;
        do
        {
            if(i >= 5)
            {
                setCalcMethod(getCustom());
                return;
            }
            if(ad[i] == -1D)
            {
                ad[i] = ((double[])methodParams.get(Integer.valueOf(getCalcMethod())))[i];
                methodParams.put(Integer.valueOf(getCustom()), ad);
            } else
            {
                ((double[])methodParams.get(Integer.valueOf(getCustom())))[i] = ad[i];
            }
            i++;
        } while(true);
    }

    public void setDhuhrMinutes(int i)
    {
        dhuhrMinutes = i;
    }

    public void setEgypt(int i)
    {
        Egypt = i;
    }

    public void setFajrAngle(double d)
    {
        setCustomParams(new double[] {
            d, -1D, -1D, -1D, -1D
        });
    }

    public void setFloating(int i)
    {
        Floating = i;
    }

    public void setHanafi(int i)
    {
        Hanafi = i;
    }

    public void setISNA(int i)
    {
        ISNA = i;
    }

    public void setIshaAngle(double d)
    {
        setCustomParams(new double[] {
            -1D, -1D, -1D, 0.0D, d
        });
    }

    public void setIshaMinutes(double d)
    {
        setCustomParams(new double[] {
            -1D, -1D, -1D, 1.0D, d
        });
    }

    public void setJDate(double d)
    {
        JDate = d;
    }

    public void setJafari(int i)
    {
        Jafari = i;
    }

    public void setKarachi(int i)
    {
        Karachi = i;
    }

    public void setLat(double d)
    {
        lat = d;
    }

    public void setLng(double d)
    {
        lng = d;
    }

    public void setMWL(int i)
    {
        MWL = i;
    }

    public void setMaghribAngle(double d)
    {
        setCustomParams(new double[] {
            -1D, 0.0D, d, -1D, -1D
        });
    }

    public void setMaghribMinutes(double d)
    {
        setCustomParams(new double[] {
            -1D, 1.0D, d, -1D, -1D
        });
    }

    public void setMakkah(int i)
    {
        Makkah = i;
    }

    public void setMidNight(int i)
    {
        MidNight = i;
    }

    public void setNone(int i)
    {
        None = i;
    }

    public void setNumIterations(int i)
    {
        numIterations = i;
    }

    public void setOneSeventh(int i)
    {
        OneSeventh = i;
    }

    public void setShafii(int i)
    {
        Shafii = i;
    }

    public void setTehran(int i)
    {
        Tehran = i;
    }

    public void setTime12(int i)
    {
        Time12 = i;
    }

    public void setTime12NS(int i)
    {
        Time12NS = i;
    }

    public void setTime24(int i)
    {
        Time24 = i;
    }

    public void setTimeFormat(int i)
    {
        timeFormat = i;
    }

    public void setTimeZone(double d)
    {
        timeZone = d;
    }

    public double sunDeclination(double d)
    {
        return sunPosition(d)[0];
    }

    public double[] sunPosition(double d)
    {
        double d1 = d - 2451545D;
        double d2 = fixangle(357.529D + 0.98560028D * d1);
        double d3 = fixangle(280.459D + 0.98564735999999997D * d1);
        double d4 = fixangle(d3 + 1.915D * dsin(d2) + 0.02D * dsin(2D * d2));
        double d5 = 23.439D - 3.5999999999999999E-007D * d1;
        double d6 = darcsin(dsin(d5) * dsin(d4));
        double d7 = fixhour(darctan2(dcos(d5) * dsin(d4), dcos(d4)) / 15D);
        return (new double[] {
            d6, d3 / 15D - d7
        });
    }

    public double timeDiff(double d, double d1)
    {
        return fixhour(d1 - d);
    }

    public void tune(int ai[])
    {
        int i = 0;
        do
        {
            if(i >= ai.length)
            {
                return;
            }
            offsets[i] = ai[i];
            i++;
        } while(true);
    }

    public double[] tuneTimes(double ad[])
    {
        int i = 0;
        do
        {
            if(i >= ad.length)
            {
                return ad;
            }
            ad[i] = ad[i] + (double)offsets[i] / 60D;
            i++;
        } while(true);
    }
}

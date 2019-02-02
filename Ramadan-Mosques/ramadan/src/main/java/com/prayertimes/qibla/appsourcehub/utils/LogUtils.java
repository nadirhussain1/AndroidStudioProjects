package com.prayertimes.qibla.appsourcehub.utils;

import android.util.Log;

public class LogUtils
{

    public static String DEFAULT_TAG = "Prayer Times";

    public LogUtils()
    {
    }

    public static void d(String s)
    {
    	if(s != null)
    		Log.d(DEFAULT_TAG, s);
    }

    public static void d(String s, String s1)
    {
    	if(s != null && s1 != null)
    		Log.d(s, s1);
    }

    public static void e(String s)
    {
    	if(s != null)
    		Log.e(DEFAULT_TAG, s);
    }

    public static void e(String s, String s1)
    {
    	if(s != null && s1 != null)
    		Log.e(s, s1);
    }

    public static void i(String s)
    {
    	if(s != null)
    		Log.i(DEFAULT_TAG, s);
    }

    public static void i(String s, String s1)
    {
    	if(s1 != null)
    		Log.i(DEFAULT_TAG, s1);
    }

}

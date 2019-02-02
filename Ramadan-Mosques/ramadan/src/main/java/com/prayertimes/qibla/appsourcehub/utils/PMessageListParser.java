package com.prayertimes.qibla.appsourcehub.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.dd.plist.*;
import com.prayertimes.qibla.appsourcehub.model.Messages;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class PMessageListParser
{

    public static final boolean LOAD_ALL_PACK = true;

    public PMessageListParser()
    {
    }

    public static List getMessages(Context context)
    {
        ArrayList arraylist = new ArrayList();
        try{
	        String s = (new StringBuilder(String.valueOf(getAppPath(context)))).append("/messages.plist").toString();
	        NSDictionary nsdictionary1;
	        if(!(new File(s)).exists()){
	        	NSDictionary nsdictionary = getNSDFromPath(context, "messages.plist");
	            nsdictionary1 = nsdictionary;
	        }else
	        	nsdictionary1 = getNSDFromPath(s);
	        NSObject ansobject[];
	        int i;
	        ansobject = ((NSArray)nsdictionary1.objectForKey("packs")).getArray();
	        Log.i(" ", (new StringBuilder(String.valueOf(ansobject.length))).append(" packs ").append(ansobject).toString());
	        i = ansobject.length;
	        int j;
	        Messages messages;
	        j = 0;
	        messages = null;
	        while(j < i){
	        	NSObject nsobject;
	            Messages messages1;
	            nsobject = ansobject[j];
	            messages1 = new Messages();
	            NSObject nsobject1 = ((NSDictionary)nsobject).objectForKey("title");
	            NSObject nsobject2 = ((NSDictionary)nsobject).objectForKey("meaning");
	        
	            messages1.setTitle(nsObjectToString(nsobject1));
	            messages1.setMeaning(nsObjectToString(nsobject2));
	           
	            System.out.println((new StringBuilder("!!!pack name ")).append(messages1.getTitle()).toString());
	            arraylist.add(messages1);
	            j++;
	            messages = messages1;
	        }
	        LogUtils.i(" ", (new StringBuilder(" mustache path ")).append(arraylist.size()).toString());
        }catch(Exception e){}
        return arraylist;
    }

    public static String getAppPath(Context context)
    {
        return context.getFilesDir().getAbsolutePath();
    }

    private static NSDictionary getNSDFromPath(Context context, String s)
    {
        NSDictionary nsdictionary;
        try
        {
            nsdictionary = (NSDictionary)PropertyListParser.parse(context.getAssets().open(s));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            System.out.println(s);
            return null;
        }
        return nsdictionary;
    }

    private static NSDictionary getNSDFromPath(String s)
    {
        NSDictionary nsdictionary;
        try
        {
            nsdictionary = (NSDictionary)PropertyListParser.parse(new File(s));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
        return nsdictionary;
    }

    private static boolean nsObjectToBoolean(NSObject nsobject)
    {
        boolean flag = false;
        if(nsobject != null)
        {
            boolean flag1 = nsobject.getClass().equals(com.dd.plist.NSNumber.class);
            flag = false;
            if(flag1)
            {
                NSNumber nsnumber = (NSNumber)nsobject;
                int i = nsnumber.type();
                flag = false;
                if(i == 2)
                {
                    flag = nsnumber.boolValue();
                }
            }
        }
        return flag;
    }

    private static String nsObjectToString(NSObject nsobject)
    {
        String s = "";
        if(nsobject != null && nsobject.toJavaObject() != null)
        {
            s = nsobject.toJavaObject().toString();
        }
        return s;
    }
}

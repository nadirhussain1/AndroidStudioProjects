package com.prayertimes.qibla.appsourcehub.support;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.*;
import android.util.Log;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.util.*;

public class LocationAddress extends Utils
{

    private static final String TAG = "LocationAddress";
    boolean value;

    public LocationAddress()
    {
        value = false;
    }

    public void getAddressFromLocation(Context context)
    {
    	Address address;
        StringBuilder stringbuilder;
        int i;
        Address address1;
        String s;
        String s1;
        String s2;
        String s3;
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        List list;
        Iterator iterator;
        try
        {
        	list = geocoder.getFromLocation(Double.parseDouble(loadString(USER_LAT)), Double.parseDouble(loadString(USER_LNG)), 1);
            iterator = list.iterator();
            while(iterator.hasNext()){
            	address1 = (Address)iterator.next();
                if(address1 == null)
                {
                    continue; /* Loop/switch isn't completed */
                }
                s = address1.getLocality();
                s1 = address1.getCountryName();
                s2 = address1.getAdminArea();
                s3 = address1.getSubLocality();
                if(s == null)
                {
                    continue; /* Loop/switch isn't completed */
                }
                if(s.equals("") || s1 == null)
                {
                    continue; /* Loop/switch isn't completed */
                }
                if(s1.equals("") || s2 == null)
                {
                    continue; /* Loop/switch isn't completed */
                }
                if(s2.equals("") || s3 == null)
                {
                    continue; /* Loop/switch isn't completed */
                }
                if(!s3.equals(""))
                {
                    SavePref(USER_CITY, s);
                    SavePref(USER_STATE, s2);
                    SavePref(USER_COUNTRY, s1);
                    SavePref(USER_STREET, s3);
                }
            }
            if(list == null)
            	return;
            address = (Address)list.get(0);
            stringbuilder = new StringBuilder("Your Present Location is:\n");
            i = 0;
            while(i < address.getMaxAddressLineIndex())
            {
            	stringbuilder.append(address.getAddressLine(i)).append("\n");
                i++;
            }
        }catch(Exception exception){}
    }

    public void getAddressFromLocation(final String locationAddress, Context context, final Handler handler) // strange
    {
        (new Thread() {
            public void run()
            {
            	Message message;
                Bundle bundle;
                Bundle bundle1;
                Message message1;
                Bundle bundle2;
                Bundle bundle3;
                Object obj;
                try{
	                Geocoder geocoder = new Geocoder(LocationAddress.this);
	                List list = geocoder.getFromLocationName(locationAddress, 1);
	                if(list != null)
	                {
	                	if(list.size() > 0)
	                    {
	                        Address address = (Address)list.get(0);
	                        SavePref(USER_LAT, String.valueOf(address.getLatitude()));
	                        SavePref(USER_LNG, String.valueOf(address.getLongitude()));
	                        value = true;
	                    }
	                }
	                Message message2 = Message.obtain();
	                message2.setTarget(handler);
	                message2.what = 1;
	                Bundle bundle4 = new Bundle();
	                bundle4.putString("address", (new StringBuilder("Address: ")).append(locationAddress).append("\n Unable to get Latitude and Longitude for this address location.").toString());
	                message2.setData(bundle4);
	                message2.sendToTarget();
                }catch(NullPointerException nullpointerexception){
                	Log.e("LocationAddress", "Unable to connect to Geocoder", ((Throwable) (nullpointerexception)));
                    value = false;
                    message1 = Message.obtain();
                    message1.setTarget(handler);
                    message1.what = 1;
                    bundle2 = new Bundle();
                    bundle2.putString("address", (new StringBuilder("Address: ")).append(locationAddress).append("\n Unable to get Latitude and Longitude for this address location.").toString());
                    message1.setData(bundle2);    
                    message1.sendToTarget();
                }catch(IOException ioexception){
                	Log.e("LocationAddress", "Unable to connect to Geocoder", ((Throwable) (ioexception)));
                    value = false;
                    message1 = Message.obtain();
                    message1.setTarget(handler);
                    message1.what = 1;
                    bundle2 = new Bundle();
                    bundle2.putString("address", (new StringBuilder("Address: ")).append(locationAddress).append("\n Unable to get Latitude and Longitude for this address location.").toString());
                    message1.setData(bundle2);    
                    message1.sendToTarget();
                }catch(Exception exception){
                	message = Message.obtain();
                    message.setTarget(handler);
                    message.what = 1;
                    bundle = new Bundle();
                    bundle.putString("address", (new StringBuilder("Address: ")).append(locationAddress).append("\n Unable to get Latitude and Longitude for this address location.").toString());
                    message.setData(bundle);
                    message.sendToTarget();
                    try{
                    	throw exception;
                    }catch(Exception e){}
                }
            }
        }).start();
    }

    public void getAddressFromLocation1(final double latitude, final double longitude, final Context context, final Handler handler)
    {
        (new Thread() {
            public void run()
            {
            	try{
	                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
	                List list = geocoder.getFromLocation(latitude, longitude, 1);
	                if(list != null)
	                {
	                	if(list.size() > 0)
	                    {
	                        Address address = (Address)list.get(0);
	                        saveString(USER_CITY, address.getLocality());
	                        saveString(USER_COUNTRY, address.getCountryName());
	                    }
	                }
	                Message message2 = Message.obtain();
	                message2.setTarget(handler);
	                message2.what = 1;
	                message2.sendToTarget();
            	}catch(IOException ioexception){
            		Log.e("LocationAddress", "Unable connect to Geocoder", ioexception);
                    Message message1 = Message.obtain();
                    message1.setTarget(handler);
                    message1.what = 1;
                    message1.sendToTarget();
            	}catch(Exception exception){
            		Message message = Message.obtain();
                    message.setTarget(handler);
                    message.what = 1;
                    message.sendToTarget();
            	}
            }
        }).start();
    }
}

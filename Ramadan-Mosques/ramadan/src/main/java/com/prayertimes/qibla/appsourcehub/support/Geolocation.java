package com.prayertimes.qibla.appsourcehub.support;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.*;
import android.util.Log;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geolocation extends Utils
{

    private static final String TAG = "GeocodingLocation";

    public Geolocation()
    {
    }

    public void getAddressFromLocation(final String locationAddress, final Context context, final Handler handler)
    {
        (new Thread() {
            public void run()
            {
            	Message message;
                Bundle bundle;
                String s3;
                Bundle bundle1;
                Message message1;
                Bundle bundle2;
                String s4;
                Bundle bundle3;
                Geocoder geocoder;
                String s;
                String s1;
                String s2;
                Object obj;
                geocoder = new Geocoder(context, Locale.getDefault());
                s = null;
                s1 = null;
                s2 = null;
                try{
	                List list = geocoder.getFromLocationName(locationAddress, 1);
	                s1 = null;
	                s2 = null;
	                s = null;
	                if(list != null)
	                {
		                int i = list.size();
		                s1 = null;
		                s2 = null;
		                s = null;
		                if(i > 0)
		                {
			                Address address = (Address)list.get(0);
			                StringBuilder stringbuilder = new StringBuilder();
			                stringbuilder.append(address.getLatitude()).append("\n");
			                StringBuilder stringbuilder1 = new StringBuilder();
			                stringbuilder1.append(address.getLongitude()).append("\n");
			                s = stringbuilder.toString();
			                s1 = stringbuilder.toString();
			                s2 = stringbuilder1.toString();
			                SavePref(USER_LAT, String.valueOf(address.getLatitude()));
			                SavePref(USER_LNG, String.valueOf(address.getLongitude()));
		                }
	                }
	                Message message2 = Message.obtain();
	                message2.setTarget(handler);
	                LogUtils.i("Unable to connect to Geocoder", s);
	                if(s != null)
	                {
	                    message2.what = 1;
	                    String s5 = (new StringBuilder(String.valueOf(s1))).append("/").append(s2).toString();
	                    Bundle bundle5 = new Bundle();
	                    bundle5.putString("address", s5);
	                    message2.setData(bundle5);
	                } else
	                {
	                    message2.what = 1;
	                    Bundle bundle4 = new Bundle();
	                    bundle4.putString("address", s);
	                    message2.setData(bundle4);
	                }
	                message2.sendToTarget();
                }catch(NullPointerException nullpointerexception){
                	Log.e("GeocodingLocation", "Unable to connect to Geocoder", ((Throwable) (nullpointerexception)));
                    message1 = Message.obtain();
                    message1.setTarget(handler);
                    LogUtils.i("Unable to connect to Geocoder", s);
                    if(s != null)
                    {
                        message1.what = 1;
                        s4 = (new StringBuilder(String.valueOf(s1))).append("/").append(s2).toString();
                        bundle3 = new Bundle();
                        bundle3.putString("address", s4);
                        message1.setData(bundle3);
                    } else
                    {
                        message1.what = 1;
                        bundle2 = new Bundle();
                        bundle2.putString("address", s);
                        message1.setData(bundle2);
                    }
                    message1.sendToTarget();
                }catch(IOException ioexception){
                	Log.e("GeocodingLocation", "Unable to connect to Geocoder", ((Throwable) (ioexception)));
                    message1 = Message.obtain();
                    message1.setTarget(handler);
                    LogUtils.i("Unable to connect to Geocoder", s);
                    if(s != null)
                    {
                        message1.what = 1;
                        s4 = (new StringBuilder(String.valueOf(s1))).append("/").append(s2).toString();
                        bundle3 = new Bundle();
                        bundle3.putString("address", s4);
                        message1.setData(bundle3);
                    } else
                    {
                        message1.what = 1;
                        bundle2 = new Bundle();
                        bundle2.putString("address", s);
                        message1.setData(bundle2);
                    }
                    message1.sendToTarget();
                }catch(Exception exception){
                	message = Message.obtain();
                    message.setTarget(handler);
                    LogUtils.i("Unable to connect to Geocoder", s);
                    if(s != null)
                    {
                        message.what = 1;
                        s3 = (new StringBuilder(String.valueOf(s1))).append("/").append(s2).toString();
                        bundle1 = new Bundle();
                        bundle1.putString("address", s3);
                        message.setData(bundle1);
                    } else
                    {
                        message.what = 1;
                        bundle = new Bundle();
                        bundle.putString("address", s);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                    try{
                    	throw exception;
                    }catch(Exception e){}
                }
            }
        }).start();
    }
}

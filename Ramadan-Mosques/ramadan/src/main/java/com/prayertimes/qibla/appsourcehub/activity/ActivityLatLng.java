package com.prayertimes.qibla.appsourcehub.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.util.*;
import muslim.prayers.time.R;
public class ActivityLatLng extends Utils
{

    Button btn_submit;
    String str_lat;
    String str_lng;
    EditText txt_lat;
    EditText txt_lng;

    public ActivityLatLng()
    {
    }

    public void Location()
    {
        Iterator iterator;
        try{
	        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
	        LogUtils.i((new StringBuilder(String.valueOf(str_lat))).append(" lat ").append(str_lng).append(" lng").toString());
	        iterator = geocoder.getFromLocation(Double.parseDouble(loadString(USER_LAT)), Double.parseDouble(loadString(USER_LNG)), 1).iterator();
	        while(true){
		        boolean flag = iterator.hasNext();
		        if(!flag)
		        	break;
		        Address address;
		        address = (Address)iterator.next();
		        LogUtils.i((new StringBuilder(String.valueOf(address.getLocality()))).append(" city ").append(address.getAdminArea()).append(" state ").append(address.getCountryName()).toString());
		        if(address != null){
			        String s = address.getLocality();
			        String s1 = address.getCountryName();
			        String s2 = address.getAdminArea();
			        String s3 = address.getSubLocality();
			        SavePref(USER_CITY, s);
			        SavePref(USER_STATE, s2);
			        SavePref(USER_COUNTRY, s1);
			        SavePref(USER_STREET, s3);
		        }
	        }
        }catch(Exception e){
        	SavePref(USER_CITY, "");
            SavePref(USER_STATE, "");
            SavePref(USER_STREET, "");
            SavePref(USER_COUNTRY, "");
        }
        finish();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_latlng);
        Actionbar("Enter Latitude & Longitude");
        Analytics(getString(R.string.settings));
        typeface();
        txt_lat = (EditText)findViewById(R.id.txt_lat);
        txt_lng = (EditText)findViewById(R.id.txt_lng);
        txt_lat.setText(LoadPref(USER_LAT));
        txt_lng.setText(LoadPref(USER_LNG));
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                str_lat = txt_lat.getText().toString();
                str_lng = txt_lng.getText().toString();
                if(!str_lat.equals("") || !str_lng.equals(""))
                {
                    saveString(USER_LAT, str_lat);
                    saveString(USER_LNG, str_lng);
                    saveString(USER_MLAT, str_lat);
                    saveString(USER_MLNG, str_lng);
                    Location();
                    return;
                } else
                {
                    Toast.makeText(ActivityLatLng.this, getString(R.string.toast_entervalidletIng), 0).show();
                    return;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        menu.findItem(R.id.menu_ok).setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        return super.onOptionsItemSelected(menuitem);
    }
}

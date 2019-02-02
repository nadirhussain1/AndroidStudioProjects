package com.prayertimes.qibla.appsourcehub.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.support.AppLocationService;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.IOException;
import java.util.*;
import muslim.prayers.time.R;
public class ActivityLocation extends Utils
{
    private class GeocoderHandler extends Handler
    {
        public void handleMessage(Message message)
        {
            message.getData();
            switch(message.what){
            default:
            	break;
            case 1:
            	loadString(USER_CITY);
                loadString(USER_COUNTRY);
            	break;
            case 2:
            	LogUtils.i("locationAddress ");
            	break;
            }
            dismissProgress();
            if(!loadString(USER_CITY).equals(""))
            {
                Intent intent = new Intent(ActivityLocation.this, com.prayertimes.qibla.appsourcehub.activity.ActivityMain.class);
                intent.putExtra("menu_show", true);
                startActivity(intent);
                finish();
                return;
            } else
            {
                Toast.makeText(ActivityLocation.this, getString(R.string.nolocation), 0).show();
                return;
            }
        }

        private GeocoderHandler()
        {
            super();
        }
    }


    private static final int LOCATION_INTENT_CALLED = 1;
    AppLocationService appLocationService;
    ProgressDialog showProgressDialog;

    public ActivityLocation()
    {
    }

    public void dismissProgress()
    {
        LogUtils.i(" on log", "  dismiss");
        showProgressDialog.dismiss();
    }

    public void getLocations()
    {
        Location location;
        double d;
        double d1;
        Iterator iterator;
        Intent intent;
        Address address;
        String s;
        String s1;
        String s2;
        String s3;
        try
        {
            location = appLocationService.getLocation();
            LogUtils.i((new StringBuilder("location ")).append(location).toString());
            if(location == null)
            {
            	showSettingsAlert();
                SavePref(USER_CITY, "");
                SavePref(USER_STATE, "");
                SavePref(USER_STREET, "");
                SavePref(USER_COUNTRY, "");
                return;
            }
            d = location.getLatitude();
            d1 = location.getLongitude();
            saveString(USER_LAT, String.valueOf(d));
            saveString(USER_LNG, String.valueOf(d1));
            iterator = (new Geocoder(this, Locale.getDefault())).getFromLocation(d, d1, 1).iterator();
            while(true) {
    	        if(!iterator.hasNext())
    	        {
    	            intent = new Intent(this, com.prayertimes.qibla.appsourcehub.activity.ActivityMain.class);
    	            intent.putExtra("menu_show", true);
    	            startActivity(intent);
    	            finish();
    	            return;
    	        }
    	        address = (Address)iterator.next();
    	        LogUtils.i((new StringBuilder(String.valueOf(address.getLocality()))).append(" city ").append(address.getAdminArea()).append(" state ").append(address.getCountryName()).toString());
    	        if(address != null){
    		        s = address.getLocality();
    		        s1 = address.getCountryName();
    		        s2 = address.getAdminArea();
    		        s3 = address.getSubLocality();
    		        SavePref(USER_CITY, s);
    		        SavePref(USER_STATE, s2);
    		        SavePref(USER_COUNTRY, s1);
    		        SavePref(USER_STREET, s3);
    	        }
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
            SavePref(USER_CITY, "");
            SavePref(USER_STATE, "");
            SavePref(USER_STREET, "");
            SavePref(USER_COUNTRY, "");
        }
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        LogUtils.i((new StringBuilder(" on activity result")).append(j).append(" ").append(intent).toString());
        if(j == 1)
        {
            LogUtils.i(" on log", "if  condition");
            getLocations();
            return;
        } else
        {
            getLocations();
            return;
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_location);
        Actionbar(getString(R.string.topbar_your_location));
        typeface();
        banner_ad();
        Analytics(getString(R.string.menu_about));
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        if(!loadString(USER_CITY).equals(""))
        {
            startActivity(new Intent(this, com.prayertimes.qibla.appsourcehub.activity.ActivityMain.class));
            finish();
        }
        appLocationService = new AppLocationService(this);
        ((TextView)findViewById(R.id.txt_location)).setTypeface(typeface);
     //   Button button = (Button)findViewById(R.id.automatic);
        Button button1 = (Button)findViewById(R.id.manual);
        button1.setTypeface(tf2);
       // button.setTypeface(tf2);
      /*  button.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(isOnline())
                {
                    Toast.makeText(ActivityLocation.this, "Please wait, Fetching Location", 0).show();
                    getLocations();
                    return;
                } else
                {
                    Toast.makeText(ActivityLocation.this, getString(R.string.nointernet), 0).show();
                    return;
                }
            }
        });*/
        button1.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = (new Intent(ActivityLocation.this, com.prayertimes.qibla.appsourcehub.activity.ActivitySearch.class)).putExtra("cat", "main");
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        menu.findItem(R.id.menu_ok).setVisible(false);
        return true;
    }

    protected void onDestroy()
    {
        appLocationService.stopUsingGPS();
        stopService(new Intent(this, com.prayertimes.qibla.appsourcehub.support.AppLocationService.class));
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        if(menuitem.getItemId() == 0x102002c)
        {
            finish();
        }
        return super.onOptionsItemSelected(menuitem);
    }

    public void showProgress()
    {
        showProgressDialog = new ProgressDialog(this);
        showProgressDialog.setTitle(R.string.progress_dialog);
        showProgressDialog.setMessage(getString(R.string.please_wait));
        showProgressDialog.show();
    }

    public void showSettingsAlert()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("SETTINGS");
        builder.setMessage("Enable Location Provider! Go to settings menu?");
        builder.setPositiveButton("Settings", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 1);
            }
        });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.cancel();
            }
        });
        builder.show();
    }
}

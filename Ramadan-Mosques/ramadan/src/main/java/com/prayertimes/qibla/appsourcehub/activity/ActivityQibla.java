package com.prayertimes.qibla.appsourcehub.activity;

import android.hardware.*;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.*;
import android.view.animation.RotateAnimation;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.text.DecimalFormat;
import muslim.prayers.time.R;
public class ActivityQibla extends Utils
    implements SensorEventListener, LocationListener
{

    TextView CityCountryName;
    TextView Country_Name;
    TextView Direction_Qibla;
    TextView Qibla_Distance;
    int angleFormated;
    int bearing;
    int compass[] = {
        R.drawable.compass, R.drawable.compass1, R.drawable.compass2, R.drawable.compass3, R.drawable.compass4, R.drawable.compass5, R.drawable.compass6, R.drawable.compass7, R.drawable.compass8, R.drawable.compass9, 
        R.drawable.compass10, R.drawable.compass11, R.drawable.compass12, R.drawable.compass13, R.drawable.compass14
    };
    private float currentDegree;
    int i;
    private ImageView image;
    private ImageView imageArrow;
    double lat1;
    double lat2;
    RelativeLayout layoutcompass;
    double lon1;
    double lon2;
    private SensorManager mSensorManager;
    int needle[] = {
        R.drawable.needle, R.drawable.needle1, R.drawable.needle2, R.drawable.needle3, R.drawable.needle4, R.drawable.needle5, R.drawable.needle6, R.drawable.needle7, R.drawable.needle8, R.drawable.needle9, 
        R.drawable.needle10, R.drawable.needle11, R.drawable.needle12, R.drawable.needle13, R.drawable.needle14
    };

    public ActivityQibla()
    {
        currentDegree = 0.0F;
        i = 0;
    }

    public static float distFrom(double d, double d1, double d2, double d3)
    {
        double d4 = Math.toRadians(d2 - d);
        double d5 = Math.toRadians(d3 - d1);
        double d6 = Math.sin(d4 / 2D) * Math.sin(d4 / 2D) + Math.cos(Math.toRadians(d)) * Math.cos(Math.toRadians(d2)) * Math.sin(d5 / 2D) * Math.sin(d5 / 2D);
        return (float)(6371000D * (2D * Math.atan2(Math.sqrt(d6), Math.sqrt(1.0D - d6)))) / 1000F;
    }

    public void magnetometer()
    {
        Location location = new Location("point A");
        location.setLatitude(lat1);
        location.setLongitude(lon1);
        Location location1 = new Location("point B");
        location1.setLatitude(lat2);
        location1.setLongitude(lon2);
        float f = location.distanceTo(location1);
        RotateAnimation rotateanimation = new RotateAnimation(currentDegree, f, 1, 0.5F, 1, 0.5F);
        rotateanimation.setFillAfter(true);
        currentDegree = f;
        imageArrow.startAnimation(rotateanimation);
        LogUtils.i((new StringBuilder("distance ")).append(f).toString());
    }

    public void onAccuracyChanged(Sensor sensor, int j)
    {
    }

    public void onCreate(Bundle bundle)
    {
       // fullscreen();
        super.onCreate(bundle);
        setContentView(R.layout.activity_compass);
        Actionbar(getString(R.string.lbl_qibla));
        Analytics(getString(R.string.lbl_qibla));
        typeface();
        banner_ad();
        image = (ImageView)findViewById(R.id.main_image_compass);
        imageArrow = (ImageView)findViewById(R.id.main_image_arrow);
        CityCountryName = (TextView)findViewById(R.id.CityCountryName);
        CityCountryName.setTypeface(tf2, 1);
        Direction_Qibla = (TextView)findViewById(R.id.Direction_Qibla);
        Qibla_Distance = (TextView)findViewById(R.id.Qibla_Distance);
        Direction_Qibla.setTypeface(tf2, 1);
        Qibla_Distance.setTypeface(tf2, 1);
        mSensorManager = (SensorManager)getSystemService("sensor");
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onLocationChanged(Location location)
    {
        LogUtils.i((new StringBuilder(String.valueOf(location.getLatitude()))).append(" onlocation ").append(location.getLongitude()).toString());
        super.onLocationChanged(location);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        menuitem.getItemId();
        return super.onOptionsItemSelected(menuitem);
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
        image.setBackgroundResource(compass[LoadPrefInt("user_compass")]);
        imageArrow.setBackgroundResource(needle[LoadPrefInt("user_compass")]);
        i = LoadPrefInt("compass_count");
        String s = loadString(USER_CITY);
        String s1 = loadString(USER_COUNTRY);
        String s2 = loadString(USER_STATE);
        CityCountryName.setText((new StringBuilder(String.valueOf(loadString(USER_STREET)))).append(" ").append(s).append(" ").append(s2).append(" ").append(s1).toString());
        lat2 = 21.422509999999999D;
        lon2 = 39.826168000000003D;
        int j;
        TextView textview;
        Object aobj[];
        int k;
        if(loadString(USER_LAT).equals("") && loadString(USER_LNG).equals(""))
        {
            lat1 = 0.0D;
            lon1 = 0.0D;
        } else
        {
            lat1 = Double.parseDouble(loadString(USER_LAT));
            lon1 = Double.parseDouble(loadString(USER_LNG));
        }
        LogUtils.i((new StringBuilder(String.valueOf(loadString(USER_LNG)))).append(" lat ").append(loadString(USER_LAT)).toString());
        if(mSensorManager.getDefaultSensor(2) == null)
        {
            if(loadString(USER_LAT).equals("") && loadString(USER_LNG).equals("") || loadString(USER_LAT).equals("0.0") && loadString(USER_LNG).equals("0.0"))
            {
                Toast.makeText(this, getString(R.string.toast_cantgetlatIng), 0).show();
            } else
            {
                magnetometer();
            }
        }
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(3), 1);
        j = (int)bearing(lat1, lon1, lat2, lon2);
        new DecimalFormat("#");
        angleFormated = j;
        LogUtils.i((new StringBuilder(String.valueOf(j))).append(" bearing ").append(angleFormated).toString());
        textview = Direction_Qibla;
        aobj = new Object[1];
        aobj[0] = String.valueOf(angleFormated);
        textview.setText(getString(R.string.degree, aobj));
        k = (int)distFrom(lat1, lon1, lat2, lon2);
        LogUtils.i((new StringBuilder(" n ")).append(k).toString());
        Qibla_Distance.setText((new StringBuilder(String.valueOf(getString(R.string.distance)))).append(" ").append(k).append(" KM").toString());
    }

    public void onSensorChanged(SensorEvent sensorevent)
    {
        float f = Math.round(sensorevent.values[0]);
        LogUtils.i((new StringBuilder(String.valueOf((float)angleFormated - f))).append(" degree ").append(f).toString());
        imageArrow.setRotation((float)angleFormated - f);
        RotateAnimation rotateanimation;
        try
        {
            int j = (int)((float)angleFormated - f);
            TextView textview = Direction_Qibla;
            Object aobj[] = new Object[1];
            aobj[0] = String.valueOf(j);
            textview.setText(getString(R.string.degree, aobj));
        }
        catch(Exception exception) { }
        rotateanimation = new RotateAnimation(currentDegree, -f, 1, 0.5F, 1, 0.5F);
        rotateanimation.setDuration(50L);
        rotateanimation.setFillAfter(true);
        image.startAnimation(rotateanimation);
        currentDegree = -f;
    }

    public void onStop()
    {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }
}

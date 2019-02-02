package com.prayertimes.qibla.appsourcehub.activity;

import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.receiver.AlarmReceiver;
import com.prayertimes.qibla.appsourcehub.support.*;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.text.*;
import java.util.*;
import muslim.prayers.time.R;
public class FragmentHome extends Utils
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
                txt_location.setText((new StringBuilder(String.valueOf(LoadPref(USER_CITY)))).append(" ").append(LoadPref(USER_STATE)).append(" ").append(LoadPref(USER_COUNTRY)).toString());
            	break;
            case 2:
            	LogUtils.i("locationAddress ");
            	break;
            }
            checkDateTime();
            dismissProgress();
        }

        private GeocoderHandler()
        {
            super();
        }

        GeocoderHandler(GeocoderHandler geocoderhandler)
        {
            this();
        }
    }


    private static final int LOCATION_INTENT_CALLED = 1;
    private static final String TAG_RINGTONE = "ringtone";
    ImageView alarm;
    Intent alarmIntent;
    AppLocationService appLocationService;
    boolean bool_alarm_fajar;
    boolean bool_alarm_maghrib;
    String city;
    boolean cur_date_display;
    android.content.SharedPreferences.Editor editor;
    int id;
    ImageView img_slider_next;
    ImageView img_slider_previous;
    FrameLayout lyt_fasting_end_time;
    FrameLayout lyt_fasting_mid_time;
    FrameLayout lyt_fasting_start_time;
    boolean next_date_display;
    String p_name[];
    private PendingIntent pendingIntent;
    SharedPreferences pref;
    boolean prev_date_display;
    ProgressDialog showProgressDialog;
    SwipeRefreshLayout swipehc_container;
    int time;
    int time_back;
    TextView title_end;
    TextView title_end_time;
    TextView title_mid;
    TextView title_mid_time;
    TextView title_start;
    TextView title_start_time;
    TextView txt_date;
    TextView txt_location;
    String url;

    public FragmentHome()
    {
        cur_date_display = true;
        next_date_display = false;
        prev_date_display = false;
        time = 0;
        id = 0;
    }

    public void PrayerTimeTomorrow(int i)
        throws ParseException
    {
        ArrayList arraylist;
        String as[];
        int j;
        double d = Double.parseDouble(loadString(USER_LAT));
        double d1 = Double.parseDouble(loadString(USER_LNG));
        String s = pref.getString("timezone", "");
        PrayTime praytime = new PrayTime();
        praytime.setTimeFormat(praytime.Time12);
        praytime.setCalcMethod(Integer.parseInt(pref.getString("method", "1")));
        praytime.setAsrJuristic(Integer.parseInt(pref.getString("juristic", "1")));
        praytime.setAdjustHighLats(Integer.parseInt(pref.getString("higherLatitudes", "1")));
        praytime.tune(new int[7]);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, i);
        if(s.equals(""))
        {
            getTimeZone(calendar.getTimeZone().getID().toString());
            editor = pref.edit();
            editor.putString("timezone", s);
            editor.commit();
        } else
        {
            getTimeZone(s);
        }
        arraylist = praytime.getPrayerTimes(calendar, d, d1, getTimeZone(s));
        praytime.getTimeNames();
        if(d == 0.0D || d1 == 0.0D)
        	return;
        as = getResources().getStringArray(R.array.prayer_array);
        j = 0;
        while(j < arraylist.size()){
        	if(id == 0)
            {
    	        set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(LoadStringPref((new StringBuilder(String.valueOf(as[0]))).append("daylight").toString()))));
    	        set_alarm_tomoro(addDayLight((String)arraylist.get(0), Integer.parseInt(LoadStringPref((new StringBuilder(String.valueOf(as[1]))).append("daylight").toString()))));
            }else if(id == 3)
            {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), -66 + Integer.parseInt(LoadStringPref((new StringBuilder(String.valueOf(as[2]))).append("daylight").toString()))));
            } else
            if(id == 4)
            {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(LoadStringPref((new StringBuilder(String.valueOf(as[3]))).append("daylight").toString()))));
            } else
            if(id == 5)
            {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(LoadStringPref((new StringBuilder(String.valueOf(as[4]))).append("daylight").toString()))));
            } else
            if(id == 6)
            {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(LoadStringPref((new StringBuilder(String.valueOf(as[5]))).append("daylight").toString()))));
            }
            j++;
        }
    }

    public void cancel_alarm(int i)
    {
        pendingIntent = PendingIntent.getBroadcast(this, i, alarmIntent, 0);
        ((AlarmManager)getSystemService("alarm")).cancel(pendingIntent);
        Toast.makeText(this, getString(R.string.toast_alarmcanceled), 0).show();
    }

    public void changeSliderView()
    {
        if(time == 0)
        {
            img_slider_previous.setVisibility(4);
        } else
        {
            img_slider_previous.setVisibility(0);
        }
        if(time == 30)
        {
            img_slider_next.setVisibility(4);
            return;
        } else
        {
            img_slider_next.setVisibility(0);
            return;
        }
    }

    public void checkDateTime()
    {
        try
        {
            initPrayerTime(time);
            txt_date.setText(getTimeNow(time));
            return;
        }
        catch(ParseException parseexception)
        {
            parseexception.printStackTrace();
        }
    }

    public void dismissProgress()
    {
        LogUtils.i(" on log", "  dismiss");
        showProgressDialog.dismiss();
    }

    public void getLocations()
    {
        Location location = appLocationService.getLocation();
        LogUtils.i((new StringBuilder("location ")).append(location).toString());
        if(location != null)
        {
            double d = location.getLatitude();
            double d1 = location.getLongitude();
            LogUtils.i((new StringBuilder("latitude3")).append(d).append("langitude3").append(d1).toString());
            saveString(USER_LAT, String.valueOf(d));
            saveString(USER_LNG, String.valueOf(d1));
            (new LocationAddress()).getAddressFromLocation1(d, d1, this, new GeocoderHandler(null));
            return;
        } else
        {
            showSettingsAlert();
            return;
        }
    }

    public void initAlarm()
    {
        bool_alarm_fajar = loadBoolean("alarm_fajar");
        bool_alarm_maghrib = loadBoolean("alarm_maghrib");
        if(bool_alarm_fajar && bool_alarm_maghrib)
        {
            alarm.setImageResource(R.drawable.alarm_icon_clicked);
            return;
        } else
        {
            alarm.setImageResource(R.drawable.alarm_icon);
            return;
        }
    }

    public void initPrayerTime(int i)
        throws ParseException
    {
        double d = Double.parseDouble(loadString(USER_LAT));
        double d1 = Double.parseDouble(loadString(USER_LNG));
        String s = pref.getString("timezone", "");
        PrayTime praytime = new PrayTime();
        praytime.setTimeFormat(praytime.Time12);
        praytime.setCalcMethod(Integer.parseInt(pref.getString("method", "1")));
        praytime.setAsrJuristic(Integer.parseInt(pref.getString("juristic", "1")));
        praytime.setAdjustHighLats(Integer.parseInt(pref.getString("higherLatitudes", "1")));
        praytime.tune(new int[7]);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, i);
        double d2;
        ArrayList arraylist;
        ArrayList arraylist1;
        if(s.equals(""))
        {
            d2 = getTimeZone(calendar.getTimeZone().getID().toString());
            String s1 = calendar.getTimeZone().getID().toString();
            editor = pref.edit();
            editor.putString("timezone", s1);
            editor.commit();
            LogUtils.i("data", (new StringBuilder(String.valueOf(d2))).append(" TimeZoneName ").append(s1).toString());
        } else
        {
            d2 = getTimeZone(s);
        }
        LogUtils.i("data", (new StringBuilder(" ")).append(d2).append(" data ").append(calendar.getTimeZone().getID().toString()).toString());
        LogUtils.i("data", (new StringBuilder()).append(calendar).append(" data ").append(Integer.parseInt(pref.getString("daylight", "0"))).toString());
        arraylist = praytime.getPrayerTimes(calendar, d, d1, d2);
        arraylist1 = praytime.getTimeNames();
        if(pref_time.equals("0"))
        {
            title_mid_time.setText(addDayLight(arraylist.get(0).toString(), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0"))));
            title_start_time.setText(addDayLight(arraylist.get(0).toString(), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0"))));
            title_end_time.setText(addDayLight(arraylist.get(4).toString(), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0"))));
        } else
        {
            title_mid_time.setText(addDayLight(main(arraylist.get(0).toString()), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0"))));
            title_start_time.setText(addDayLight(main(arraylist.get(5).toString()), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0"))));
            title_end_time.setText(addDayLight(main(arraylist.get(0).toString()), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0"))));
        }
        LogUtils.i((new StringBuilder(String.valueOf(arraylist1.size()))).append(" time ").append(arraylist.size()).toString());
    }

    public String main(String s)
    {
        SimpleDateFormat simpledateformat;
        SimpleDateFormat simpledateformat1;
        simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
        simpledateformat1 = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date;
        try{
	        Date date1 = simpledateformat1.parse(s);
	        date = date1;
        }catch(Exception e){
        	date = null;
        }
        return simpledateformat.format(date);
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

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_home);
        p_name = getResources().getStringArray(R.array.prayer_array);
        Actionbar(getString(R.string.menu_ramdantime));
        typeface();
        banner_ad();
        Analytics(getString(R.string.menu_ramdantime));
        appLocationService = new AppLocationService(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        city = LoadPref(USER_CITY);
        alarmIntent = new Intent(this, com.prayertimes.qibla.appsourcehub.receiver.AlarmReceiver.class);
        lyt_fasting_start_time = (FrameLayout)findViewById(R.id.lyt_fasting_start_time);
        lyt_fasting_end_time = (FrameLayout)findViewById(R.id.lyt_fasting_end_time);
        lyt_fasting_mid_time = (FrameLayout)findViewById(R.id.lyt_fasting_mid_time);
        img_slider_next = (ImageView)findViewById(R.id.img_slider_next);
        img_slider_previous = (ImageView)findViewById(R.id.img_slider_previous);
        txt_location = (TextView)findViewById(R.id.txt_location);
        txt_date = (TextView)findViewById(R.id.txt_date);
        title_start_time = (TextView)findViewById(R.id.title_start_time);
        title_end_time = (TextView)findViewById(R.id.title_end_time);
        title_mid_time = (TextView)findViewById(R.id.title_mid_time);
        title_start = (TextView)findViewById(R.id.title_start);
        title_end = (TextView)findViewById(R.id.title_end);
        title_mid = (TextView)findViewById(R.id.title_mid);
        txt_date.setTypeface(tf2, 1);
        txt_location.setTypeface(tf2, 1);
        title_start_time.setTypeface(tf2, 1);
        title_end_time.setTypeface(tf2, 1);
        title_mid_time.setTypeface(tf2, 1);
        title_start.setTypeface(tf2, 1);
        title_end.setTypeface(tf2, 1);
        title_mid.setTypeface(tf2, 1);
        LoadPref(USER_CITY);
        LoadPref(USER_COUNTRY);
        txt_location.setText((new StringBuilder(String.valueOf(LoadPref(USER_CITY)))).append(" ").append(LoadPref(USER_STATE)).append(" ").append(LoadPref(USER_COUNTRY)).toString());
        String s = LoadPref(USER_LAT);
        String s1 = LoadPref(USER_LNG);
        if(s1.equals(""))
        {
            s1 = "0.0";
        }
        double d = Double.parseDouble(s1);
        if(s.equals(""))
        {
            s = "0.0";
        }
        double d1 = Double.parseDouble(s);
        LogUtils.i((new StringBuilder("latitude")).append(d).append("langitude").append(d1).toString());
        LogUtils.i((new StringBuilder("latitude2")).append(d).append("langitude2").append(d1).toString());
        checkDateTime();
        img_slider_next.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                android.view.animation.Animation animation = AnimationUtils.loadAnimation(FragmentHome.this, R.anim.push_left_out);
                lyt_fasting_start_time.startAnimation(animation);
                lyt_fasting_end_time.startAnimation(animation);
                lyt_fasting_mid_time.startAnimation(animation);
                txt_date.startAnimation(animation);
                Runnable runnable = new Runnable() {
                    public void run()
                    {
                        android.view.animation.Animation animation;
                        try
                        {
                            FragmentHome fragmenthome = FragmentHome.this;
                            fragmenthome.time = 1 + fragmenthome.time;
                            txt_date.setText(getTimeNow(time));
                            initPrayerTime(time);
                        }
                        catch(ParseException parseexception)
                        {
                            parseexception.printStackTrace();
                        }
                        animation = AnimationUtils.loadAnimation(FragmentHome.this, R.anim.push_left_in);
                        lyt_fasting_start_time.startAnimation(animation);
                        lyt_fasting_end_time.startAnimation(animation);
                        lyt_fasting_mid_time.startAnimation(animation);
                        txt_date.startAnimation(animation);
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
        img_slider_previous.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                android.view.animation.Animation animation = AnimationUtils.loadAnimation(FragmentHome.this, R.anim.push_right_out);
                lyt_fasting_start_time.startAnimation(animation);
                lyt_fasting_end_time.startAnimation(animation);
                lyt_fasting_mid_time.startAnimation(animation);
                txt_date.startAnimation(animation);
                Runnable runnable = new Runnable() {
                    public void run()
                    {
                        android.view.animation.Animation animation;
                        try
                        {
                            FragmentHome fragmenthome = FragmentHome.this;
                            fragmenthome.time = -1 + fragmenthome.time;
                            txt_date.setText(getTimeNow(time));
                            initPrayerTime(time);
                        }
                        catch(ParseException parseexception)
                        {
                            parseexception.printStackTrace();
                        }
                        animation = AnimationUtils.loadAnimation(FragmentHome.this, R.anim.push_right_in);
                        lyt_fasting_start_time.startAnimation(animation);
                        lyt_fasting_end_time.startAnimation(animation);
                        lyt_fasting_mid_time.startAnimation(animation);
                        txt_date.startAnimation(animation);
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
        alarm = (ImageView)findViewById(R.id.alarm);
        initAlarm();
        alarm.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                id = 0;
                if(bool_alarm_fajar)
                {
                    saveBoolean("alarm_fajar", false);
                    cancel_alarm(id);
                } else
                {
                    alarmIntent.putExtra("type", getString(R.string.fajar));
                    alarmIntent.putExtra("time", title_start_time.getText());
                    set_alarm(title_start_time.getText().toString());
                    saveBoolean("alarm_fajar", true);
                }
                id = 4;
                if(bool_alarm_maghrib)
                {
                    saveBoolean("alarm_maghrib", false);
                    cancel_alarm(id);
                } else
                {
                    alarmIntent.putExtra("type", getString(R.string.maghrib));
                    alarmIntent.putExtra("time", title_end_time.getText());
                    set_alarm(title_end_time.getText().toString());
                    saveBoolean("alarm_maghrib", true);
                }
                initAlarm();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        menu.findItem(R.id.menu_ok).setVisible(false);
        return true;
    }

    public void onDestroy()
    {
        LogUtils.i("", "destroy fragment home");
        appLocationService.stopUsingGPS();
        stopService(new Intent(this, com.prayertimes.qibla.appsourcehub.activity.FragmentHome.class));
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

    protected void onResume()
    {
        font();
        txt_location.setTypeface(tf2, 1);
        try
        {
            initPrayerTime(time);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(isOnline())
        {
            findViewById(R.id.ad_layout).setVisibility(0);
        } else
        {
            findViewById(R.id.ad_layout).setVisibility(8);
        }
        super.onResume();
    }

    public void set_alarm(String s)
    {
        SimpleDateFormat simpledateformat;
        String s1;
        int i;
        simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        s1 = simpledateformat.format(new Date());
        i = 0;
        int k;
        int j;
        try{
	        String s2 = changeTimeFormat(s);
	        i = Integer.parseInt(s2.substring(0, 2));
	        k = Integer.parseInt(s2.substring(3, 5));
	        j = k;
        }catch(Exception e){
        	j = 0;
        }
        Calendar calendar;
        calendar = Calendar.getInstance();
        ParseException parseexception;
        Date date;
        Calendar calendar1;
        try
        {
            calendar.setTime(simpledateformat.parse(s1));
        }
        catch(ParseException parseexception1)
        {
            parseexception1.printStackTrace();
        }
        calendar.set(11, i);
        calendar.set(12, j + Integer.parseInt(pref.getString("adjust_alarm", "-5")));
        date = new Date();
        calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        LogUtils.i("", (new StringBuilder("time ")).append(calendar).toString());
        if(!calendar1.after(calendar))
        {
        	pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
            ((AlarmManager)getSystemService("alarm")).set(0, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, getString(R.string.toast_alarmenabled), 0).show();
            return;
        }
        try
        {
            PrayerTimeTomorrow(1);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void set_alarm_tomoro(String s)
    {
        SimpleDateFormat simpledateformat;
        String s1;
        int i;
        simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        s1 = simpledateformat.format(new Date());
        i = 0;
        int k;
        int j;
        try{
	        String s2 = changeTimeFormat(s);
	        i = Integer.parseInt(s2.substring(0, 2));
	        k = Integer.parseInt(s2.substring(3, 5));
	        j = k;
        }catch(Exception e){
        	j = 0;
        }
        Calendar calendar = Calendar.getInstance();
        ParseException parseexception;
        try
        {
            calendar.setTime(simpledateformat.parse(s1));
        }
        catch(ParseException parseexception1)
        {
            parseexception1.printStackTrace();
        }
        calendar.set(11, i);
        calendar.set(12, j + Integer.parseInt(pref.getString("adjust_alarm", "-5")));
        calendar.add(5, 1);
        LogUtils.i("", (new StringBuilder("tomoro ")).append(calendar).append(" ").toString());
        pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
        ((AlarmManager)getSystemService("alarm")).set(0, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, getString(R.string.toast_alarmenabled), 0).show();
    }

    public void showProgress()
    {
        showProgressDialog = new ProgressDialog(this);
        showProgressDialog.setTitle(R.string.progress_dialog);
        showProgressDialog.setMessage(getString(R.string.please_wait));
        showProgressDialog.show();
    }

    public void showSettingsAlert1()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("SETTINS");
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

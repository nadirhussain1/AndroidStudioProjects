package com.prayertimes.qibla.appsourcehub.activity;

import android.app.*;
import android.content.*;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import com.prayertimes.qibla.appsourcehub.receiver.AlarmReceiver;
import com.prayertimes.qibla.appsourcehub.support.PrayTime;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import java.io.IOException;
import java.io.PrintStream;
import java.text.*;
import java.util.*;
import muslim.prayers.time.R;
public class ActivityAlarm extends Utils
{

    Intent alarmIntent;
    android.content.SharedPreferences.Editor editor;
    int id;
    private MediaPlayer mMediaPlayer;
    private PendingIntent pendingIntent;
    SharedPreferences pref;
    ImageView stopAlarm;
    TextView txt_date;
    TextView txt_location;
    TextView txt_name;
    TextView txt_type;
    String type_nawaz;

    public ActivityAlarm()
    {
        id = 0;
    }

    private static void generateNotification(Context context, String s, String s1)
    {
        NotificationManager notificationmanager = (NotificationManager)context.getSystemService("notification");
        String s2 = context.getString(R.string.app_name);
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, new Intent(context, com.prayertimes.qibla.appsourcehub.activity.ActivityAlarm.class), 0x10000000);
        notificationmanager.notify(0, (new android.support.v4.app.NotificationCompat.Builder(context)).setSmallIcon(R.drawable.ic_launcher).setAutoCancel(false).setContentTitle(s2).setContentText((new StringBuilder(String.valueOf(s))).append(",").append(s1).toString()).setContentIntent(pendingintent).build());
    }

    private Uri getPrefAlarmUri()
    {
        return Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString("ringtone", "content://settings/system/alarm_alert"));
    }

    private void playSound(Context context)
    {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.azaan);
        if(((AudioManager)context.getSystemService("audio")).getStreamVolume(3) != 0)
        {
            mMediaPlayer.setAudioStreamType(3);
            mMediaPlayer.start();
        }
    }

    private void playSoundAlarm(Context context, Uri uri)
    {
        mMediaPlayer = new MediaPlayer();
        try
        {
            mMediaPlayer.setDataSource(context, uri);
            if(((AudioManager)context.getSystemService("audio")).getStreamVolume(4) != 0)
            {
                mMediaPlayer.setAudioStreamType(4);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
            return;
        }
        catch(IOException ioexception)
        {
            System.out.println("OOPS");
        }
    }

    public void initPrayerTime(int i)
        throws ParseException
    {
        ArrayList arraylist;
        ArrayList arraylist1;
        int j;
        int k;
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
        if(s.equals(""))
        {
            d2 = getTimeZone(calendar.getTimeZone().getID().toString());
            editor = pref.edit();
            editor.putString("timezone", s);
            editor.commit();
        } else
        {
            d2 = getTimeZone(s);
        }
        arraylist = praytime.getPrayerTimes(calendar, d, d1, d2);
        arraylist1 = praytime.getTimeNames();
        if(d == 0.0D || d1 == 0.0D)
        	return;
        j = Integer.parseInt(pref.getString("daylight", "0"));
        k = 0;
        while(k < arraylist.size()){
        	if(((String)arraylist1.get(k)).equals("Fajr"))
            {
    	        id = 0;
    	        if(type_nawaz.equals("Fajar"))
    	        {
    	            set_alarm(addDayLight((String)arraylist.get(k), j));
    	        }
            }else if(((String)arraylist1.get(k)).equals("Sunrise")){
                id = 1;
                if(type_nawaz.equals("Shorook"))
                {
                    set_alarm(addDayLight((String)arraylist.get(k), j));
                }
            } else if(((String)arraylist1.get(k)).equals("Dhuhr")) {
                id = 2;
                if(type_nawaz.equals("Zuher"))
                {
                    set_alarm(addDayLight((String)arraylist.get(k), j));
                }
            } else if(((String)arraylist1.get(k)).equals("Asr")) {
                id = 3;
                if(type_nawaz.equals("Asar"))
                {
                    set_alarm(addDayLight((String)arraylist.get(k), j));
                }
            } else if(((String)arraylist1.get(k)).equals("Maghrib")) {
                id = 4;
                if(type_nawaz.equals("Maghrib"))
                {
                    set_alarm(addDayLight((String)arraylist.get(k), j));
                }
            } else if(((String)arraylist1.get(k)).equals("Isha")) {
                id = 5;
                if(type_nawaz.equals("Isha"))
                {
                    set_alarm(addDayLight((String)arraylist.get(k), j));
                }
            }
            k++;
        }
    }

    public void onBackPressed()
    {
        mMediaPlayer.stop();
        finish();
        super.onBackPressed();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_alarm);
        
        String s;
        String s1;
        String s2;
        Actionbar(getString(R.string.app_name));
        Analytics(getString(R.string.app_name));
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Bundle bundle1 = getIntent().getExtras();
    	typeface();
        txt_date = (TextView)findViewById(R.id.txt_date);
        txt_date.setTypeface(tf1);
        try {
			txt_date.setText(getTimeDate(0));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        alarmIntent = new Intent(this, com.prayertimes.qibla.appsourcehub.receiver.AlarmReceiver.class);
        txt_type = (TextView)findViewById(R.id.txt_type);
        txt_type.setTypeface(tf1);
        txt_location = (TextView)findViewById(R.id.txt_location);
        txt_location.setTypeface(tf1);
        s = loadString(USER_CITY);
        s1 = loadString(USER_COUNTRY);
        txt_location.setText(getString(R.string.lblcitycountry, new Object[] {
            s, s1
        }));
        if(bundle1 != null)
        {
            type_nawaz = bundle1.getString("type");
            String s3 = bundle1.getString("time");
            if(s3 != null && type_nawaz != null)
            {
                txt_type.setText((new StringBuilder(String.valueOf(s3))).append(", ").append(type_nawaz).toString());
                SavePref(type_nawaz, "fals");
                generateNotification(this, s3, type_nawaz);
            }
        }
        stopAlarm = (ImageView)findViewById(R.id.stopAlarm);
        stopAlarm.setOnTouchListener(new android.view.View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mMediaPlayer.stop();
                finish();
                return false;
            }
        });
        s2 = pref.getString("ringtone", "content://settings/system/alarm_alert");
        LogUtils.i((new StringBuilder("alarms")).append(s2).toString());
        if(s2.equals("content://settings/system/alarm_alert"))
            playSound(this);
        else
            playSoundAlarm(this, getPrefAlarmUri());
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        menu.findItem(R.id.menu_ok).setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        if(menuitem.getItemId() == 0x102002c)
        {
            mMediaPlayer.stop();
            finish();
        }
        return super.onOptionsItemSelected(menuitem);
    }

    public void set_alarm(String s)
    {
        SimpleDateFormat simpledateformat;
        String s1;
        int k;
        int j;
        int i;
        simpledateformat = new SimpleDateFormat("MMMM d, yyyy");
    	s1 = simpledateformat.format(new Date());
        i = 0;
    	String s2;
		try {
			s2 = changeTimeFormat(s);
			i = Integer.parseInt(s2.substring(0, 2));
	        k = Integer.parseInt(s2.substring(3, 5));
	        j = k;
		} catch (ParseException e) {
			j = 0;
			e.printStackTrace();
		}
        
        pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
        AlarmManager alarmmanager = (AlarmManager)getSystemService("alarm");
        Calendar calendar = Calendar.getInstance();
        try
        {
            calendar.setTime(simpledateformat.parse(s1));
        }
        catch(ParseException parseexception1)
        {
            parseexception1.printStackTrace();
        }
        calendar.add(5, 1);
        calendar.set(11, i);
        calendar.set(12, j + Integer.parseInt(pref.getString("adjust_alarm", "-5")));
        alarmmanager.set(0, calendar.getTimeInMillis(), pendingIntent);
        LogUtils.i("", (new StringBuilder("time ")).append(calendar).toString());
    }

}

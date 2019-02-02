package com.prayertimes.qibla.appsourcehub.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.prayertimes.qibla.appsourcehub.receiver.AlarmReceiver;
import com.prayertimes.qibla.appsourcehub.support.AppLocationService;
import com.prayertimes.qibla.appsourcehub.support.PrayTime;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import com.sahaab.hijricalendar.HijriCalendarDate;
import java.io.PrintStream;
import java.text.*;
import java.util.*;
import muslim.prayers.time.R;
public class PrayerTimesActivity extends Utils
    implements android.view.View.OnClickListener
{
    public class AdapterPrayer extends BaseAdapter
    {

        boolean alarm;
        ImageView btn_alarm;
        LayoutInflater inflater;
        TextView lbl_time;
        LinearLayout lyt_listview;
        Context mContext;
        TextView name;

        public int getCount()
        {
            return p_name.length;
        }

        public Object getItem(int i)
        {
            return Integer.valueOf(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(final int position, View view, ViewGroup viewgroup)
        {
            View view1 = inflater.inflate(R.layout.prayer_list, null);
            lyt_listview = (LinearLayout)view1.findViewById(R.id.lyt_listview);
            name = (TextView)view1.findViewById(R.id.name);
            lbl_time = (TextView)view1.findViewById(R.id.time);
            btn_alarm = (ImageView)view1.findViewById(R.id.btn_alarm);
            lbl_time.setTypeface(PrayerTimesActivity.this.tf2, 1);
            name.setTypeface(PrayerTimesActivity.this.tf2, 1);
            name.setText(p_name[position]);
            lbl_time.setText(p_time[position]);
            Date date;
            final Calendar calendar;
            if(today == 0)
            {
                btn_alarm.setVisibility(0);
            } else
            {
                btn_alarm.setVisibility(4);
            }
            date = new Date();
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            if(LoadPref(p_name[position]).equals("tru"))
            {
                if(calendar.after(p_time[position]))
                {
                    SavePref(p_name[position], "fals");
                    setnormalimage(position);
                } else
                {
                    SavePref(p_name[position], "tru");
                    setimage(position);
                }
            } else
            {
                SavePref(p_name[position], "fals");
                setnormalimage(position);
            }
            lyt_listview.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view)
                {
                }
            });
            btn_alarm.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view)
                {
                    LogUtils.i((new StringBuilder(String.valueOf(LoadPref(p_name[position])))).append(" click ").toString());
                    if(calendar.after(p_time[position]))
                    {
                        Toast.makeText(PrayerTimesActivity.this, "Prayer has been passed", 0).show();
                        return;
                    }
                    if(LoadPref(p_name[position]).equals("tru"))
                    {
                        alarm = false;
                    } else
                    {
                        alarm = true;
                    }
                    id = position;
                    setalarm(p_name[position].trim().toString(), p_time[position].trim().toString(), alarm);
                }
            });
            return view1;
        }

        public void setimage(int i)
        {
            if(i == 0)
            {
                btn_alarm.setImageResource(R.drawable.fajar_icon);
            } else
            {
                if(i == 1)
                {
                    btn_alarm.setImageResource(R.drawable.zuher_icon);
                    return;
                }
                if(i == 2)
                {
                    btn_alarm.setImageResource(R.drawable.asar_icon);
                    return;
                }
                if(i == 3)
                {
                    btn_alarm.setImageResource(R.drawable.maghrib_icon);
                    return;
                }
                if(i == 4)
                {
                    btn_alarm.setImageResource(R.drawable.isha_icon);
                    return;
                }
                if(i == 5)
                {
                    btn_alarm.setImageResource(R.drawable.shorook_icon);
                    return;
                }
            }
        }

        public void setnormalimage(int i)
        {
            if(i == 0)
            {
                btn_alarm.setImageResource(R.drawable.fajar_icon_disabled);
            } else
            {
                if(i == 1)
                {
                    btn_alarm.setImageResource(R.drawable.zuher_icon_disabled);
                    return;
                }
                if(i == 2)
                {
                    btn_alarm.setImageResource(R.drawable.asar_icon_disabled);
                    return;
                }
                if(i == 3)
                {
                    btn_alarm.setImageResource(R.drawable.maghrib_icon_disabled);
                    return;
                }
                if(i == 4)
                {
                    btn_alarm.setImageResource(R.drawable.isha_icon_disabled);
                    return;
                }
                if(i == 5)
                {
                    btn_alarm.setImageResource(R.drawable.shorook_icon_disabled);
                    return;
                }
            }
        }


        public AdapterPrayer(Context context)
        {
            super();
            alarm = false;
            mContext = context;
            inflater = (LayoutInflater)mContext.getSystemService("layout_inflater");
        }
    }


    Calendar Qurancal;
    AdRequest adRequest;
    AdapterPrayer adapter;
    Intent alarmIntent;
    int alarm_pos;
    String alarm_time;
    public final ArrayList alarm_value = new ArrayList();
    AnalogClock analog;
    AppLocationService appLocationService;
    boolean bool_alarm_Asr;
    boolean bool_alarm_Sunrise;
    boolean bool_alarm_fajar;
    boolean bool_alarm_isha;
    boolean bool_alarm_maghrib;
    boolean bool_alarm_zuheer;
    Calendar cal;
    String cur_time;
    String d_time;
    android.content.SharedPreferences.Editor editor;
    int id;
    ImageView img_slider_next;
    ImageView img_slider_previous;
    int interflagnext;
    int interflagprevious;

    double latitude;
    RelativeLayout layoutprayer;
    double longitude;
    GridView lv_gridview;
    ArrayList n_array;
    String next_prayer[];
    DecimalFormat numberFormat;
    String nxt_time;
    String p_name[];
    String p_next_prayer[];
    String p_time[];
    String p_time_1[];
    private PendingIntent pendingIntent;
    SharedPreferences pref;
    SharedPreferences sp;
    EditText text;
    int time;
    int time_back;
    double timezone;
    int today;
    Toolbar toolbar;
    TextView txt_date;
    TextView txt_place;
    TextView txt_prayer_time;
    TextView txt_street;
    TextView txt_time;
    TextView txt_title;
    ArrayList values;
    String weekDay;

    public PrayerTimesActivity()
    {
        time = 0;
        today = 0;
        alarm_time = "fals";
        latitude = 0.0D;
        longitude = 0.0D;
        id = 0;
        cur_time = "";
        nxt_time = "";
        n_array = new ArrayList();
        values = new ArrayList();
        alarm_pos = 0;
        interflagprevious = 1;
        interflagnext = 1;
    }

    private void showCustomView(final String name, final String n_time)
    {
        MaterialDialog materialdialog = (new com.afollestad.materialdialogs.MaterialDialog.Builder(this)).title(getString(R.string.edittext_pref_dialogtitle)).content(R.string.optional_dialog_message).cancelable(false).positiveText("Ok").positiveColor(Color.parseColor("#379e24")).customView(R.layout.alert_dialog, true).negativeText(0x1040000).negativeColor(Color.parseColor("#379e24")).callback(new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
            public void onNegative(MaterialDialog materialdialog1)
            {
            }

            public void onPositive(MaterialDialog materialdialog1)
            {
                editor = pref.edit();
                editor.putString((new StringBuilder(String.valueOf(name))).append("daylight").toString(), text.getText().toString().trim());
                editor.commit();
                try
                {
                    setalarm(name, n_time, false);
                    setalarm(name, addDayLight(n_time, Integer.parseInt(LoadStringPref((new StringBuilder(String.valueOf(name))).append("daylight").toString()))), true);
                    LogUtils.i((new StringBuilder(String.valueOf(latitude))).append(" lat ").append(longitude).toString());
                    initPrayerTime(time);
                    return;
                }
                catch(ParseException parseexception)
                {
                    parseexception.printStackTrace();
                }
            }
        }).build();
        text = (EditText)materialdialog.getCustomView().findViewById(R.id.text);
        LogUtils.i((new StringBuilder(" name: ")).append(name).toString());
        text.setText(pref.getString((new StringBuilder(String.valueOf(name))).append("daylight").toString(), "0"));
        text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable)
            {
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }
        });
        materialdialog.show();
    }

    public void PrayerTimeTomorrow(int i, String s)
        throws ParseException
    {
        ArrayList arraylist = preinitPrayerTime(1).getPrayerTimes(cal, latitude, longitude, timezone);
        if(latitude == 0.0D || longitude == 0.0D)
        	return;
        String as[];
        int j;
        as = getResources().getStringArray(R.array.prayer_array);
        j = 0;
        while(j < arraylist.size()){
        	if(id == 0)
            {
            	set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(as[0]))).append("daylight").toString(), "0"))));
            	set_alarm_tomoro(addDayLight((String)arraylist.get(0), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(as[1]))).append("daylight").toString(), "0"))));
            } else if(id == 3) {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(as[2]))).append("daylight").toString(), "0"))));
            } else if(id == 4) {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(as[3]))).append("daylight").toString(), "0"))));
            } else if(id == 5) {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(as[4]))).append("daylight").toString(), "0"))));
            } else if(id == 6) {
                set_alarm_tomoro(addDayLight((String)arraylist.get(j), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(as[5]))).append("daylight").toString(), "0"))));
            }
            j++;
        }
    }

    public void cancel_alarm()
    {
        pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
        ((AlarmManager)getSystemService("alarm")).cancel(pendingIntent);
        Toast.makeText(this, "Alarm disable", 0).show();
    }

    public void initPrayerTime(int i)
        throws ParseException
    {
        ArrayList arraylist;
        int j;
        int l;
        font();
        PrayTime praytime = preinitPrayerTime(i);
        arraylist = praytime.getPrayerTimes(getTimeByCal(i), latitude, longitude, timezone);
        praytime.getTimeNames();
        j = 0;
        int k = arraylist.size();
        while(j < k){
        	String as1[];
            String as2[];
            if(pref_time.equals("0"))
            {
                String as3[] = new String[6];
                as3[0] = addDayLight((String)arraylist.get(0), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0")));
                as3[1] = addDayLight((String)arraylist.get(0), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0")));
                as3[2] = addDayLight((String)arraylist.get(2), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0")));
                as3[3] = addDayLight((String)arraylist.get(3), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0")));
                as3[4] = addDayLight((String)arraylist.get(5), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0")));
                as3[5] = addDayLight((String)arraylist.get(6), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0")));
                p_time = as3;
            } else
            {
                String as[] = new String[6];
                as[0] = addDayLight(main((String)arraylist.get(0)), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0")));
                as[1] = addDayLight(main((String)arraylist.get(0)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0")));
                as[2] = addDayLight(main((String)arraylist.get(2)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0")));
                as[3] = addDayLight(main((String)arraylist.get(3)), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0")));
                as[4] = addDayLight(main((String)arraylist.get(5)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0")));
                as[5] = addDayLight(main((String)arraylist.get(6)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0")));
                p_time = as;
            }
            as1 = new String[6];
            as1[0] = set_time((String)arraylist.get(0), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0")), 0);
            as1[1] = set_time((String)arraylist.get(0), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0")), 0);
            as1[2] = set_time((String)arraylist.get(2), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0")), 0);
            as1[3] = set_time((String)arraylist.get(3), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0")), 0);
            as1[4] = set_time((String)arraylist.get(5), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0")), 0);
            as1[5] = set_time((String)arraylist.get(6), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0")), 0);
            p_time_1 = as1;
            as2 = new String[6];
            as2[0] = (new StringBuilder(String.valueOf(p_time_1[0]))).append(" ").append(addDay_Light(main((String)arraylist.get(0)), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0")))).toString();
            as2[1] = (new StringBuilder(String.valueOf(p_time_1[1]))).append(" ").append(addDay_Light(main((String)arraylist.get(0)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0")))).toString();
            as2[2] = (new StringBuilder(String.valueOf(p_time_1[2]))).append(" ").append(addDay_Light(main((String)arraylist.get(2)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0")))).toString();
            as2[3] = (new StringBuilder(String.valueOf(p_time_1[3]))).append(" ").append(addDay_Light(main((String)arraylist.get(3)), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0")))).toString();
            as2[4] = (new StringBuilder(String.valueOf(p_time_1[4]))).append(" ").append(addDay_Light(main((String)arraylist.get(5)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0")))).toString();
            as2[5] = (new StringBuilder(String.valueOf(p_time_1[5]))).append(" ").append(addDay_Light(main((String)arraylist.get(6)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0")))).toString();
            p_next_prayer = as2;
            j++;
        }
        n_array.clear();
        values.clear();
        nxt_time = "";
        alarm_value.clear();
        l = 0;
        int i1 = p_next_prayer.length;
        while(l < i1){
        	SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US);
            Date date = simpledateformat.parse(simpledateformat.format(new Date()));
            long l1 = simpledateformat.parse(p_next_prayer[l]).getTime() - date.getTime();
            int j1 = (int)(l1 / 0x5265c00L);
            int k1 = (int)((l1 - (long)(0x5265c00 * j1)) / 0x36ee80L);
            int i2 = (int)(l1 - (long)(0x5265c00 * j1) - (long)(0x36ee80 * k1)) / 60000;
            if(k1 >= 0 && i2 >= 0)
            {
                String s;
                if(i2 < 9)
                {
                    s = (new StringBuilder(String.valueOf(k1))).append(".0").append(i2).toString();
                } else
                {
                    s = (new StringBuilder(String.valueOf(k1))).append(".").append(i2).toString();
                }
                if(i2 > 9)
                {
                    ArrayList arraylist2 = alarm_value;
                    Object aobj1[] = new Object[1];
                    aobj1[0] = Double.valueOf(Double.parseDouble((new StringBuilder(String.valueOf(k1))).append(".").append(i2).toString()));
                    arraylist2.add((new StringBuilder(String.valueOf(String.format("%.2f", aobj1)))).append(" ").append(p_name[l]).toString());
                } else
                {
                    ArrayList arraylist1 = alarm_value;
                    Object aobj[] = new Object[1];
                    aobj[0] = Double.valueOf(Double.parseDouble((new StringBuilder(String.valueOf(k1))).append(".").append(i2).toString()));
                    arraylist1.add((new StringBuilder(String.valueOf(String.format("%.1f", aobj)))).append(" ").append(p_name[l]).toString());
                }
                n_array.add(Double.valueOf(Double.parseDouble(s.replace("-", "").replace(",", ".").trim())));
                values.add((new StringBuilder(String.valueOf(s.replace("-", "").replace(",", ".")))).append(" ").append(p_name[l]).toString());
            }
            l++;
        }
        String s1 = "";
        if(n_array.size() <= 0){
        	String s2;
            int j2;
            String as4[] = new String[6];
            as4[0] = set_time((String)arraylist.get(0), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0")), 1);
            as4[1] = set_time((String)arraylist.get(0), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0")), 1);
            as4[2] = set_time((String)arraylist.get(2), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0")), 1);
            as4[3] = set_time((String)arraylist.get(3), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0")), 1);
            as4[4] = set_time((String)arraylist.get(5), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0")), 1);
            as4[5] = set_time((String)arraylist.get(6), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0")), 1);
            p_time_1 = as4;
            String as5[] = new String[6];
            as5[0] = (new StringBuilder(String.valueOf(p_time_1[0]))).append(" ").append(addDay_Light(main((String)arraylist.get(0)), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0")))).toString();
            as5[1] = (new StringBuilder(String.valueOf(p_time_1[1]))).append(" ").append(addDay_Light(main((String)arraylist.get(0)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0")))).toString();
            as5[2] = (new StringBuilder(String.valueOf(p_time_1[2]))).append(" ").append(addDay_Light(main((String)arraylist.get(2)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0")))).toString();
            as5[3] = (new StringBuilder(String.valueOf(p_time_1[3]))).append(" ").append(addDay_Light(main((String)arraylist.get(3)), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0")))).toString();
            as5[4] = (new StringBuilder(String.valueOf(p_time_1[4]))).append(" ").append(addDay_Light(main((String)arraylist.get(5)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0")))).toString();
            as5[5] = (new StringBuilder(String.valueOf(p_time_1[5]))).append(" ").append(addDay_Light(main((String)arraylist.get(6)), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0")))).toString();
            p_next_prayer = as5;
            n_array.clear();
            values.clear();
            alarm_value.clear();
            s2 = "";
            nxt_time = "";
            alarm_value.clear();
            //new String[0];
            j2 = 0;
            int k2 = p_next_prayer.length;
            while(j2 < k2){
            	SimpleDateFormat simpledateformat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US);
                Date date1 = simpledateformat1.parse(simpledateformat1.format(new Date()));
                Date date2 = simpledateformat1.parse(p_next_prayer[j2]);
                long l2 = date2.getTime() - date1.getTime();
                int i3 = (int)(l2 / 0x5265c00L);
                int j3 = (int)((l2 - (long)(0x5265c00 * i3)) / 0x36ee80L);
                int k3 = (int)(l2 - (long)(0x5265c00 * i3) - (long)(0x36ee80 * j3)) / 60000;
                LogUtils.i((new StringBuilder()).append(date1).append(" date ").append(date2).toString());
                if(j3 >= 0 && k3 >= 0)
                {
                    String s3;
                    if(k3 < 9)
                    {
                        s3 = (new StringBuilder(String.valueOf(j3))).append(".0").append(k3).toString();
                    } else
                    {
                        s3 = (new StringBuilder(String.valueOf(j3))).append(".").append(k3).toString();
                    }
                    LogUtils.i((new StringBuilder(" hours ")).append(s3).toString());
                    if(k3 > 9)
                    {
                        ArrayList arraylist4 = alarm_value;
                        Object aobj3[] = new Object[1];
                        aobj3[0] = Double.valueOf(Double.parseDouble((new StringBuilder(String.valueOf(j3))).append(".").append(k3).toString()));
                        arraylist4.add((new StringBuilder(String.valueOf(String.format("%.2f", aobj3)))).append(" ").append(p_name[j2]).toString());
                    } else
                    {
                        ArrayList arraylist3 = alarm_value;
                        Object aobj2[] = new Object[1];
                        aobj2[0] = Double.valueOf(Double.parseDouble((new StringBuilder(String.valueOf(j3))).append(".").append(k3).toString()));
                        arraylist3.add((new StringBuilder(String.valueOf(String.format("%.1f", aobj2)))).append(" ").append(p_name[j2]).toString());
                    }
                    n_array.add(Double.valueOf(Double.parseDouble(s3.replace("-", "").replace(",", ".").trim())));
                    values.add((new StringBuilder(String.valueOf(s3.replace("-", "").replace(",", ".")))).append(" ").append(p_name[j2]).toString());
                }
                j2++;
            }
            if(n_array.size() > 0){
    	        int l3;
    	        Collections.sort(n_array);
    	        l3 = 0;
    	        int i4 = values.size();
    	        while(l3 < i4){
    	        	String as6[];
    	            as6 = ((String)values.get(l3)).split(" ");
    	            LogUtils.i((new StringBuilder()).append(n_array.get(0)).append(" values value ").append(n_array.get(1)).toString());
    	            if(((Double)n_array.get(0)).doubleValue() == Double.parseDouble(as6[0].trim())){
    	    	        int j4;
    	    	        nxt_time = as6[1].trim();
    	    	        j4 = 0;
    	    	        int k4 = alarm_value.size();
    	    	        while(j4 < k4)
    	    	        {
    	    	        	if(((String)alarm_value.get(j4)).contains(nxt_time))
    	    	            {
    	    	                LogUtils.i((new StringBuilder(String.valueOf((String)alarm_value.get(j4)))).append(" alaram value ").append(nxt_time).toString());
    	    	                String as7[] = ((String)alarm_value.get(j4)).replace(nxt_time, "").replace(".", "/").replace("\u066B", "/").replace(",", "/").split("/");
    	    	                if(as7[0].trim().equals("0"))
    	    	                {
    	    	                    s2 = (new StringBuilder(String.valueOf(as7[1].trim()))).append(" minutes").toString();
    	    	                } else
    	    	                {
    	    	                    s2 = (new StringBuilder(String.valueOf(as7[0].trim()))).append(" Hours ").append(as7[1].trim()).append(" Minutes").toString();
    	    	                }
    	    	            }
    	    	            j4++;
    	    	        }
    	            }
    	            l3++;
    	        }
    	        txt_prayer_time.setText((new StringBuilder(String.valueOf(nxt_time))).append(" ").append(s2).toString());
            }
        }else{
	        int l4;
	        Collections.sort(n_array);
	        l4 = 0;
	        int i5 = values.size();
	        while(l4 < i5){
	        	String as8[] = ((String)values.get(l4)).split(" ");
	            if(((Double)n_array.get(0)).doubleValue() == Double.parseDouble(as8[0].trim())){
	    	        int j5;
	    	        nxt_time = as8[1].trim();
	    	        j5 = 0;
	    	        int k5 = alarm_value.size();
	    	        while(j5 < k5)
	    	        {
	    	        	if(((String)alarm_value.get(j5)).contains(nxt_time))
	    	            {
	    	                String as9[] = ((String)alarm_value.get(j5)).replace(nxt_time, "").replace(".", "/").replace("\u066B", "/").replace(",", "/").split("/");
	    	                LogUtils.i((new StringBuilder(String.valueOf((String)alarm_value.get(j5)))).append(" val[0].trim() ").append(as9[0].trim()).append(" nxt_time ").append(nxt_time).toString());
	    	                if(as9[0].trim().equals("0"))
	    	                {
	    	                    s1 = (new StringBuilder(String.valueOf(as9[1].trim()))).append(" minutes").toString();
	    	                } else
	    	                {
	    	                    s1 = (new StringBuilder(String.valueOf(as9[0].trim()))).append(" Hours ").append(as9[1].trim()).append(" Minutes").toString();
	    	                }
	    	            }
	    	            j5++;
	    	        }
	            }
	            l4++;
	        }
	        txt_prayer_time.setText((new StringBuilder(String.valueOf(nxt_time))).append(" ").append(s1).toString());
        }
        lv_gridview.setAdapter(adapter);
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

    public void onClick(View view)
    {
        view.getId();
    }

    public void onCreate(Bundle bundle)
    {
        fullscreen();
        super.onCreate(bundle);
        setContentView(R.layout.activity_prayer_times);
        adRequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("B5169A57FF57E5D323B4A99BC6BC7B37").build();

        p_name = getResources().getStringArray(R.array.prayer_array);
        Actionbar(getString(R.string.lbl_prayertimes_title));
        typeface();
        banner_ad();
        Analytics(getString(R.string.lbl_prayertimes_title));
        appLocationService = new AppLocationService(this);
        img_slider_next = (ImageView)findViewById(R.id.img_slider_next);
        img_slider_previous = (ImageView)findViewById(R.id.img_slider_previous);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        txt_time = (TextView)findViewById(R.id.txt_time);
        txt_date = (TextView)findViewById(R.id.txt_date);
        txt_place = (TextView)findViewById(R.id.txt_place);
        txt_title = (TextView)findViewById(R.id.txt_title);
        txt_prayer_time = (TextView)findViewById(R.id.txt_prayer_time);
        txt_street = (TextView)findViewById(R.id.txt_street);
        d_time = (new SimpleDateFormat("hh:mm", Locale.ENGLISH)).format(new Date());
        lv_gridview = (GridView)findViewById(R.id.lv_gridview);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE", Locale.US);
        String s = (new SimpleDateFormat("MMMM")).format(calendar.getTime());
        cur_time = (new SimpleDateFormat("hh:mm aa", Locale.ENGLISH)).format(new Date());
        txt_time.setText(cur_time);
        weekDay = simpledateformat.format(calendar.getTime());
        txt_date.setText((new StringBuilder(String.valueOf(weekDay))).append(", ").append(s).append(" ").append(calendar.get(5)).append(", ").append(calendar.get(1)).toString());
        Qurancal = Calendar.getInstance();
        Qurancal.add(5, 0);
        String s1 = (new StringBuilder(String.valueOf(HijriCalendarDate.getSimpleDateDay(Qurancal, 0)))).append(" ").append(HijriCalendarDate.getSimpleDateMonth(Qurancal, 0)).append(" ").append(HijriCalendarDate.getSimpleDateYear(Qurancal, 0)).toString();
        txt_title.setText(s1);
        alarmIntent = new Intent(this, com.prayertimes.qibla.appsourcehub.receiver.AlarmReceiver.class);
        numberFormat = new DecimalFormat("#.##");
        img_slider_next.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                PrayerTimesActivity prayertimesactivity;
                android.view.animation.Animation animation;
                Runnable runnable;

                prayertimesactivity = PrayerTimesActivity.this;
                prayertimesactivity.interflagnext = 1 + prayertimesactivity.interflagnext;
                animation = AnimationUtils.loadAnimation(PrayerTimesActivity.this, R.anim.push_left_out);
                lv_gridview.startAnimation(animation);
                txt_date.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                        android.view.animation.Animation animation;
                        try
                        {
                            PrayerTimesActivity prayertimesactivity = PrayerTimesActivity.this;
                            prayertimesactivity.time = 1 + prayertimesactivity.time;
                            LogUtils.i((new StringBuilder("previous time ")).append(time).toString());
                            today = time;
                            txt_date.setText(getTimeNow(time));
                            Qurancal.add(5, 1);
                            String s = (new StringBuilder(String.valueOf(HijriCalendarDate.getSimpleDateDay(Qurancal, 0)))).append(" ").append(HijriCalendarDate.getSimpleDateMonth(Qurancal, 0)).append(" ").append(HijriCalendarDate.getSimpleDateYear(Qurancal, 0)).toString();
                            txt_title.setText(s);
                            initPrayerTime(time);
                        }
                        catch(ParseException parseexception)
                        {
                            parseexception.printStackTrace();
                        }
                        animation = AnimationUtils.loadAnimation(PrayerTimesActivity.this, R.anim.push_left_in);
                        lv_gridview.startAnimation(animation);
                        txt_date.startAnimation(animation);
                        txt_title.startAnimation(animation);
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
        img_slider_previous.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                PrayerTimesActivity prayertimesactivity;
                android.view.animation.Animation animation;
                Runnable runnable;

                prayertimesactivity = PrayerTimesActivity.this;
                prayertimesactivity.interflagprevious = 1 + prayertimesactivity.interflagprevious;
                animation = AnimationUtils.loadAnimation(PrayerTimesActivity.this, R.anim.push_right_out);
                lv_gridview.startAnimation(animation);
                txt_date.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                        android.view.animation.Animation animation;
                        try
                        {
                            PrayerTimesActivity prayertimesactivity = PrayerTimesActivity.this;
                            prayertimesactivity.time = -1 + prayertimesactivity.time;
                            LogUtils.i((new StringBuilder("previous time ")).append(time).toString());
                            today = time;
                            txt_date.setText(getTimeNow(time));
                            Qurancal.add(5, -1);
                            String s = (new StringBuilder(String.valueOf(HijriCalendarDate.getSimpleDateDay(Qurancal, 0)))).append(" ").append(HijriCalendarDate.getSimpleDateMonth(Qurancal, 0)).append(" ").append(HijriCalendarDate.getSimpleDateYear(Qurancal, 0)).toString();
                            txt_title.setText(s);
                            initPrayerTime(time);
                        }
                        catch(ParseException parseexception)
                        {
                            parseexception.printStackTrace();
                        }
                        animation = AnimationUtils.loadAnimation(PrayerTimesActivity.this, R.anim.push_right_in);
                        lv_gridview.startAnimation(animation);
                        txt_date.startAnimation(animation);
                        txt_title.startAnimation(animation);
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
        txt_time.setTypeface(tf2, 1);
        txt_title.setTypeface(tf2, 1);
        txt_date.setTypeface(tf2, 1);
        txt_place.setTypeface(tf2, 1);
        txt_prayer_time.setTypeface(tf2, 1);
        txt_street.setTypeface(tf2, 1);
    }

    protected void onDestroy()
    {
        if(appLocationService != null)
        {
            appLocationService.stopUsingGPS();
            stopService(new Intent(this, com.prayertimes.qibla.appsourcehub.support.AppLocationService.class));
        }
        super.onDestroy();
    }

    protected void onResume()
    {
        font();
        cur_time = (new SimpleDateFormat("hh:mm aa", Locale.ENGLISH)).format(new Date());
        if(pref_time.equals("0"))
        {
            txt_time.setText(cur_time);
        } else
        {
            String s = (new SimpleDateFormat("HH:mm")).format(new Date());
            System.out.println(s);
            txt_time.setText(s);
        }
        LogUtils.i((new StringBuilder(" LoadPref(USER_LAT) ")).append(LoadPref(USER_LAT)).toString());
        latitude = Double.parseDouble(loadString(USER_LAT));
        longitude = Double.parseDouble(loadString(USER_LNG));
        LogUtils.i((new StringBuilder(String.valueOf(latitude))).append(" lat in ").append(longitude).toString());
        txt_street.setText(LoadPref(USER_STREET));
        txt_place.setText((new StringBuilder(String.valueOf(LoadPref(USER_CITY)))).append(" ").append(LoadPref(USER_STATE)).append(" ").append(LoadPref(USER_COUNTRY)).toString());
        LogUtils.i((new StringBuilder("txt_time ")).append(txt_time.getTypeface()).toString());
        adapter = new AdapterPrayer(this);
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

    public PrayTime preinitPrayerTime(int i)
    {
        String s = pref.getString("timezone", "");
        PrayTime praytime = new PrayTime();
        praytime.setTimeFormat(praytime.Time12);
        praytime.setCalcMethod(Integer.parseInt(pref.getString("method", "1")));
        praytime.setAsrJuristic(Integer.parseInt(pref.getString("juristic", "1")));
        praytime.setAdjustHighLats(Integer.parseInt(pref.getString("higherLatitudes", "1")));
        praytime.tune(new int[7]);
        Date date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, i);
        if(s.equals(""))
        {
            timezone = getTimeZone(cal.getTimeZone().getID().toString());
            String s1 = cal.getTimeZone().getID().toString();
            editor = pref.edit();
            editor.putString("timezone", s1);
            editor.commit();
            return praytime;
        } else
        {
            timezone = getTimeZone(s);
            return praytime;
        }
    }

    public void set_alarm(String s, String s1)
    {
        SimpleDateFormat simpledateformat;
        String s2;
        int i;
        simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        s2 = simpledateformat.format(new Date());
        i = 0;
        int k;
        int j;
        try{
	        String s3 = changeTimeFormat(s);
	        i = Integer.parseInt(s3.substring(0, 2));
	        k = Integer.parseInt(s3.substring(3, 5));
	        j = k;
        }catch(Exception e){
        	j = 0;
        }
        Calendar calendar = Calendar.getInstance();
        ParseException parseexception;
        Date date;
        Calendar calendar1;
        try
        {
            calendar.setTime(simpledateformat.parse(s2));
            calendar.set(11, i);
            calendar.set(12, j + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(s1))).append("daylight").toString(), "0")));
            date = new Date();
            calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            if(calendar1.after(calendar))
            {
                try
                {
                    alarm_time = "fals";
                    Toast.makeText(this, "Prayer has been passed", 0).show();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            } else
            {
                alarm_time = "tru";
                pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
                ((AlarmManager)getSystemService("alarm")).set(0, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(this, " Alarm Enabled", 0).show();
            }
            setbg(s1, alarm_time);
            return;
        }
        catch(ParseException parseexception1)
        {
            parseexception1.printStackTrace();
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
        pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
        ((AlarmManager)getSystemService("alarm")).set(0, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, getString(R.string.toast_prayerhasbeenpassed), 0).show();
    }

    public String set_time(String s, int i, int j)
    {
        SimpleDateFormat simpledateformat;
        String s1;
        int k;
        simpledateformat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        s1 = simpledateformat.format(new Date());
        k = 0;
        int i1;
        int l;
        try{
	        String s2 = changeTimeFormat(s);
	        k = Integer.parseInt(s2.substring(0, 2));
	        i1 = Integer.parseInt(s2.substring(3, 5));
	        l = i1;
        }catch(Exception e){
        	l = 0;
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
        if(j == 1)
        {
            calendar.set(11, k + 12);
            calendar.add(5, 1);
        } else
        {
            calendar.set(11, k);
        }
        calendar.set(12, l);
        return simpledateformat.format(calendar.getTime());
    }

    public void setalarm(String s, String s1, boolean flag)
    {
        LogUtils.i((new StringBuilder(String.valueOf(s))).append(" setalarm ").append(flag).toString());
        if(!flag)
        {
            cancel_alarm();
            setbg(s, "fals");
            return;
        } else
        {
            alarmIntent.putExtra("type", s);
            alarmIntent.putExtra("time", s1);
            set_alarm(s1, s);
            return;
        }
    }

    public boolean setalarmimage(String s, String s1)
    {
        SimpleDateFormat simpledateformat;
        String s2;
        int i;
        simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        s2 = simpledateformat.format(new Date());
        i = 0;
        int k;
        int j;
        try{
	        String s3 = changeTimeFormat(s1);
	        i = Integer.parseInt(s3.substring(0, 2));
	        k = Integer.parseInt(s3.substring(3, 5));
	        j = k;
        }catch(Exception e){
        	j = 0;
        }
        Calendar calendar = Calendar.getInstance();
        ParseException parseexception;
        Date date;
        Calendar calendar1;
        try
        {
            calendar.setTime(simpledateformat.parse(s2));
        }
        catch(ParseException parseexception1)
        {
            parseexception1.printStackTrace();
        }
        calendar.set(11, i);
        calendar.set(12, j + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(s))).append("daylight").toString(), "0")));
        date = new Date();
        calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        return calendar1.after(calendar);
    }

    public void setbg(String s, String s1)
    {
        SavePref(s, s1);
        LogUtils.i((new StringBuilder(String.valueOf(LoadPref(s)))).append(" calendar name ").append(s).toString());
        adapter.notifyDataSetChanged();
    }

}

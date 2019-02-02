package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.support.PrayTime;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.text.*;
import java.util.*;
import muslim.prayers.time.R;
public class FragmentCalender extends Utils
{
    public class AdapterCalender extends BaseAdapter
    {

        ViewHolder holder;
        LayoutInflater inflater;
        Context mContext;

        public int getCount()
        {
            return time;
        }

        public Object getItem(int i)
        {
            return null;
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            holder = new ViewHolder();
            View view1 = inflater.inflate(R.layout.listview_calender, null);
            holder.lyt_listview = (LinearLayout)view1.findViewById(R.id.lyt_listview);
            holder.lblDate = (TextView)view1.findViewById(R.id.lblDate);
            holder.lblimsak = (TextView)view1.findViewById(R.id.lblimsak);
            holder.lblfajar = (TextView)view1.findViewById(R.id.lblfajar);
            holder.lbldhur = (TextView)view1.findViewById(R.id.lbldhur);
            holder.lblasr = (TextView)view1.findViewById(R.id.lblasr);
            holder.lblmaghrib = (TextView)view1.findViewById(R.id.lblmaghrib);
            holder.lblisha = (TextView)view1.findViewById(R.id.lblisha);
            holder.lblDate.setTypeface(FragmentCalender.this.tf2, 1);
            holder.lblimsak.setTypeface(FragmentCalender.this.tf2, 1);
            holder.lblfajar.setTypeface(FragmentCalender.this.tf2, 1);
            holder.lbldhur.setTypeface(FragmentCalender.this.tf2, 1);
            holder.lblmaghrib.setTypeface(FragmentCalender.this.tf2, 1);
            holder.lblisha.setTypeface(FragmentCalender.this.tf2, 1);
            holder.lblasr.setTypeface(FragmentCalender.this.tf2, 1);
            view1.setTag(holder);
            int _tmp = i % 2;
            try
            {
                initPrayerTime(i);
            }
            catch(ParseException parseexception)
            {
                parseexception.printStackTrace();
                return view1;
            }
            return view1;
        }

        public void initPrayerTime(int i) throws ParseException
        {
            double d = Double.parseDouble(loadString(USER_LAT));
            double d1 = Double.parseDouble(loadString(USER_LNG));
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String s = sharedpreferences.getString("timezone", "");
            PrayTime praytime = new PrayTime();
            praytime.setTimeFormat(praytime.Time12);
            praytime.setCalcMethod(Integer.parseInt(sharedpreferences.getString("method", "1")));
            praytime.setAsrJuristic(Integer.parseInt(sharedpreferences.getString("juristic", "1")));
            praytime.setAdjustHighLats(Integer.parseInt(sharedpreferences.getString("higherLatitudes", "1")));
            praytime.tune(new int[7]);
            new Date();
            cal.add(5, i);
            double d2;
            SimpleDateFormat simpledateformat;
            ArrayList arraylist;
            String as[];
            if(s.equals(""))
            {
                String s1 = cal.getTimeZone().getID().toString();
                d2 = getTimeZone(cal.getTimeZone().getID().toString());
                android.content.SharedPreferences.Editor editor1 = sharedpreferences.edit();
                editor1.putString("timezone", s1);
                editor1.commit();
            } else
            {
                d2 = getTimeZone(s);
            }
            simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            holder.lblDate.setText(simpledateformat.format(getCalenderTime(i, month, year_).getTime()));
            arraylist = praytime.getPrayerTimes(getCalenderTime(i, month, year_), d, d1, d2);
            praytime.getTimeNames();
            
            if(FragmentCalender.this.pref_time.equals("0"))
            {
                holder.lblimsak.setText(addDayLight(((String)arraylist.get(0)).toString(), -15 + Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0"))));
                holder.lblfajar.setText(addDayLight(((String)arraylist.get(0)).toString(), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0"))));
                holder.lbldhur.setText(addDayLight(((String)arraylist.get(2)).toString(), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0"))));
                holder.lblasr.setText(addDayLight(((String)arraylist.get(3)).toString(), -66 + Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0"))));
                holder.lblmaghrib.setText(addDayLight(((String)arraylist.get(5)).toString(), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0"))));
                holder.lblisha.setText(addDayLight(((String)arraylist.get(6)).toString(), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0"))));
            } else {
                holder.lblimsak.setText(addDayLight(main(((String)arraylist.get(0)).toString()), -15 + Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0"))));
                holder.lblfajar.setText(addDayLight(main((String)arraylist.get(0)), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[1]))).append("daylight").toString(), "0"))));
                holder.lbldhur.setText(addDayLight(main((String)arraylist.get(2)), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[2]))).append("daylight").toString(), "0"))));
                holder.lblasr.setText(addDayLight(main((String)arraylist.get(3)), -66 + Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[3]))).append("daylight").toString(), "0"))));
                holder.lblmaghrib.setText(addDayLight(main((String)arraylist.get(5)), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[4]))).append("daylight").toString(), "0"))));
                holder.lblisha.setText(addDayLight(main((String)arraylist.get(6)), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0"))));
            }
            if(i == 0)
            {
            	as = simpledateformat.format(getCalenderTime(i, month, year_).getTime()).split(" ");
                title.setText(as[0].trim());
                year.setText(as[2].trim());
            }
            
        }
        
        public AdapterCalender(Context context)
        {
            super();
            mContext = context;
            inflater = LayoutInflater.from(mContext);
        }
        
        public class ViewHolder
        {

            TextView lblDate;
            TextView lblasr;
            TextView lbldhur;
            TextView lblfajar;
            TextView lblimsak;
            TextView lblisha;
            TextView lblmaghrib;
            LinearLayout lyt_listview;

            public ViewHolder()
            {
                super();
            }
        }
    }


    AdapterCalender adapter;
    ArrayList arraylist;
    Calendar cal;
    String city_name[];
    int cur_month;
    int cur_time;
    android.content.SharedPreferences.Editor editor;
    EditText editsearch;
    ImageView ic_next;
    ImageView ic_previous;
    RelativeLayout layoutcalendar;
    TextView lblAsar;
    TextView lblDate;
    TextView lblEndDate;
    TextView lblIsha;
    TextView lblMaghrib;
    TextView lblStartDate;
    TextView lblZuher;
    ListView list;
    int month;
    String p_name[];
    SharedPreferences pref;
    int time;
    TextView title;
    String type;
    TextView year;
    int year_;

    public FragmentCalender()
    {
        type = "country";
        arraylist = new ArrayList();
        time = 0;
        cur_month = 0;
        year_ = 0;
    }

    static boolean isLeapYear(int i)
    {
        return i % 4 == 0 && (i % 100 != 0 || i % 400 == 0);
    }

    static int monthDays(int i, int j)
    {
        switch(j)
        {
        default:
            return -1;

        case 1: // '\001'
        case 3: // '\003'
        case 5: // '\005'
        case 7: // '\007'
        case 8: // '\b'
        case 10: // '\n'
        case 12: // '\f'
            return 31;

        case 4: // '\004'
        case 6: // '\006'
        case 9: // '\t'
        case 11: // '\013'
            return 30;

        case 2: // '\002'
            break;
        }
        return !isLeapYear(i) ? 28 : 29;
    }

    public Calendar getCalenderTime(int i, int j, int k)
        throws ParseException
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String s = simpledateformat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(2, cur_month);
        calendar.set(1, calendar.get(1));
        LogUtils.i((new StringBuilder("Year Before : ")).append(calendar.get(1)).toString());
        calendar.add(5, i);
        LogUtils.i((new StringBuilder("Year after add: ")).append(calendar.get(1)).toString());
        return calendar;
    }

    String getMonthForInt(int i)
    {
        int j = i - 1;
        String s = "";
        String as[] = (new DateFormatSymbols()).getMonths();
        if(j >= 0 && j <= 11)
        {
            s = as[j];
        }
        return s;
    }

    public String getTimeNows(int i)
        throws ParseException
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM d, yyyy");
        String s = simpledateformat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(2, month);
        return simpledateformat.format(calendar.getTime());
    }

    public String main(String s)
    {
    	Date date;
        SimpleDateFormat simpledateformat;
        SimpleDateFormat simpledateformat1;
        simpledateformat = new SimpleDateFormat("HH:mm");
        simpledateformat1 = new SimpleDateFormat("hh:mm a");
        try{
	        Date date1 = simpledateformat1.parse(s);
	        date = date1;
        }catch(Exception e){
        	date = null;
        }
        return simpledateformat.format(date);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_calender);
        p_name = getResources().getStringArray(R.array.prayer_array);
        Actionbar(getString(R.string.lblprayertime));
        typeface();
        banner_ad();
        Analytics(getString(R.string.lblprayertime));
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        ic_previous = (ImageView)findViewById(R.id.ic_previous);
        ic_next = (ImageView)findViewById(R.id.ic_next);
        title = (TextView)findViewById(R.id.title);
        year = (TextView)findViewById(R.id.year);
        list = (ListView)findViewById(R.id.listview);
        lblStartDate = (TextView)findViewById(R.id.lblStartDate);
        lblEndDate = (TextView)findViewById(R.id.lblEndDate);
        lblDate = (TextView)findViewById(R.id.lblDate);
        lblStartDate.setTypeface(tf2, 1);
        lblEndDate.setTypeface(tf2, 1);
        lblDate.setTypeface(tf2, 1);
        lblIsha = (TextView)findViewById(R.id.lblIsha);
        lblMaghrib = (TextView)findViewById(R.id.lblMaghrib);
        lblAsar = (TextView)findViewById(R.id.lblAsar);
        lblZuher = (TextView)findViewById(R.id.lblZuher);
        lblZuher.setTypeface(tf2, 1);
        lblIsha.setTypeface(tf2, 1);
        lblMaghrib.setTypeface(tf2, 1);
        lblAsar.setTypeface(tf2, 1);
        title.setTypeface(tf2, 1);
        year.setTypeface(tf2, 1);
        Date date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        month = 1 + cal.get(2);
        LogUtils.i((new StringBuilder(String.valueOf(cal.get(1)))).append(" month ").append(1 + cal.get(2)).toString());
        title.setText(getMonthForInt(month));
        year.setText((new StringBuilder()).append(cal.get(1)).toString());
        time = cal.getActualMaximum(5);
        LogUtils.i((new StringBuilder(String.valueOf(month))).append(" month ").append(1 + cal.get(2)).append(" days ").append(time).toString());
        ic_previous.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                android.view.animation.Animation animation = AnimationUtils.loadAnimation(FragmentCalender.this, R.anim.push_right_out);
                list.startAnimation(animation);
                title.startAnimation(animation);
                Runnable runnable = new Runnable() {
                    public void run()
                    {
                        FragmentCalender fragmentcalender = FragmentCalender.this;
                        fragmentcalender.month = -1 + fragmentcalender.month;
                        FragmentCalender fragmentcalender1 = FragmentCalender.this;
                        fragmentcalender1.cur_month = -1 + fragmentcalender1.cur_month;
                        time = cal.getActualMaximum(5);
                        LogUtils.i((new StringBuilder(String.valueOf(cur_month))).append(" month ").append(month).toString());
                        if(month == 12)
                        {
                            cur_time = 0;
                        }
                        android.view.animation.Animation animation;
                        if(month <= 0)
                        {
                            if(month <= 0)
                            {
                                cur_time = 12;
                                FragmentCalender fragmentcalender3 = FragmentCalender.this;
                                fragmentcalender3.year_ = -1 + fragmentcalender3.year_;
                                LogUtils.i((new StringBuilder("previous year_ : ")).append(year_).toString());
                            } else
                            {
                                FragmentCalender fragmentcalender2 = FragmentCalender.this;
                                fragmentcalender2.cur_time = -1 + fragmentcalender2.cur_time;
                            }
                        }
                        list.setAdapter(adapter);
                        animation = AnimationUtils.loadAnimation(FragmentCalender.this, R.anim.push_right_in);
                        list.startAnimation(animation);
                        title.startAnimation(animation);
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
            }
        });
        ic_next.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                android.view.animation.Animation animation = AnimationUtils.loadAnimation(FragmentCalender.this, R.anim.push_left_out);
                list.startAnimation(animation);
                title.startAnimation(animation);
                Runnable runnable = new Runnable() {
                    public void run()
                    {
                    	android.view.animation.Animation animation;
                        FragmentCalender fragmentcalender3;
                        FragmentCalender fragmentcalender4;
                        FragmentCalender fragmentcalender = FragmentCalender.this;
                        fragmentcalender.cur_month = 1 + fragmentcalender.cur_month;
                        time = cal.getActualMaximum(5);
                        LogUtils.i((new StringBuilder(String.valueOf(cur_month))).append(" month ").append(month).toString());
                        if(month == 12)
                        {
                            if(month % 12 == 0)
                            {
                                cur_time = 0;
                            } else
                            {
                                FragmentCalender fragmentcalender2 = FragmentCalender.this;
                                fragmentcalender2.cur_time = 1 + fragmentcalender2.cur_time;
                            }
                            fragmentcalender3 = FragmentCalender.this;
                            fragmentcalender3.month = 1 + fragmentcalender3.month;
                            fragmentcalender4 = FragmentCalender.this;
                            fragmentcalender4.year_ = 1 + fragmentcalender4.year_;
                        } else
                        {
                            FragmentCalender fragmentcalender1 = FragmentCalender.this;
                            fragmentcalender1.month = 1 + fragmentcalender1.month;
                        }
                        list.setAdapter(adapter);
                        animation = AnimationUtils.loadAnimation(FragmentCalender.this, R.anim.push_left_in);
                        list.startAnimation(animation);
                        title.startAnimation(animation);
                    }
                };
                (new Handler()).postDelayed(runnable, 150L);
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
        if(menuitem.getItemId() == 0x102002c)
        {
            finish();
        }
        return super.onOptionsItemSelected(menuitem);
    }

    protected void onResume()
    {
        font();
        adapter = new AdapterCalender(this);
        list.setAdapter(adapter);
        if(isOnline())
        {
            findViewById(R.id.ad_layout).setVisibility(0);
        } else
        {
            findViewById(R.id.ad_layout).setVisibility(8);
        }
        super.onResume();
    }


}

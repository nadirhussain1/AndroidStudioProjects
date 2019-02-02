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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.prayertimes.qibla.appsourcehub.support.PrayTime;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.text.*;
import java.util.*;
import muslim.prayers.time.R;
public class FragmentPrayer extends Utils
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
            if(view == null)
            {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.list_calender, null);
                holder.lyt_listview = (LinearLayout)view.findViewById(R.id.lyt_listview);
                holder.lblDate = (TextView)view.findViewById(R.id.lblDate);
                holder.lblStartDate = (TextView)view.findViewById(R.id.lblStartDate);
                holder.lblEndDate = (TextView)view.findViewById(R.id.lblEndDate);
                holder.lblDate.setTypeface(FragmentPrayer.this.tf2, 1);
                holder.lblStartDate.setTypeface(FragmentPrayer.this.tf2, 1);
                holder.lblEndDate.setTypeface(FragmentPrayer.this.tf2, 1);
                holder.lblDate.setTypeface(FragmentPrayer.this.tf2, 1);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            int _tmp = i % 2;
            try
            {
                initPrayerTime(i);
            }
            catch(ParseException parseexception)
            {
                parseexception.printStackTrace();
                return view;
            }
            return view;
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
            //new Date();
            cal.add(5, i);
            double d2;
            SimpleDateFormat simpledateformat;
            ArrayList arraylist1;
            String as[];
            if(s.equals(""))
            {
                String s1 = cal.getTimeZone().getID().toString();
                d2 = getTimeZone(cal.getTimeZone().getID().toString());
                android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("timezone", s1);
                editor.commit();
            } else
            {
                d2 = getTimeZone(s);
            }
            LogUtils.i("data", (new StringBuilder(" ")).append(d2).append(" data ").append(cal.getTimeZone().getID().toString()).toString());
            simpledateformat = new SimpleDateFormat("MMMM d, yyyy");
            holder.lblDate.setText(simpledateformat.format(getCalenderTime(i, month, year_).getTime()));
            arraylist1 = praytime.getPrayerTimes(getCalenderTime(i, month, year_), d, d1, d2);
            praytime.getTimeNames();
            if(pref_time.equals("0"))
            {
                holder.lblStartDate.setText(addDayLight((String)arraylist1.get(0), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0"))));
                holder.lblEndDate.setText(addDayLight((String)arraylist1.get(5), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0"))));
            } else {
            	holder.lblStartDate.setText(addDayLight(main(((String)arraylist.get(0)).toString()), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[0]))).append("daylight").toString(), "0"))));
                holder.lblEndDate.setText(addDayLight(main(((String)arraylist1.get(5)).toString()), Integer.parseInt(sharedpreferences.getString((new StringBuilder(String.valueOf(p_name[5]))).append("daylight").toString(), "0"))));
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
            TextView lblEndDate;
            TextView lblStartDate;
            LinearLayout lyt_listview;

            public ViewHolder()
            {
                super();
            }
        }
    }

    


    AdRequest adRequest;
    AdapterCalender adapter;
    ArrayList arraylist;
    Calendar cal;
    String city_name[];
    int cur_month;
    int cur_time;
    EditText editsearch;
    ImageView ic_next;
    ImageView ic_previous;
    int interflagnext;
    int interflagprevious;
    RelativeLayout layoutcalendar;
    TextView lblDate;
    TextView lblEndDate;
    TextView lblStartDate;
    ListView list;
    int month;
    String p_name[];
    int time;
    TextView title;
    String type;
    TextView year;
    int year_;

    public FragmentPrayer()
    {
        type = "country";
        arraylist = new ArrayList();
        time = 0;
        cur_month = 0;
        interflagprevious = 1;
        interflagnext = 1;
        year_ = 0;
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

    public String main(String s)
    {
        SimpleDateFormat simpledateformat;
        SimpleDateFormat simpledateformat1;
        Date date;
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
        setContentView(R.layout.fragment_prayer);
        adRequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("B5169A57FF57E5D323B4A99BC6BC7B37").build();
      
        p_name = getResources().getStringArray(R.array.prayer_array);
        Actionbar(getString(R.string.menu_prayer_ramdantime));
        typeface();
        banner_ad();
        Analytics(getString(R.string.menu_prayer_ramdantime));
        ic_previous = (ImageView)findViewById(R.id.ic_previous);
        ic_next = (ImageView)findViewById(R.id.ic_next);
        Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        list = (ListView)findViewById(R.id.listview);
        adapter = new AdapterCalender(this);
        list.setAdapter(adapter);
        title = (TextView)findViewById(R.id.title);
        year = (TextView)findViewById(R.id.year);
        lblStartDate = (TextView)findViewById(R.id.lblStartDate);
        lblEndDate = (TextView)findViewById(R.id.lblEndDate);
        lblDate = (TextView)findViewById(R.id.lblDate);
        lblStartDate.setTypeface(tf2, 1);
        lblEndDate.setTypeface(tf2, 1);
        lblDate.setTypeface(tf2, 1);
        title.setTypeface(tf2, 1);
        year.setTypeface(tf2, 1);
        Date date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        month = 1 + cal.get(2);
        title.setText(getMonthForInt(month));
        year.setText((new StringBuilder()).append(cal.get(1)).toString());
        time = cal.getActualMaximum(5);
        LogUtils.i((new StringBuilder(String.valueOf(month))).append(" month ").append(1 + cal.get(2)).append(" days ").append(time).toString());
        ic_previous.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                FragmentPrayer fragmentprayer;
                android.view.animation.Animation animation;
                Runnable runnable;
               
                fragmentprayer = FragmentPrayer.this;
                fragmentprayer.interflagprevious = 1 + fragmentprayer.interflagprevious;
                animation = AnimationUtils.loadAnimation(FragmentPrayer.this, R.anim.push_right_out);
                list.startAnimation(animation);
                title.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                        FragmentPrayer fragmentprayer = FragmentPrayer.this;
                        fragmentprayer.month = -1 + fragmentprayer.month;
                        FragmentPrayer fragmentprayer1 = FragmentPrayer.this;
                        fragmentprayer1.cur_month = -1 + fragmentprayer1.cur_month;
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
                                FragmentPrayer fragmentprayer3 = FragmentPrayer.this;
                                fragmentprayer3.year_ = -1 + fragmentprayer3.year_;
                            } else
                            {
                                FragmentPrayer fragmentprayer2 = FragmentPrayer.this;
                                fragmentprayer2.cur_time = -1 + fragmentprayer2.cur_time;
                            }
                        }
                        list.setAdapter(adapter);
                        animation = AnimationUtils.loadAnimation(FragmentPrayer.this, R.anim.push_right_in);
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
                FragmentPrayer fragmentprayer;
                android.view.animation.Animation animation;
                Runnable runnable;
               
                fragmentprayer = FragmentPrayer.this;
                fragmentprayer.interflagnext = 1 + fragmentprayer.interflagnext;
                animation = AnimationUtils.loadAnimation(FragmentPrayer.this, R.anim.push_left_out);
                list.startAnimation(animation);
                title.startAnimation(animation);
                runnable = new Runnable() {
                    public void run()
                    {
                    	android.view.animation.Animation animation;
                        FragmentPrayer fragmentprayer3;
                        FragmentPrayer fragmentprayer4;
                        FragmentPrayer fragmentprayer = FragmentPrayer.this;
                        fragmentprayer.cur_month = 1 + fragmentprayer.cur_month;
                        time = cal.getActualMaximum(5);
                        LogUtils.i((new StringBuilder(String.valueOf(cur_month))).append(" month ").append(month).toString());
                        if(month == 12)
                        {
                            if(month % 12 == 0)
                            {
                                cur_time = 0;
                            } else {
                                FragmentPrayer fragmentprayer2 = FragmentPrayer.this;
                                fragmentprayer2.cur_time = 1 + fragmentprayer2.cur_time;
                            }
                            fragmentprayer3 = FragmentPrayer.this;
                            fragmentprayer3.month = 1 + fragmentprayer3.month;
                            fragmentprayer4 = FragmentPrayer.this;
                            fragmentprayer4.year_ = 1 + fragmentprayer4.year_;
                        } else
                        {
                            FragmentPrayer fragmentprayer1 = FragmentPrayer.this;
                            fragmentprayer1.month = 1 + fragmentprayer1.month;
                        }
                        list.setAdapter(adapter);
                        animation = AnimationUtils.loadAnimation(FragmentPrayer.this, R.anim.push_left_in);
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
            finish();
        return super.onOptionsItemSelected(menuitem);
    }

    protected void onResume()
    {
        font();
        adapter = new AdapterCalender(this);
        list.setAdapter(adapter);
        if(isOnline())
            findViewById(R.id.ad_layout).setVisibility(0);
        else
            findViewById(R.id.ad_layout).setVisibility(8);
        super.onResume();
    }


}

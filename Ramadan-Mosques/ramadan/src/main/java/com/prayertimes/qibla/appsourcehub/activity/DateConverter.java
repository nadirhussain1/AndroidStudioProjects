package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import com.sahaab.hijricalendar.HijriCalendarDate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import muslim.prayers.time.R;
public class DateConverter extends Utils
{

    public static boolean isHijri = false;
    int curMonth;
    WheelView day;
    int gendday;
    TextView greg;
    TextView gregorianc_date;
    int gstartday;
    int hcurMonth;
    WheelView hday;
    int hendday;
    TextView hij;
    int hijri_day;
    int hijri_month;
    int hijri_year;
    TextView hijric_date;
    WheelView hmonth;
    public int hsel_day;
    int hstartday;
    WheelView hyear;
    LinearLayout layoutconverter;
    OnWheelScrollListener listener;
    int mili_day;
    int mili_month;
    int mili_year;
    int minimum_year;
    WheelView month;
    String []months;
    String []monthsh;
    public int sel_day;
    WheelView year;

    public DateConverter()
    {
        minimum_year = 623;
        hstartday = 1;
        hendday = 31;
        gstartday = 1;
        gendday = 31;
    }

    void hupdateDays(WheelView wheelview, WheelView wheelview1, WheelView wheelview2)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, calendar.get(1) + wheelview.getCurrentItem());
        calendar.set(2, wheelview1.getCurrentItem());
        int i = calendar.getActualMaximum(5);
        wheelview2.setViewAdapter(new DateNumericAdapter(this, hstartday, i, hsel_day));
        wheelview2.setCurrentItem(-1 + Math.min(i, 1 + wheelview2.getCurrentItem()), true);
    }

    void newgregoriandate()
    {
        int ai[] = (new GregorianCalendar()).islToChr(hyear.getCurrentItem() + minimum_year, hmonth.getCurrentItem(), 1 + hday.getCurrentItem(), 0);
        mili_year = ai[2];
        mili_month = -1 + ai[1];
        mili_day = -1 + ai[0];
        setGregorianCalendar();
        gregorianc_date.setText((new StringBuilder(String.valueOf(mili_day))).append("/").append(months[mili_month]).append("/").append(mili_year).toString());
        HijriCalendar hijricalendar = new HijriCalendar(year.getCurrentItem() + minimum_year, 1 + month.getCurrentItem(), 1 + day.getCurrentItem());
        hijri_year = hijricalendar.getHijriYear();
        hijri_month = hijricalendar.getHijriMonth();
        hijri_day = hijricalendar.getHijriDay();
        hijric_date.setText((new StringBuilder(String.valueOf(hijri_day))).append("/").append(monthsh[hijri_month]).append("/").append(hijri_year).toString());
        LogUtils.i((new StringBuilder("newgregoriandate ")).append(mili_year).append(" mili_month ").append(mili_month).toString());
        LogUtils.i((new StringBuilder(" mili_day ")).append(mili_day).toString());
    }

    void newhijridate()
    {
        HijriCalendar hijricalendar = new HijriCalendar(year.getCurrentItem() + minimum_year, 1 + month.getCurrentItem(), 1 + day.getCurrentItem());
        hijri_year = hijricalendar.getHijriYear();
        hijri_month = hijricalendar.getHijriMonth();
        hijri_day = hijricalendar.getHijriDay();
        setHijriCalendar();
        LogUtils.i((new StringBuilder("newhijridate ")).append(hijri_month).append(" hcurYear ").append(hijri_year).toString());
        LogUtils.i((new StringBuilder(" hijri_day ")).append(hijri_day).toString());
        hijric_date.setText((new StringBuilder(String.valueOf(hijri_day))).append("/").append(monthsh[hijri_month]).append("/").append(hijri_year).toString());
        int ai[] = (new GregorianCalendar()).islToChr(hyear.getCurrentItem() + minimum_year, hmonth.getCurrentItem(), 1 + hday.getCurrentItem(), 0);
        mili_year = ai[2];
        mili_month = -1 + ai[1];
        mili_day = -1 + ai[0];
        gregorianc_date.setText((new StringBuilder(String.valueOf(mili_day))).append("/").append(months[mili_month]).append("/").append(mili_year).toString());
    }

    public void onCreate(Bundle bundle)
    {
       // fullscreen();
        super.onCreate(bundle);
        setContentView(R.layout.activity_converter);
        
        months=getResources().getStringArray(R.array.date_converter_month);
        monthsh=getResources().getStringArray(R.array.date_converter_monthsh);
        
        Actionbar(getString(R.string.lbl_date));
        Analytics(getString(R.string.lbl_date));
        typeface();
        banner_ad();
        final Vibrator vibrator = (Vibrator)getSystemService("vibrator");
        Calendar calendar = Calendar.getInstance();
        HijriCalendar hijricalendar = new HijriCalendar(calendar.get(1), 1 + calendar.get(2), calendar.get(5));
        greg = (TextView)findViewById(R.id.gregorianc);
        greg.setTypeface(tf2);
        hij = (TextView)findViewById(R.id.hijric);
        hij.setTypeface(tf2);
        gregorianc_date = (TextView)findViewById(R.id.gregorianc_date);
        hijric_date = (TextView)findViewById(R.id.hijric_date);
        mili_year = calendar.get(1);
        mili_month = calendar.get(2);
        mili_day = calendar.get(5);
        hijri_year = hijricalendar.getHijriYear();
        hijri_month = hijricalendar.getHijriMonth();
        hijri_day = hijricalendar.getHijriDay();
        Log.d("Year", (new StringBuilder()).append(hijri_year).toString());
        Log.d("Year", (new StringBuilder()).append(hijri_month).toString());
        Log.d("Year", (new StringBuilder()).append(hijri_day).toString());
        month = (WheelView)findViewById(R.id.month);
        year = (WheelView)findViewById(R.id.year);
        day = (WheelView)findViewById(R.id.day);
        hmonth = (WheelView)findViewById(R.id.hmonth);
        hyear = (WheelView)findViewById(R.id.hyear);
        hday = (WheelView)findViewById(R.id.hday);
        listener = new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelview)
            {
                if(wheelview.getId() == R.id.day || wheelview.getId() == R.id.month || wheelview.getId() == R.id.year)
                {
                    updateDays(year, month, day);
                    newhijridate();
                    vibrator.vibrate(30L);
                } else if(wheelview.getId() == R.id.hday || wheelview.getId() == R.id.hmonth || wheelview.getId() == R.id.hyear)
                {
                    hupdateDays(hyear, hmonth, hday);
                    newgregoriandate();
                    vibrator.vibrate(30L);
                }
            }

            public void onScrollingStarted(WheelView wheelview)
            {
            }
        };
        curMonth = mili_month;
        month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addScrollingListener(listener);
        int i = mili_year;
        year.setViewAdapter(new DateNumericAdapter(this, minimum_year, 2500, i - minimum_year));
        year.setCurrentItem(i - minimum_year);
        year.addScrollingListener(listener);
        day.setCurrentItem(-1 + mili_day);
        sel_day = -1 + mili_day;
        day.setViewAdapter(new DateNumericAdapter(this, gstartday, gendday, sel_day));
        updateDays(year, month, day);
        day.setCurrentItem(-1 + mili_day);
        day.addScrollingListener(listener);
        hcurMonth = hijri_month;
        hmonth.setViewAdapter(new DateArrayAdapter(this, monthsh, hcurMonth));
        hmonth.setCurrentItem(hcurMonth);
        hmonth.addScrollingListener(listener);
        int j = hijri_year;
        hyear.setViewAdapter(new DateNumericAdapter(this, minimum_year, 2500, j - minimum_year));
        hyear.setCurrentItem(j - minimum_year);
        hyear.addScrollingListener(listener);
        hday.setCurrentItem(-1 + hijri_day);
        hsel_day = -1 + hijri_day;
        hday.setViewAdapter(new DateNumericAdapter(this, hstartday, hendday, hsel_day));
        hupdateDays(hyear, hmonth, hday);
        hday.setCurrentItem(-1 + hijri_day);
        hday.addScrollingListener(listener);
        String s = (new SimpleDateFormat("MMMM")).format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(5, 0);
        gregorianc_date.setText((new StringBuilder(String.valueOf(mili_day))).append("/").append(s).append("/").append(i).toString());
        hijric_date.setText((new StringBuilder(String.valueOf(hijri_day))).append("/").append(HijriCalendarDate.getSimpleDateMonth(calendar1, 0)).append("/").append(j).toString());
        LogUtils.i("hi created");
    }

    void setGregorianCalendar()
    {
        curMonth = mili_month;
        month.setCurrentItem(curMonth);
        int i = mili_year;
        year.setCurrentItem(i - minimum_year);
        day.setCurrentItem(-1 + mili_day);
        updateDays(year, month, day);
        day.setCurrentItem(-1 + mili_day);
    }

    void setHijriCalendar()
    {
        hcurMonth = hijri_month;
        hmonth.setCurrentItem(hcurMonth);
        int i = hijri_year;
        hyear.setCurrentItem(i - minimum_year);
        hday.setCurrentItem(-1 + hijri_day);
        hupdateDays(hyear, hmonth, hday);
        hday.setCurrentItem(-1 + hijri_day);
    }

    void updateDays(WheelView wheelview, WheelView wheelview1, WheelView wheelview2)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, calendar.get(1) + wheelview.getCurrentItem());
        calendar.set(2, wheelview1.getCurrentItem());
        int i = calendar.getActualMaximum(5);
        wheelview2.setViewAdapter(new DateNumericAdapter(this, gstartday, i, sel_day));
        wheelview2.setCurrentItem(-1 + Math.min(i, 1 + wheelview2.getCurrentItem()), true);
    }

    private class DateArrayAdapter extends ArrayWheelAdapter<String>
	{
	    int currentItem;
	    int currentValue;
	    
	    public DateArrayAdapter(Context paramContext, String[] paramArrayOfString, int paramInt)
	    {
	    	super(paramContext, paramArrayOfString);
	    	this.currentValue = paramInt;
	    	setTextSize(16);
	    }
	    
	    protected void configureTextView(TextView paramTextView)
	    {
	    	super.configureTextView(paramTextView);
	    	if (this.currentItem == this.currentValue)
	    	{
	    		paramTextView.setTextColor(Color.parseColor("#06310f"));
	    		paramTextView.setTypeface(DateConverter.this.tf1, 1);
	    	}else{
		      paramTextView.setTextColor(Color.parseColor("#73c484"));
		      paramTextView.setTypeface(DateConverter.this.tf1);
	    	}
	    }
	    
	    public View getItem(int paramInt, View paramView, ViewGroup paramViewGroup)
	    {
	    	this.currentItem = paramInt;
	    	return super.getItem(paramInt, paramView, paramViewGroup);
	    }
	}
    
    private class DateNumericAdapter extends NumericWheelAdapter
	{
	    int currentItem;
	    int currentValue;
	    
	    public DateNumericAdapter(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
	    {
	    	super(paramContext, paramInt1, paramInt2);
	    	this.currentValue = paramInt3;
	    	setTextSize(16);
	    }
	    
	    protected void configureTextView(TextView paramTextView)
	    {
	    	super.configureTextView(paramTextView);
	    	if (this.currentItem == this.currentValue)
	    	{
	    		paramTextView.setTextColor(Color.parseColor("#06310f"));
	    		paramTextView.setTypeface(DateConverter.this.tf1, 1);
	    	}else{
		    	paramTextView.setTextColor(Color.parseColor("#73c484"));
		    	paramTextView.setTypeface(DateConverter.this.tf1);
	    	}
	    }
	    
	    public View getItem(int paramInt, View paramView, ViewGroup paramViewGroup)
	    {
	    	this.currentItem = paramInt;
	    	return super.getItem(paramInt, paramView, paramViewGroup);
	    }
	}
}

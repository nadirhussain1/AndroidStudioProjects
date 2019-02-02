package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import com.sahaab.hijri.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import muslim.prayers.time.R;

public class ActivityCalender extends Utils {

	private static final String TAG_HIJRIDAY = "hijriday";
	public static AdapterCalender adapter;
	public static ArrayList date = new ArrayList();
	public static ArrayList event = new ArrayList();
	public static int i = 0;
	public static ListView lv_events;
	public static String mMonth = "";
	public static String mYear = "";
	public static ArrayList month = new ArrayList();
	public static String nyear = "";
	ArrayList arraylist;
	private CaldroidFragment caldroidFragment;
	String city_name[];
	boolean colorset;
	ArrayList eventlist;
	SharedPreferences sp;
	String type;
	Utils util;

	public ActivityCalender() {
		colorset = false;
		type = "country";
		arraylist = new ArrayList();
		eventlist = new ArrayList();
		util = new Utils();
	}

	private void setCustomResourceForDates() {
		Calendar calendar = Calendar.getInstance();
		nyear = (new StringBuilder()).append(calendar.get(1)).toString();
		// Prashant
		// calendar.set(5, 31);
		calendar.add(5, -1 * Integer.parseInt(sp.getString("hijriday", "0")));
		Date date1 = calendar.getTime();
		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.drawable.cell_bg1,
					date1);
			caldroidFragment.setTextColorForDate(R.color.white, date1);
		}
	}

	public void onCreate(Bundle bundle) {
		// fullscreen();
		super.onCreate(bundle);
		setContentView(R.layout.activity_calender);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		Actionbar(getString(R.string.lbl_calendar));
		Analytics(getString(R.string.lbl_calendar));
		typeface();
		banner_ad();
		lv_events = (ListView) findViewById(R.id.events);
		colorset = false;
		adapter = new AdapterCalender(this);
		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		caldroidFragment = new com.prayertimes.qibla.appsourcehub.activity.CaldroidFragment();
		FragmentTransaction fragmenttransaction;
		CaldroidListener caldroidlistener;
		if (bundle != null) {
			caldroidFragment.restoreStatesFromKey(bundle,
					"CALDROID_SAVED_STATE");
		} else {
			Bundle bundle1 = new Bundle();
			Calendar calendar = Calendar.getInstance();
			bundle1.putInt("month", 1 + calendar.get(2));
			bundle1.putInt("year", calendar.get(1));
			bundle1.putBoolean("enableSwipe", true);
			bundle1.putBoolean("sixWeeksInCalendar", true);
			caldroidFragment.setArguments(bundle1);
		}
		setCustomResourceForDates();
		fragmenttransaction = getSupportFragmentManager().beginTransaction();
		fragmenttransaction.replace(R.id.calendar1, caldroidFragment);
		fragmenttransaction.commit();
		caldroidlistener = new CaldroidListener() {
			public void onCaldroidViewCreated() {
				caldroidFragment.getLeftArrowButton();
			}

			public void onChangeMonth(int j, int k) {
			}

			public void onLongClickDate(Date date1, View view) {
			}

			public void onSelectDate(Date date1, View view) {
				Toast.makeText(ActivityCalender.this, formatter.format(date1),
						0).show();
			}
		};
		caldroidFragment.setCaldroidListener(caldroidlistener);
	}

	public void refresh() {
		(new Handler()).postDelayed(new Runnable() {
			public void run() {
				JSONArray jsonarray;
				try {
					String dateMonth = null;
					String dateYear = null;
					String findMotnthYear = null;
					String nYear = null;
					Date date = null;
					Calendar cal;
					InputStream inputstream = getAssets().open(
							"islamicevents.json");
					byte abyte0[] = new byte[inputstream.available()];
					inputstream.read(abyte0);
					inputstream.close();

					nYear = getDate(ActivityCalender.nyear);

					jsonarray = (new JSONObject(new String(abyte0)))
							.getJSONArray(nYear);
					ActivityCalender.date.clear();
					ActivityCalender.month.clear();
					ActivityCalender.event.clear();
					int j = 0;
					while (j < jsonarray.length()) {
						/* Prashant */

						if (ActivityCalender.mYear.startsWith("2")) {
							dateMonth = ActivityCalender.mYear.substring(4,
									ActivityCalender.mYear.length());
							dateYear = ActivityCalender.mYear.substring(0, 4);
							dateMonth = getFullDate(dateMonth);
							dateYear = getDate(dateYear);

						} else if (Locale.getDefault().toString()
								.equalsIgnoreCase("ar_EG")) {
							dateMonth = ActivityCalender.mYear.substring(0,
									ActivityCalender.mYear.indexOf(" "));
							dateYear = ActivityCalender.mYear
									.substring(ActivityCalender.mYear
											.indexOf(" "));
							dateMonth = getFullDate(dateMonth);
							dateYear = getDate(dateYear);
						} else {
							dateMonth = ActivityCalender.mYear.substring(0,
									ActivityCalender.mYear.indexOf("2"));
							dateYear = ActivityCalender.mYear
									.substring(ActivityCalender.mYear
											.lastIndexOf("2"));
							dateMonth = getFullDate(dateMonth);
							dateYear = getDate(dateYear);
						}
						/*
						 * if (Locale.getDefault().toString()
						 * .equalsIgnoreCase("zh_CN") ||
						 * Locale.getDefault().toString()
						 * .equalsIgnoreCase("zh_TW")) {
						 *
						 * }
						 */

						findMotnthYear = dateMonth + " " + dateYear;
						System.out.println("===Final Date: " + findMotnthYear);
						if (findMotnthYear.equals((new StringBuilder(String
								.valueOf(jsonarray.getJSONObject(j).getString(
										"month")))).append(" ").append(nYear)
								.toString())) {

							ActivityCalender.date.add(jsonarray
									.getJSONObject(j).getString("date"));
							ActivityCalender.month.add(jsonarray.getJSONObject(
									j).getString("month"));
							ActivityCalender.event.add(jsonarray.getJSONObject(
									j).getString("event"));
						}
						j++;
					}
					LogUtils.i((new StringBuilder(String
							.valueOf(ActivityCalender.event.size())))
							.append(" year ")
							.append(ActivityCalender.date.size())
							.append(" nyear ").append(ActivityCalender.nyear)
							.toString());
					if (ActivityCalender.i == ActivityCalender.i) {
						ActivityCalender.lv_events
								.setAdapter(ActivityCalender.adapter);
						return;
					}
					ActivityCalender.adapter.notifyDataSetChanged();
				} catch (IOException ioexception) {
					ioexception.printStackTrace();
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
			}
		}, 1000L);
	}

	private String getDate(String dateString) {
		// Prashant
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		formatter.setTimeZone(TimeZone.getDefault());
		Date value = null;
		try {
			value = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy",
				Locale.ENGLISH);
		String dt = dateFormatter.format(value);
		return dt;
	}

	private String getFullDate(String dateString) {
		// Prashant
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("MMM", Locale.getDefault())
					.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int monthInt = cal.get(Calendar.MONTH) + 1;
		DateFormatSymbols symbols = new DateFormatSymbols(Locale.ENGLISH);
		String[] monthNames = symbols.getMonths();
		String monthString = monthNames[monthInt - 1];
		return monthString;
	}

	public class AdapterCalender extends BaseAdapter {

		LayoutInflater inflater;
		TextView lblDate;
		TextView lblEndDate;
		TextView lblStartDate;
		LinearLayout lyt_listview;
		Context mContext;

		public int getCount() {
			return ActivityCalender.date.size();
		}

		public Object getItem(int j) {
			return null;
		}

		public long getItemId(int j) {
			return (long) j;
		}

		public View getView(int j, View view, ViewGroup viewgroup) {
			View view1 = inflater
					.inflate(R.layout.listview_item_calender, null);
			lyt_listview = (LinearLayout) view1.findViewById(R.id.lyt_listview);
			lblDate = (TextView) view1.findViewById(R.id.lblDate);
			lblStartDate = (TextView) view1.findViewById(R.id.lblStartDate);
			lblEndDate = (TextView) view1.findViewById(R.id.lblEndDate);
			LogUtils.i((new StringBuilder("json ")).append(
					(String) ActivityCalender.event.get(j)).toString());
			lblDate.setText((new StringBuilder(String
					.valueOf((String) ActivityCalender.date.get(j))))
					.append(" ").append((String) ActivityCalender.month.get(j))
					.append(" ").append(ActivityCalender.nyear).toString());
			lblStartDate.setText((CharSequence) ActivityCalender.month.get(j));
			lblEndDate.setText((CharSequence) ActivityCalender.event.get(j));
			return view1;
		}

		public AdapterCalender(Context context) {
			super();
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}
	}

}

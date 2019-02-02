package com.prayertimes.qibla.appsourcehub.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.sahaab.hijri.caldroid.CaldroidGridAdapter;
import com.sahaab.hijri.caldroid.CaldroidListener;
import com.sahaab.hijri.caldroid.CalendarHelper;
import com.sahaab.hijri.caldroid.DateGridFragment;
import com.sahaab.hijri.caldroid.MonthPagerAdapter;
import com.sahaab.hijri.caldroid.WeekdayArrayAdapter;
import com.sahaab.hijricalendar.HijriCalendarDate;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import muslim.prayers.time.R;

/**
 * Created by nadirhussain on 25/08/2016.
 */
public class CaldroidFragment extends DialogFragment {

    public static final String DIALOG_TITLE = "dialogTitle";
    public static final String DISABLE_DATES = "disableDates";
    public static final String ENABLE_CLICK_ON_DISABLED_DATES = "enableClickOnDisabledDates";
    public static final String ENABLE_SWIPE = "enableSwipe";
    public static final int FRIDAY = 6;
    public static final String MAX_DATE = "maxDate";
    public static final String MIN_DATE = "minDate";
    public static final int MONDAY = 2;
    public static final String MONTH = "month";
    private static final int MONTH_YEAR_FLAG = 52;
    public static final int NUMBER_OF_PAGES = 4;
    public static final int SATURDAY = 7;
    public static final String SELECTED_DATES = "selectedDates";
    public static final String SHOW_NAVIGATION_ARROWS = "showNavigationArrows";
    public static final String SIX_WEEKS_IN_CALENDAR = "sixWeeksInCalendar";
    public static final String START_DAY_OF_WEEK = "startDayOfWeek";
    public static final int SUNDAY = 1;
    private static final String TAG_HIJRIDAY = "hijriday";
    public static final int THURSDAY = 5;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final String YEAR = "year";
    public static final String _BACKGROUND_FOR_DATETIME_MAP = "_backgroundForDateTimeMap";
    public static final String _MAX_DATE_TIME = "_maxDateTime";
    public static final String _MIN_DATE_TIME = "_minDateTime";
    public static final String _TEXT_COLOR_FOR_DATETIME_MAP = "_textColorForDateTimeMap";
    public static final int disabledBackgroundDrawable = -1;
    public static final int disabledTextColor = 0xff888888;
    public static final int selectedBackgroundDrawable = -1;
    public static final int selectedTextColor = 0xff000000;
    public String TAG;
    protected HashMap backgroundForDateTimeMap;
    protected HashMap caldroidData;
    private CaldroidListener caldroidListener;
    protected ArrayList dateInMonthsList;
    private android.widget.AdapterView.OnItemClickListener dateItemClickListener;
    private android.widget.AdapterView.OnItemLongClickListener dateItemLongClickListener;
    protected ArrayList datePagerAdapters;
    private InfiniteViewPager dateViewPager;
    protected String dialogTitle;
    protected ArrayList disableDates;
    protected boolean enableClickOnDisabledDates;
    protected boolean enableSwipe;
    protected HashMap extraData;
    private Time firstMonthTime;
    private ArrayList fragments;
    private TextView hijrimonthTitleTextView;
    private ImageView leftArrowButton;
    protected DateTime maxDateTime;
    protected DateTime minDateTime;
    protected int month;
    private TextView monthTitleTextView;
    private TextView monthTitleTextView1;
    private Formatter monthYearFormatter;
    private final StringBuilder monthYearStringBuilder = new StringBuilder(
            50);
    private DatePageChangeListener pageChangeListener;
    private ImageView rightArrowButton;
    protected ArrayList selectedDates;
    protected boolean showNavigationArrows;
    private boolean sixWeeksInCalendar;
    SharedPreferences sp;
    protected int startDayOfWeek;
    protected HashMap textColorForDateTimeMap;
    private GridView weekdayGridView;
    protected int year;

    private android.widget.AdapterView.OnItemClickListener getDateItemClickListener() {
        if (dateItemClickListener == null) {
            dateItemClickListener = new android.widget.AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView adapterview, View view,
                                        int j, long l) {
                    DateTime datetime = (DateTime) dateInMonthsList.get(j);
                    if (caldroidListener == null
                            || !enableClickOnDisabledDates
                            && (minDateTime != null
                            && datetime.lt(minDateTime)
                            || maxDateTime != null
                            && datetime.gt(maxDateTime) || disableDates != null
                            && disableDates.indexOf(datetime) != -1)) {
                        return;
                    } else {
                        Date date1 = CalendarHelper
                                .convertDateTimeToDate(datetime);
                        caldroidListener.onSelectDate(date1, view);
                        return;
                    }
                }
            };
        }
        return dateItemClickListener;
    }

    private android.widget.AdapterView.OnItemLongClickListener getDateItemLongClickListener() {
        if (dateItemLongClickListener == null) {
            dateItemLongClickListener = new android.widget.AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView adapterview,
                                               View view, int j, long l) {
                    DateTime datetime = (DateTime) dateInMonthsList.get(j);
                    if (caldroidListener != null) {
                        if (!enableClickOnDisabledDates
                                && (minDateTime != null
                                && datetime.lt(minDateTime)
                                || maxDateTime != null
                                && datetime.gt(maxDateTime) || disableDates != null
                                && disableDates.indexOf(datetime) != -1)) {
                            return false;
                        }
                        Date date1 = CalendarHelper
                                .convertDateTimeToDate(datetime);
                        caldroidListener.onLongClickDate(date1, view);
                    }
                    return true;
                }
            };
        }
        return dateItemLongClickListener;
    }

    private void setupDateGridPages(View view) {
        DateTime datetime = new DateTime(Integer.valueOf(year),
                Integer.valueOf(month), Integer.valueOf(1),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0));
        pageChangeListener = new DatePageChangeListener();
        pageChangeListener.setCurrentDateTime(datetime);
        CaldroidGridAdapter caldroidgridadapter = getNewDatesGridAdapter(
                datetime.getMonth().intValue(), datetime.getYear()
                        .intValue());
        dateInMonthsList = caldroidgridadapter.getDatetimeList();
        DateTime datetime1 = datetime.plus(Integer.valueOf(0),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                hirondelle.date4j.DateTime.DayOverflow.LastDay);
        CaldroidGridAdapter caldroidgridadapter1 = getNewDatesGridAdapter(
                datetime1.getMonth().intValue(), datetime1.getYear()
                        .intValue());
        DateTime datetime2 = datetime1.plus(Integer.valueOf(0),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                hirondelle.date4j.DateTime.DayOverflow.LastDay);
        CaldroidGridAdapter caldroidgridadapter2 = getNewDatesGridAdapter(
                datetime2.getMonth().intValue(), datetime2.getYear()
                        .intValue());
        DateTime datetime3 = datetime.minus(Integer.valueOf(0),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                hirondelle.date4j.DateTime.DayOverflow.LastDay);
        CaldroidGridAdapter caldroidgridadapter3 = getNewDatesGridAdapter(
                datetime3.getMonth().intValue(), datetime3.getYear()
                        .intValue());
        datePagerAdapters.add(caldroidgridadapter);
        datePagerAdapters.add(caldroidgridadapter1);
        datePagerAdapters.add(caldroidgridadapter2);
        datePagerAdapters.add(caldroidgridadapter3);
        pageChangeListener.setCaldroidGridAdapters(datePagerAdapters);
        dateViewPager = (InfiniteViewPager) view
                .findViewById(R.id.months_infinite_pager);
        dateViewPager.setEnabled(enableSwipe);
        dateViewPager.setSixWeeksInCalendar(sixWeeksInCalendar);
        dateViewPager.setDatesInMonth(dateInMonthsList);
        MonthPagerAdapter monthpageradapter = new MonthPagerAdapter(
                getChildFragmentManager());
        fragments = monthpageradapter.getFragments();
        int j = 0;
        while (j < 4) {
            DateGridFragment dategridfragment = (DateGridFragment) fragments
                    .get(j);
            dategridfragment
                    .setGridAdapter((CaldroidGridAdapter) datePagerAdapters
                            .get(j));
            dategridfragment
                    .setOnItemClickListener(getDateItemClickListener());
            dategridfragment
                    .setOnItemLongClickListener(getDateItemLongClickListener());
            j++;
        }
        InfinitePagerAdapter infinitepageradapter = new InfinitePagerAdapter(
                monthpageradapter);
        dateViewPager.setAdapter(infinitepageradapter);
        dateViewPager.setOnPageChangeListener(pageChangeListener);
    }

    public void clearDisableDates() {
        disableDates.clear();
    }

    public void clearSelectedDates() {
        selectedDates.clear();
    }

    public HashMap getCaldroidData() {
        caldroidData.clear();
        caldroidData.put("disableDates", disableDates);
        caldroidData.put("selectedDates", selectedDates);
        caldroidData.put("_minDateTime", minDateTime);
        caldroidData.put("_maxDateTime", maxDateTime);
        caldroidData.put("startDayOfWeek", Integer.valueOf(startDayOfWeek));
        caldroidData.put("sixWeeksInCalendar",
                Boolean.valueOf(sixWeeksInCalendar));
        caldroidData.put("_backgroundForDateTimeMap",
                backgroundForDateTimeMap);
        caldroidData.put("_textColorForDateTimeMap",
                textColorForDateTimeMap);
        return caldroidData;
    }

    public CaldroidListener getCaldroidListener() {
        return caldroidListener;
    }

    public int getCurrentVirtualPosition() {
        int j = dateViewPager.getCurrentItem();
        return pageChangeListener.getCurrent(j);
    }

    public ArrayList getDatePagerAdapters() {
        return datePagerAdapters;
    }

    protected ArrayList getDaysOfWeek() {
        ArrayList arraylist1 = new ArrayList();
        if (startDayOfWeek == 1) {
            arraylist1.add("Sun");
            arraylist1.add("Mon");
            arraylist1.add("Tue");
            arraylist1.add("Wed");
            arraylist1.add("Thu");
            arraylist1.add("Fri");
            arraylist1.add("Sat");
            return arraylist1;
        } else {
            arraylist1.add("Mon");
            arraylist1.add("Tue");
            arraylist1.add("Wed");
            arraylist1.add("Thu");
            arraylist1.add("Fri");
            arraylist1.add("Sat");
            arraylist1.add("Sun");
            return arraylist1;
        }
    }

    public HashMap getExtraData() {
        return extraData;
    }

    public ArrayList getFragments() {
        return fragments;
    }

    public TextView getHijriMonthTitleTextView() {
        return monthTitleTextView;
    }

    public ImageView getLeftArrowButton() {
        return leftArrowButton;
    }

    public TextView getMonthTitleTextView() {
        return monthTitleTextView;
    }

    public TextView getMonthTitleTextView1() {
        return monthTitleTextView1;
    }

    public CaldroidGridAdapter getNewDatesGridAdapter(int j, int k) {
        return new CaldroidGridAdapter(getActivity(), j, k,
                getCaldroidData(), extraData);
    }

    public WeekdayArrayAdapter getNewWeekdayAdapter() {
        return new WeekdayArrayAdapter(getActivity(), 0x1090003,
                getDaysOfWeek());
    }

    public ImageView getRightArrowButton() {
        return rightArrowButton;
    }

    public Bundle getSavedStates() {
        Bundle bundle = new Bundle();
        bundle.putInt("month", month);
        bundle.putInt("year", year);
        if (dialogTitle != null) {
            bundle.putString("dialogTitle", dialogTitle);
        }
        if (selectedDates != null && selectedDates.size() > 0) {
            bundle.putStringArrayList("selectedDates",
                    CalendarHelper.convertToStringList(selectedDates));
        }
        if (disableDates != null && disableDates.size() > 0) {
            bundle.putStringArrayList("disableDates",
                    CalendarHelper.convertToStringList(disableDates));
        }
        if (minDateTime != null) {
            bundle.putString("minDate", minDateTime.format("YYYY-MM-DD"));
        }
        if (maxDateTime != null) {
            bundle.putString("maxDate", maxDateTime.format("YYYY-MM-DD"));
        }
        bundle.putBoolean("showNavigationArrows", showNavigationArrows);
        bundle.putBoolean("enableSwipe", enableSwipe);
        bundle.putInt("startDayOfWeek", startDayOfWeek);
        bundle.putBoolean("sixWeeksInCalendar", sixWeeksInCalendar);
        return bundle;
    }

    public GridView getWeekdayGridView() {
        return weekdayGridView;
    }

    public boolean isEnableSwipe() {
        return enableSwipe;
    }

    public boolean isShowNavigationArrows() {
        return showNavigationArrows;
    }

    public boolean isSixWeeksInCalendar() {
        return sixWeeksInCalendar;
    }

    public void moveToDate(Date date1) {
        moveToDateTime(CalendarHelper.convertDateToDateTime(date1));
    }

    public void moveToDateTime(DateTime datetime) {
        DateTime datetime1 = new DateTime(Integer.valueOf(year),
                Integer.valueOf(month), Integer.valueOf(1),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0));
        DateTime datetime2 = datetime1.getEndOfMonth();
        if (datetime.lt(datetime1)) {
            DateTime datetime4 = datetime.plus(Integer.valueOf(0),
                    Integer.valueOf(1), Integer.valueOf(0),
                    Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0), Integer.valueOf(0),
                    hirondelle.date4j.DateTime.DayOverflow.LastDay);
            pageChangeListener.setCurrentDateTime(datetime4);
            int k = dateViewPager.getCurrentItem();
            pageChangeListener.refreshAdapters(k);
            dateViewPager.setCurrentItem(k - 1);
        } else if (datetime.gt(datetime2)) {
            DateTime datetime3 = datetime.minus(Integer.valueOf(0),
                    Integer.valueOf(1), Integer.valueOf(0),
                    Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0), Integer.valueOf(0),
                    hirondelle.date4j.DateTime.DayOverflow.LastDay);
            pageChangeListener.setCurrentDateTime(datetime3);
            int j = dateViewPager.getCurrentItem();
            pageChangeListener.refreshAdapters(j);
            dateViewPager.setCurrentItem(j + 1);
            return;
        }
    }

    public CaldroidFragment newInstance(String s, int j, int k) {
        CaldroidFragment caldroidfragment = new CaldroidFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", s);
        bundle.putInt("month", j);
        bundle.putInt("year", k);
        caldroidfragment.setArguments(bundle);
        return caldroidfragment;
    }

    public void nextMonth() {
        dateViewPager.setCurrentItem(1 + pageChangeListener
                .getCurrentPage());
    }

    public View onCreateView(LayoutInflater layoutinflater,
                             ViewGroup viewgroup, Bundle bundle) {
        retrieveInitialArgs();
        if (getDialog() != null) {
            setRetainInstance(true);
        }
        View view = layoutinflater.inflate(R.layout.calendar_view, viewgroup, false);
        LinearLayout _tmp = (LinearLayout) view
                .findViewById(R.id.calendarv);
        getActivity().getSharedPreferences("pref_prayer", 0);
        hijrimonthTitleTextView = (TextView) view
                .findViewById(R.id.hijri_month_year_textview);
        monthTitleTextView = (TextView) view
                .findViewById(R.id.calendar_month_year_textview);
        monthTitleTextView1 = (TextView) view
                .findViewById(R.id.month_year_textview);
        leftArrowButton = (ImageView) view
                .findViewById(R.id.calendar_left_arrow);
        rightArrowButton = (ImageView) view
                .findViewById(R.id.calendar_right_arrow);
        leftArrowButton
                .setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(View view) {
                        prevMonth();
                    }
                });
        rightArrowButton
                .setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(View view) {
                        nextMonth();
                    }
                });
        setShowNavigationArrows(showNavigationArrows);
        weekdayGridView = (GridView) view
                .findViewById(R.id.weekday_gridview);
        WeekdayArrayAdapter weekdayarrayadapter = getNewWeekdayAdapter();
        weekdayGridView.setAdapter(weekdayarrayadapter);
        setupDateGridPages(view);
        refreshView();
        if (caldroidListener != null) {
            caldroidListener.onCaldroidViewCreated();
        }
        return view;
    }

    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public void onDetach() {
        super.onDetach();
        try {
            Field field = android.support.v4.app.Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            field.setAccessible(true);
            field.set(this, null);
            return;
        } catch (NoSuchFieldException nosuchfieldexception) {
            throw new RuntimeException(nosuchfieldexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new RuntimeException(illegalaccessexception);
        }
    }

    public void prevMonth() {
        dateViewPager.setCurrentItem(-1
                + pageChangeListener.getCurrentPage());
    }

    protected void refreshMonthTitleTextView() {
        firstMonthTime.year = year;
        firstMonthTime.month = -1 + month;
        firstMonthTime.monthDay = 1;
        long l = firstMonthTime.toMillis(true);
        monthYearStringBuilder.setLength(0);
        String s = DateUtils.formatDateRange(getActivity(),
                monthYearFormatter, l, l, 52).toString();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, Integer.parseInt(sp.getString("hijriday", "0")));
        String s1 = (new StringBuilder(String.valueOf(HijriCalendarDate
                .getSimpleDateDay(calendar, 0)))).append(" ")
                .append(HijriCalendarDate.getSimpleDateMonth(calendar, 0))
                .append(" ")
                .append(HijriCalendarDate.getSimpleDateYear(calendar, 0))
                .toString();
        long l1 = System.currentTimeMillis();
        String s2 = (new SimpleDateFormat("d MMMM yyyy")).format(Long
                .valueOf(l1));
        monthTitleTextView.setText(s2);
        hijrimonthTitleTextView.setText(s1);
        // ActivityCalender.nyear = s.split(" ")[1].trim();
        monthTitleTextView1.setText(s);
        ActivityCalender.mYear = s;
        ActivityCalender.i = 1 + ActivityCalender.i;
        ((ActivityCalender)getActivity()).refresh();
        LogUtils.i((new StringBuilder("montitle ")).append(s).toString());
    }

    public void refreshView() {
        if (month != -1 && year != -1) {
            refreshMonthTitleTextView();
            Iterator iterator = datePagerAdapters.iterator();
            while (iterator.hasNext()) {
                CaldroidGridAdapter caldroidgridadapter = (CaldroidGridAdapter) iterator
                        .next();
                caldroidgridadapter.setCaldroidData(getCaldroidData());
                caldroidgridadapter.setExtraData(extraData);
                caldroidgridadapter.updateToday();
                caldroidgridadapter.notifyDataSetChanged();
            }
        }
    }

    public void restoreDialogStatesFromKey(FragmentManager fragmentmanager,
                                           Bundle bundle, String s, String s1) {
        restoreStatesFromKey(bundle, s);
        CaldroidFragment caldroidfragment = (CaldroidFragment) fragmentmanager
                .findFragmentByTag(s1);
        if (caldroidfragment != null) {
            caldroidfragment.dismiss();
            show(fragmentmanager, s1);
        }
    }

    public void restoreStatesFromKey(Bundle bundle, String s) {
        if (bundle != null && bundle.containsKey(s)) {
            setArguments(bundle.getBundle(s));
        }
    }

    protected void retrieveInitialArgs() {
        DateTime datetime;
        ArrayList arraylist1;
        ArrayList arraylist2;
        String s;
        String s1;
        Iterator iterator;
        Iterator iterator1;
        Bundle bundle = getArguments();
        if (bundle != null) {
            month = bundle.getInt("month", -1);
            year = bundle.getInt("year", -1);
            dialogTitle = bundle.getString("dialogTitle");
            Dialog dialog = getDialog();
            if (dialog != null) {
                if (dialogTitle != null) {
                    dialog.setTitle(dialogTitle);
                } else {
                    dialog.requestWindowFeature(1);
                }
            }
            startDayOfWeek = bundle.getInt("startDayOfWeek", 1);
            if (startDayOfWeek > 7) {
                startDayOfWeek = startDayOfWeek % 7;
            }
            showNavigationArrows = bundle.getBoolean(
                    "showNavigationArrows", true);
            enableSwipe = bundle.getBoolean("enableSwipe", true);
            sixWeeksInCalendar = bundle.getBoolean("sixWeeksInCalendar",
                    true);
            enableClickOnDisabledDates = bundle.getBoolean(
                    "enableClickOnDisabledDates", false);
            arraylist1 = bundle.getStringArrayList("disableDates");
            if (!(arraylist1 == null || arraylist1.size() <= 0)) {
                disableDates.clear();
                iterator1 = arraylist1.iterator();
                while (iterator1.hasNext()) {
                    DateTime datetime2 = CalendarHelper
                            .getDateTimeFromString(
                                    (String) iterator1.next(), "yyyy-MM-dd");
                    disableDates.add(datetime2);
                }
            }
            arraylist2 = bundle.getStringArrayList("selectedDates");
            if (!(arraylist2 == null || arraylist2.size() <= 0)) {
                selectedDates.clear();
                iterator = arraylist2.iterator();
                while (iterator.hasNext()) {
                    DateTime datetime1 = CalendarHelper
                            .getDateTimeFromString(
                                    (String) iterator.next(), "yyyy-MM-dd");
                    selectedDates.add(datetime1);
                }
            }
            s = bundle.getString("minDate");
            if (s != null) {
                minDateTime = CalendarHelper.getDateTimeFromString(s, null);
            }
            s1 = bundle.getString("maxDate");
            if (s1 != null) {
                maxDateTime = CalendarHelper
                        .getDateTimeFromString(s1, null);
            }
        }
        if (month == -1 || year == -1) {
            datetime = DateTime.today(TimeZone.getDefault());
            month = datetime.getMonth().intValue();
            year = datetime.getYear().intValue();
        }
    }

    public void saveStatesToKey(Bundle bundle, String s) {
        bundle.putBundle(s, getSavedStates());
    }

    public void setBackgroundResourceForDate(int j, Date date1) {
        DateTime datetime = CalendarHelper.convertDateToDateTime(date1);
        backgroundForDateTimeMap.put(datetime, Integer.valueOf(j));
    }

    public void setBackgroundResourceForDateTime(int j, DateTime datetime) {
        backgroundForDateTimeMap.put(datetime, Integer.valueOf(j));
    }

    public void setBackgroundResourceForDateTimes(HashMap hashmap) {
        backgroundForDateTimeMap.putAll(hashmap);
    }

    public void setBackgroundResourceForDates(HashMap hashmap) {
        if (hashmap != null && hashmap.size() != 0) {
            backgroundForDateTimeMap.clear();
            Iterator iterator = hashmap.keySet().iterator();
            while (iterator.hasNext()) {
                Date date1 = (Date) iterator.next();
                Integer integer = (Integer) hashmap.get(date1);
                DateTime datetime = CalendarHelper
                        .convertDateToDateTime(date1);
                backgroundForDateTimeMap.put(datetime, integer);
            }
        }
    }

    public void setCaldroidListener(CaldroidListener caldroidlistener) {
        caldroidListener = caldroidlistener;
    }

    public void setCalendarDate(Date date1) {
        setCalendarDateTime(CalendarHelper.convertDateToDateTime(date1));
    }

    public void setCalendarDateTime(DateTime datetime) {
        month = datetime.getMonth().intValue();
        year = datetime.getYear().intValue();
        if (caldroidListener != null) {
            caldroidListener.onChangeMonth(month, year);
        }
        refreshView();
    }

    public void setDisableDates(ArrayList arraylist1) {
        if (arraylist1 != null && arraylist1.size() != 0) {
            disableDates.clear();
            Iterator iterator = arraylist1.iterator();
            while (iterator.hasNext()) {
                DateTime datetime = CalendarHelper
                        .convertDateToDateTime((Date) iterator.next());
                disableDates.add(datetime);
            }
        }
    }

    public void setDisableDatesFromString(ArrayList arraylist1) {
        setDisableDatesFromString(arraylist1, null);
    }

    public void setDisableDatesFromString(ArrayList arraylist1, String s) {
        if (arraylist1 != null) {
            disableDates.clear();
            Iterator iterator = arraylist1.iterator();
            while (iterator.hasNext()) {
                DateTime datetime = CalendarHelper.getDateTimeFromString(
                        (String) iterator.next(), s);
                disableDates.add(datetime);
            }
        }
    }

    public void setEnableSwipe(boolean flag) {
        enableSwipe = flag;
        dateViewPager.setEnabled(flag);
    }

    public void setExtraData(HashMap hashmap) {
        extraData = hashmap;
    }

    public void setHijriMonthTitleTextView(TextView textview) {
        hijrimonthTitleTextView = textview;
    }

    public void setMaxDate(Date date1) {
        if (date1 == null) {
            maxDateTime = null;
            return;
        } else {
            maxDateTime = CalendarHelper.convertDateToDateTime(date1);
            return;
        }
    }

    public void setMaxDateFromString(String s, String s1) {
        if (s == null) {
            setMaxDate(null);
            return;
        } else {
            maxDateTime = CalendarHelper.getDateTimeFromString(s, s1);
            return;
        }
    }

    public void setMinDate(Date date1) {
        if (date1 == null) {
            minDateTime = null;
            return;
        } else {
            minDateTime = CalendarHelper.convertDateToDateTime(date1);
            return;
        }
    }

    public void setMinDateFromString(String s, String s1) {
        if (s == null) {
            setMinDate(null);
            return;
        } else {
            minDateTime = CalendarHelper.getDateTimeFromString(s, s1);
            return;
        }
    }

    public void setMonthTitleTextView(TextView textview) {
        monthTitleTextView = textview;
    }

    public void setMonthTitleTextView1(TextView textview) {
        monthTitleTextView1 = textview;
    }

    public void setSelectedDateStrings(String s, String s1, String s2)
            throws ParseException {
        setSelectedDates(CalendarHelper.getDateFromString(s, s2),
                CalendarHelper.getDateFromString(s1, s2));
    }

    public void setSelectedDates(Date date1, Date date2) {
        if (date1 == null || date2 == null || date1.after(date2)) {
            return;
        }
        selectedDates.clear();
        DateTime datetime = CalendarHelper.convertDateToDateTime(date1);
        DateTime datetime1 = CalendarHelper.convertDateToDateTime(date2);
        DateTime datetime2 = datetime;
        do {
            if (!datetime2.lt(datetime1)) {
                selectedDates.add(datetime1);
                return;
            }
            selectedDates.add(datetime2);
            datetime2 = datetime2.plusDays(Integer.valueOf(1));
        } while (true);
    }

    public void setShowNavigationArrows(boolean flag) {
        showNavigationArrows = flag;
        if (flag) {
            leftArrowButton.setVisibility(0);
            rightArrowButton.setVisibility(0);
            return;
        } else {
            leftArrowButton.setVisibility(4);
            rightArrowButton.setVisibility(4);
            return;
        }
    }

    public void setSixWeeksInCalendar(boolean flag) {
        sixWeeksInCalendar = flag;
        dateViewPager.setSixWeeksInCalendar(flag);
    }

    public void setTextColorForDate(int j, Date date1) {
        DateTime datetime = CalendarHelper.convertDateToDateTime(date1);
        textColorForDateTimeMap.put(datetime, Integer.valueOf(j));
    }

    public void setTextColorForDateTime(int j, DateTime datetime) {
        textColorForDateTimeMap.put(datetime, Integer.valueOf(j));
    }

    public void setTextColorForDateTimes(HashMap hashmap) {
        textColorForDateTimeMap.putAll(hashmap);
    }

    public void setTextColorForDates(HashMap hashmap) {
        if (hashmap != null && hashmap.size() != 0) {
            textColorForDateTimeMap.clear();
            Iterator iterator = hashmap.keySet().iterator();
            while (iterator.hasNext()) {
                Date date1 = (Date) iterator.next();
                Integer integer = (Integer) hashmap.get(date1);
                DateTime datetime = CalendarHelper
                        .convertDateToDateTime(date1);
                textColorForDateTimeMap.put(datetime, integer);
            }
        }
    }

    public CaldroidFragment() {
        super();
        TAG = "CaldroidFragment";
        firstMonthTime = new Time();
        monthYearFormatter = new Formatter(monthYearStringBuilder,
                Locale.ENGLISH);
        month = -1;
        year = -1;
        disableDates = new ArrayList();
        selectedDates = new ArrayList();
        caldroidData = new HashMap();
        extraData = new HashMap();
        backgroundForDateTimeMap = new HashMap();
        textColorForDateTimeMap = new HashMap();
        startDayOfWeek = 1;
        sixWeeksInCalendar = true;
        datePagerAdapters = new ArrayList();
        enableSwipe = true;
        showNavigationArrows = true;
        enableClickOnDisabledDates = false;
    }

    public class DatePageChangeListener implements
            android.support.v4.view.ViewPager.OnPageChangeListener {

        private ArrayList caldroidGridAdapters;
        private DateTime currentDateTime;
        private int currentPage;

        private int getNext(int j) {
            return (j + 1) % 4;
        }

        private int getPrevious(int j) {
            return (j + 3) % 4;
        }

        public ArrayList getCaldroidGridAdapters() {
            return caldroidGridAdapters;
        }

        public int getCurrent(int j) {
            return j % 4;
        }

        public DateTime getCurrentDateTime() {
            return currentDateTime;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void onPageScrollStateChanged(int j) {
        }

        public void onPageScrolled(int j, float f, int k) {
        }

        public void onPageSelected(int j) {
            refreshAdapters(j);
            setCalendarDateTime(currentDateTime);
            CaldroidGridAdapter caldroidgridadapter = (CaldroidGridAdapter) caldroidGridAdapters
                    .get(j % 4);
            dateInMonthsList.clear();
            dateInMonthsList.addAll(caldroidgridadapter.getDatetimeList());
        }

        public void refreshAdapters(int j) {
            CaldroidGridAdapter caldroidgridadapter = (CaldroidGridAdapter) caldroidGridAdapters
                    .get(getCurrent(j));
            CaldroidGridAdapter caldroidgridadapter1 = (CaldroidGridAdapter) caldroidGridAdapters
                    .get(getPrevious(j));
            CaldroidGridAdapter caldroidgridadapter2 = (CaldroidGridAdapter) caldroidGridAdapters
                    .get(getNext(j));
            if (j == currentPage) {
                caldroidgridadapter.setAdapterDateTime(currentDateTime);
                caldroidgridadapter.notifyDataSetChanged();
                caldroidgridadapter1
                        .setAdapterDateTime(currentDateTime.minus(
                                Integer.valueOf(0),
                                Integer.valueOf(1),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                hirondelle.date4j.DateTime.DayOverflow.LastDay));
                caldroidgridadapter1.notifyDataSetChanged();
                caldroidgridadapter2
                        .setAdapterDateTime(currentDateTime.plus(
                                Integer.valueOf(0),
                                Integer.valueOf(1),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                hirondelle.date4j.DateTime.DayOverflow.LastDay));
                caldroidgridadapter2.notifyDataSetChanged();
            } else if (j > currentPage) {
                currentDateTime = currentDateTime.plus(Integer.valueOf(0),
                        Integer.valueOf(1), Integer.valueOf(0),
                        Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0), Integer.valueOf(0),
                        hirondelle.date4j.DateTime.DayOverflow.LastDay);
                caldroidgridadapter2
                        .setAdapterDateTime(currentDateTime.plus(
                                Integer.valueOf(0),
                                Integer.valueOf(1),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                hirondelle.date4j.DateTime.DayOverflow.LastDay));
                caldroidgridadapter2.notifyDataSetChanged();
            } else {
                currentDateTime = currentDateTime.minus(Integer.valueOf(0),
                        Integer.valueOf(1), Integer.valueOf(0),
                        Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0), Integer.valueOf(0),
                        hirondelle.date4j.DateTime.DayOverflow.LastDay);
                caldroidgridadapter1
                        .setAdapterDateTime(currentDateTime.minus(
                                Integer.valueOf(0),
                                Integer.valueOf(1),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                hirondelle.date4j.DateTime.DayOverflow.LastDay));
                caldroidgridadapter1.notifyDataSetChanged();
            }
            currentPage = j;
        }

        public void setCaldroidGridAdapters(ArrayList arraylist1) {
            caldroidGridAdapters = arraylist1;
        }

        public void setCurrentDateTime(DateTime datetime) {
            currentDateTime = datetime;
            setCalendarDateTime(currentDateTime);
        }

        public void setCurrentPage(int j) {
            currentPage = j;
        }

        public DatePageChangeListener() {
            super();
            currentPage = 1000;
        }
    }
}




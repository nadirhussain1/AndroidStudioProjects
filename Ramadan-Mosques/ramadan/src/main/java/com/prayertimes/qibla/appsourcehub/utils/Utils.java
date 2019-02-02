package com.prayertimes.qibla.appsourcehub.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.prayertimes.qibla.appsourcehub.support.PrayTime;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import muslim.prayers.time.R;

public class Utils extends ActionBarActivity
        implements LocationListener {
    public Activity mactivity;
    public static boolean isAdloaded = false;
    private static final int DAYS_UNTIL_PROMPT = 0;
    static final String EXTRA_MESSAGE = "message";
    public static final boolean IS_DEBUG = true;
    private static final int LAUNCHES_UNTIL_PROMPT = 5;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5L;
    private static final long MIN_TIME_BW_UPDATES = 30000L;
    static int MODE_PRIVATE = 0;
    public static final boolean SHOULD_PRINT_LOG = true;
    public static String SharedPref_filename = "Prayer_pref";
    public static final String USER_COMPASS = "user_compass";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_name";
    public static final String USER_PHONE = "user_phone";
    private static GoogleAnalytics analytics;
    public static int startadInterval = 20000;
    public static int timer;
    public final String EXTRA_ACTIVITY = "activityFlag";
    public final String EXTRA_ACTIVITY_HOME = "activityHome";
    public static final String USER_CITY="user_city";
    public String USER_COUNTRY;
    public String USER_LAT;
    public String USER_LNG;
    public String USER_MLAT;
    public String USER_MLNG;
    public String USER_STATE;
    public String USER_STREET;
    public String USER_THEME;
    public final String UTILS_NOTIF = "notif";
    protected MenuItem action_qibla;
    protected int adInterval;
    AdRequest adReq;
    protected int afont;
    Intent alarmIntent_1;
    protected String ar_font;
    boolean canGetLocation;
    android.content.SharedPreferences.Editor editor;
    protected int efont;
    protected String en_font;
    protected Handler handler_ad;
    InterstitialAd interstitial;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    protected String notify;
    String p_name_2[] = {
            "Imsak", "Fajar", "Dhuhr", "Asr", "Maghrib", "Isha"
    };
    String p_time_2[];
    private PendingIntent pendingIntent_1;
    protected int pos;
    SharedPreferences pref;
    protected String pref_time;
    protected Runnable runnable_ad;
    int serverResponseCode;
    SharedPreferences spref;
    protected int style[] = {
            R.style.FontStyle_Small, R.style.FontStyle_Medium, R.style.FontStyle_Large
    };
    protected int styleheader[] = {
            R.style.FontStyle_title_small, R.style.FontStyle_title_medium, R.style.FontStyle_title_large
    };
    protected Typeface tf;
    protected Typeface tf1;
    protected Typeface tf2;
    protected Typeface tf3;
    protected Typeface tf_arabic;

    public Utils() {
        mactivity = this;
        USER_COUNTRY = "user_country";
        USER_STATE = "user_state";
        USER_LAT = "user_lat";
        USER_LNG = "user_lng";
        USER_MLAT = "user_mlat";
        USER_MLNG = "user_mlng";
        USER_STREET = "user_street";
        USER_THEME = "user_theme";
        serverResponseCode = 0;
        isGPSEnabled = false;
        isNetworkEnabled = false;
        canGetLocation = false;
        ar_font = "1";
        en_font = "1";
        pref_time = "0";
        notify = "0";
        afont = 1;
        efont = 1;
        handler_ad = new Handler();
        pos = 0;
    }

    public static double bearing(double d, double d1, double d2, double d3) {
        double d4 = Math.toRadians(d);
        double d5 = Math.toRadians(d2);
        double d6 = Math.toRadians(d3 - d1);
        return radToBearing(Math.atan2(Math.sin(d6) * Math.cos(d5), Math.cos(d4) * Math.sin(d5) - Math.sin(d4) * Math.cos(d5) * Math.cos(d6)));
    }

    public static String getRealPathFromURI(Uri uri, Activity activity) {
        if (uri != null && "content".equals(uri.getScheme())) {
            Cursor cursor = activity.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            cursor.moveToFirst();
            String s = cursor.getString(0);
            cursor.close();
            return s;
        } else {
            return uri.getPath();
        }
    }

    public static void printException(Exception exception) {
        exception.printStackTrace();
    }

    public static double radToBearing(double d) {
        return (360D + Math.toDegrees(d)) % 360D;
    }

    public static int randInt(int i, int j) {
        return 10000 + (new Random(System.currentTimeMillis())).nextInt(20000);
    }

    public void Actionbar(String s) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SpannableString spannablestring = new SpannableString(s);
        spannablestring.setSpan(new TypefaceSpan("MyTypeface.otf"), 0, spannablestring.length(), 33);
        getSupportActionBar().setTitle(Html.fromHtml((new StringBuilder("<font color=\"#ffffff\">")).append(spannablestring).append("</font>").toString()));
        android.graphics.drawable.Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    public void Alert(String s, String s1) {
        (new com.afollestad.materialdialogs.MaterialDialog.Builder(this)).title(s).content(s1).cancelable(false).positiveText("Ok").positiveColor(Color.parseColor("#379e24")).negativeText(0x1040000).negativeColor(Color.parseColor("#379e24")).callback(new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
            public void onPositive(MaterialDialog materialdialog) {
                super.onPositive(materialdialog);
            }
        }).build().show();
    }

    public  void Analytics(String eventName) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, eventName);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Screen Event");

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

    }

    public String LoadPref(String s) {
        spref = getSharedPreferences(SharedPref_filename, MODE_PRIVATE);
        return spref.getString(s, "");
    }

    public Boolean LoadPrefBool(String s) {
        spref = getSharedPreferences(SharedPref_filename, MODE_PRIVATE);
        return Boolean.valueOf(spref.getBoolean(s, false));
    }

    public int LoadPrefInt(String s) {
        spref = getSharedPreferences(SharedPref_filename, MODE_PRIVATE);
        return spref.getInt(s, 0);
    }

    public String LoadStringPref(String s) {
        spref = getSharedPreferences(SharedPref_filename, MODE_PRIVATE);
        return spref.getString(s, "0");
    }

    public void Rate() {
        (new com.afollestad.materialdialogs.MaterialDialog.Builder(this)).title(R.string.rate_title).positiveText(R.string.rate_agree).negativeText(R.string.rate_disagree).callback(new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
            public void onNegative(MaterialDialog materialdialog) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(0x4000000);
                startActivity(intent);
                finish();
                System.exit(0);
            }

            public void onPositive(MaterialDialog materialdialog) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.play_rate_url)));
                startActivity(intent);
                saveBoolean("rated", true);
            }
        }).show();
    }

    public void SavePref(String s, String s1) {
        spref = getSharedPreferences(SharedPref_filename, MODE_PRIVATE);
        editor = spref.edit();
        editor.putString(s, s1);
        editor.commit();
    }

    public void SavePrefBool(String s, boolean flag) {
        spref = getSharedPreferences(SharedPref_filename, MODE_PRIVATE);
        editor = spref.edit();
        editor.putBoolean(s, flag);
        editor.commit();
    }

    public void SavePrefInt(String s, int i) {
        spref = getSharedPreferences(SharedPref_filename, MODE_PRIVATE);
        editor = spref.edit();
        editor.putInt(s, i);
        editor.commit();
    }

    public String addAsrLight(String s, int i)
            throws ParseException {
        SimpleDateFormat simpledateformat;
        Calendar calendar;
        if (pref_time.equals("0")) {
            simpledateformat = new SimpleDateFormat("hh:mm a", Locale.US);
        } else {
            simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
        }
        calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(10, 1);
        calendar.add(12, 6);
        return simpledateformat.format(calendar.getTime());
    }

    public String addDayLight(String s, int i)
            throws ParseException {
        SimpleDateFormat simpledateformat;
        Calendar calendar;
        if (pref_time.equals("0")) {
            simpledateformat = new SimpleDateFormat("hh:mm a", Locale.US);
        } else {
            simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
        }
        calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(12, i);
        return simpledateformat.format(calendar.getTime());
    }

    public String addDay_Light(String s, int i)
            throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(12, i);
        return simpledateformat.format(calendar.getTime());
    }

    public String addMidDayLight(String s, int i)
            throws ParseException {
        SimpleDateFormat simpledateformat;
        Calendar calendar;
        if (pref_time.equals("0")) {
            simpledateformat = new SimpleDateFormat("hh:mm a", Locale.US);
        } else {
            simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
        }
        calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(10, 12);
        calendar.add(12, i);
        return simpledateformat.format(calendar.getTime());
    }

    public String addMid_DayLight(String s, int i)
            throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(10, 12);
        calendar.add(12, i);
        return simpledateformat.format(calendar.getTime());
    }

    public void banner_ad() {
        ((AdView) findViewById(R.id.adMob1)).loadAd((new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice("937A71190C8F67F9D97F6932B7E2D68E").addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB").addTestDevice("8409E0302A168D0C1760462AB5CA3D1D").addTestDevice("").build());
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }

    public void cancel() {
        if (p_time_2.length > 0) {
            int i = 0;
            while (i < p_time_2.length) {
                pendingIntent_1 = PendingIntent.getBroadcast(this, i, alarmIntent_1, 0);
                ((AlarmManager) getSystemService("alarm")).cancel(pendingIntent_1);
                i++;
            }
        }
    }

    public void cancel_all_alarm(Context context) {
        Intent intent = new Intent(context, com.prayertimes.qibla.appsourcehub.receiver.AlarmReceiver.class);
        int i = 0;
        do {
            if (i > 5) {
                return;
            }
            PendingIntent pendingintent = PendingIntent.getBroadcast(context, i, intent, 0);
            ((AlarmManager) context.getSystemService("alarm")).cancel(pendingintent);
            i++;
        } while (true);
    }

    public String changeTimeFormat(String s)
            throws ParseException {
        SimpleDateFormat simpledateformat;
        SimpleDateFormat simpledateformat1;
        if (pref_time.equals("0")) {
            simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
            simpledateformat1 = new SimpleDateFormat("hh:mm a", Locale.US);
        } else {
            simpledateformat = new SimpleDateFormat("HH:mm", Locale.US);
            simpledateformat1 = new SimpleDateFormat("hh:mm", Locale.US);
        }
        return simpledateformat.format(simpledateformat1.parse(s));
    }

    public void disableAllAlarm(Context context) {
        saveBoolean("alarm_fajar", false);
        saveBoolean("alarm_shorook", false);
        saveBoolean("alarm_zuheer", false);
        saveBoolean("alarm_asar", false);
        saveBoolean("alarm_maghrib", false);
        saveBoolean("alarm_isha", false);
    }

    public void font() {
        alarmIntent_1 = new Intent(this, com.prayertimes.qibla.appsourcehub.receiver.NotificationReceiver.class);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        ar_font = pref.getString("arabicfont", "1");
        en_font = pref.getString("englishfont", "1");
        notify = pref.getString("notification", "0");
        if (en_font.equals("0")) {
            efont = 0;
        } else if (en_font.equals("1")) {
            efont = 1;
        } else if (en_font.equals("2")) {
            efont = 2;
        }
        if (ar_font.equals("0")) {
            afont = 0;
        } else if (ar_font.equals("1")) {
            afont = 1;
        } else if (ar_font.equals("2")) {
            afont = 2;
        }
        pref_time = pref.getString("timeformat", "0");
        LogUtils.i((new StringBuilder("notification ")).append(pref_time).toString());
    }

    public void fullscreen() {
        //getWindow().setFlags(1024, 1024);
    }



    public Calendar getCalender(int i)
            throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String s = simpledateformat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(5, i);
        return calendar;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getSystemService("location");
            isGPSEnabled = locationManager.isProviderEnabled("gps");
            isNetworkEnabled = locationManager.isProviderEnabled("network");
            Log.d("Network", (new StringBuilder(String.valueOf(isNetworkEnabled))).append(" Network ").append(isGPSEnabled).toString());
            if (isNetworkEnabled) {
                canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates("network", 30000L, 5F, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation("network");
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            } else if (isGPSEnabled && location == null) {
                locationManager.requestLocationUpdates("gps", 30000L, 5F, this);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation("gps");
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
            Log.d("Network", (new StringBuilder(String.valueOf(latitude))).append(" Network ").append(longitude).toString());
        } catch (Exception e) {
        }
        return location;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String s = cursor.getString(0);
        String s1 = s.substring(1 + s.lastIndexOf(":"));
        cursor.close();
        Cursor cursor1 = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_id = ? ", new String[]{
                s1
        }, null);
        cursor1.moveToFirst();
        String s2 = cursor1.getString(cursor1.getColumnIndex("_data"));
        cursor1.close();
        return s2;
    }

    public String getTime(int i)
            throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse("June 18, 2015"));
        calendar.add(5, i);
        return simpledateformat.format(calendar.getTime());
    }

    public Calendar getTimeByCal(int i)
            throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String s = simpledateformat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(5, i);
        return calendar;
    }

    public String getTimeDate(int i)
            throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("hh:mm a MMMM d, yyyy", Locale.US);
        String s = simpledateformat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(5, i);
        return simpledateformat.format(calendar.getTime());
    }

    public String getTimeNow(int i)
            throws ParseException {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String s = simpledateformat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpledateformat.parse(s));
        calendar.add(5, i);
        return simpledateformat.format(calendar.getTime());
    }

    public double getTimeZone(String s) {
        int i = TimeZone.getTimeZone(s).getRawOffset() / 60000;
        int j = i / 60;
        int k = i % 60;
        StringBuilder stringbuilder = (new StringBuilder(String.valueOf(j))).append(".");
        byte byte0;
        if (k == 30) {
            byte0 = 5;
        } else {
            byte0 = 0;
        }
        return Double.parseDouble(stringbuilder.append(byte0).toString());
    }

    public void initPrayerTimes(int i) throws ParseException {
        if (loadString(USER_LAT).equals("0"))
            return;
        ArrayList arraylist;
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
        if (s.equals("")) {
            d2 = getTimeZone(calendar.getTimeZone().getID().toString());
            editor = pref.edit();
            editor.putString("timezone", s);
            editor.commit();
        } else {
            d2 = getTimeZone(s);
        }
        arraylist = praytime.getPrayerTimes(calendar, d, d1, d2);
        praytime.getTimeNames();
        if (d == 0.0D || d1 == 0.0D)
            return;
        Integer.parseInt(pref.getString("daylight", "0"));
        j = 0;
        while (j < arraylist.size()) {
            String as[] = new String[6];
            as[0] = addDayLight((String) arraylist.get(0), -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[0]))).append("daylight").toString(), "0")));
            as[1] = addDayLight((String) arraylist.get(0), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[1]))).append("daylight").toString(), "0")));
            as[2] = addDayLight((String) arraylist.get(2), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[2]))).append("daylight").toString(), "0")));
            as[3] = addDayLight((String) arraylist.get(3), -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[3]))).append("daylight").toString(), "0")));
            as[4] = addDayLight((String) arraylist.get(5), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[4]))).append("daylight").toString(), "0")));
            as[5] = addDayLight((String) arraylist.get(6), Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[5]))).append("daylight").toString(), "0")));
            p_time_2 = as;
            j++;
        }
        k = 0;
        while (k < p_time_2.length) {
            alarmIntent_1.putExtra("type", p_name_2[k]);
            alarmIntent_1.putExtra("time", p_time_2[k]);
            alarmIntent_1.putExtra("id", k);
            LogUtils.i((new StringBuilder(String.valueOf(k))).append(" id ").append(p_time_2[k]).append(" p_time length ").append(p_name_2[k]).toString());
            set_alarms(k, p_time_2[k], p_name_2[k]);
            k++;
        }
    }

    public boolean isNetworkAvailable() {
        NetworkInfo anetworkinfo[];
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService("connectivity");
        if (connectivitymanager != null) {
            if ((anetworkinfo = connectivitymanager.getAllNetworkInfo()) != null) {
                int i = 0;
                while (i < anetworkinfo.length) {
                    if (anetworkinfo[i].getState() == android.net.NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                    i++;
                }
            }
        }
        return false;
    }

    public boolean isOnline() {
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService("connectivity");
        return connectivitymanager.getActiveNetworkInfo() != null && connectivitymanager.getActiveNetworkInfo().isAvailable() && connectivitymanager.getActiveNetworkInfo().isConnected();
    }

    public boolean isPackageExists(String s) {
        Iterator iterator = getPackageManager().getInstalledApplications(0).iterator();
        do {
            if (!iterator.hasNext()) {
                return false;
            }
        } while (!((ApplicationInfo) iterator.next()).packageName.equals(s));
        return true;
    }

    public boolean loadBoolean(String s) {
        return getSharedPreferences("Prayer_pref", 0).getBoolean(s, false);
    }

    public Long loadLong(String s) {
        return Long.valueOf(getSharedPreferences("Prayer_pref", 0).getLong(s, 0L));
    }

    public int loadPreferences(String s) {
        return getSharedPreferences("Prayer_pref", 0).getInt(s, 0);
    }

    public String loadString(String s) {
        return getSharedPreferences("Prayer_pref", 0).getString(s, "");
    }

    public void loadlist() {
        (new Thread(new Runnable() {
            public void run() {
            }
        })).start();
    }

    public String loadpref(String s) {
        return getSharedPreferences("Prayer_pref", 0).getString(s, "false");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onLocationChanged(Location location1) {
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
        int i = menuitem.getItemId();
        if (i == 0x102002c)
            finish();
        else if (i == R.id.action_settings)
            startActivity(new Intent(this, com.prayertimes.qibla.appsourcehub.activity.ActivitySettings.class));
        if (i == R.id.action_qibla)
            startActivity(new Intent(this, com.prayertimes.qibla.appsourcehub.activity.ActivityThemes.class));
        if (i == R.id.action_location)
            startActivity(new Intent(this, com.prayertimes.qibla.appsourcehub.activity.ActivityPlaceLocation.class));
        return super.onOptionsItemSelected(menuitem);
    }

    public void onProviderDisabled(String s) {
    }

    public void onProviderEnabled(String s) {
    }

    protected void onStart() {
        //GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    protected void onStop() {
       // GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    public void rate() {
        if (ratefun()) {
            Rate();
        }
    }

    public boolean ratefun() {
        Boolean boolean1 = Boolean.valueOf(loadBoolean("rated"));
        long l = 1L + loadLong("launch_count").longValue();
        saveLong("launch_count", l);
        Long long1 = loadLong("date_firstlaunch");
        if (long1.longValue() == 0L) {
            long1 = Long.valueOf(System.currentTimeMillis());
            saveLong("date_firstlaunch", long1.longValue());
        }
        if (l >= 5L && System.currentTimeMillis() >= 0L + long1.longValue() && !boolean1.booleanValue()) {
            saveLong("launch_count", 0L);
            return true;
        } else {
            return false;
        }
    }

    public void saveBoolean(String s, boolean flag) {
        android.content.SharedPreferences.Editor editor1 = getSharedPreferences("Prayer_pref", 0).edit();
        editor1.putBoolean(s, flag);
        editor1.commit();
    }

    public void saveDouble(String s, float f) {
        android.content.SharedPreferences.Editor editor1 = getSharedPreferences("Prayer_pref", 0).edit();
        editor1.putFloat(s, f);
        editor1.commit();
    }

    public void saveLong(String s, long l) {
        android.content.SharedPreferences.Editor editor1 = getSharedPreferences("Prayer_pref", 0).edit();
        editor1.putLong(s, l);
        editor1.commit();
    }

    public void savePreferences(String s, int i) {
        android.content.SharedPreferences.Editor editor1 = getSharedPreferences("Prayer_pref", 0).edit();
        editor1.putInt(s, i);
        editor1.commit();
    }

    public void saveString(String s, String s1) {
        android.content.SharedPreferences.Editor editor1 = getSharedPreferences("Prayer_pref", 0).edit();
        editor1.putString(s, s1);
        editor1.commit();
    }

    public void set_alarms(int i, String s, String s1) {
        SimpleDateFormat simpledateformat;
        String s2;
        int j;
        simpledateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        s2 = simpledateformat.format(new Date());
        j = 0;
        int i1;
        int k;
        try {
            String s3 = changeTimeFormat(s);
            j = Integer.parseInt(s3.substring(0, 2));
            i1 = Integer.parseInt(s3.substring(3, 5));
            k = i1;
        } catch (Exception e) {
            k = 0;
        }
        Calendar calendar = Calendar.getInstance();
        ParseException parseexception;
        int l;
        Date date;
        Calendar calendar1;
        try {
            calendar.setTime(simpledateformat.parse(s2));
        } catch (ParseException parseexception1) {
            parseexception1.printStackTrace();
        }
        calendar.set(11, j);
        if (i == 0) {
            l = -15 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[0]))).append("daylight").toString(), "0"));
        } else if (i == 3) {
            l = -66 + Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[3]))).append("daylight").toString(), "0"));
        } else {
            l = Integer.parseInt(pref.getString((new StringBuilder(String.valueOf(p_name_2[i]))).append("daylight").toString(), "0"));
        }
        calendar.set(12, k + l);
        date = new Date();
        calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        if (!calendar1.after(calendar)) {
            LogUtils.i((new StringBuilder(String.valueOf(i))).append(" set alarm ").append(s1).toString());
            pendingIntent_1 = PendingIntent.getBroadcast(this, i, alarmIntent_1, 0);
            ((AlarmManager) getSystemService("alarm")).set(0, calendar.getTimeInMillis(), pendingIntent_1);
        }
    }

    public void showSettingsAlert() {
        (new com.afollestad.materialdialogs.MaterialDialog.Builder(this)).title("GPS is Not Enabled").content("Enable your GPS and Refresh the Activity").cancelable(false).positiveText("Ok").positiveColor(Color.parseColor("#379e24")).negativeText(0x1040000).negativeColor(Color.parseColor("#379e24")).callback(new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
            public void onPositive(MaterialDialog materialdialog) {
                super.onPositive(materialdialog);
            }
        }).build().show();
    }

    public void showalert() {
        (new com.afollestad.materialdialogs.MaterialDialog.Builder(this)).title("No Internet Connection").content("Enable your Network Connectivity").cancelable(false).positiveText("Ok").positiveColor(Color.parseColor("#379e24")).negativeText(0x1040000).negativeColor(Color.parseColor("#379e24")).callback(new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
            public void onPositive(MaterialDialog materialdialog) {
                super.onPositive(materialdialog);
            }
        }).build().show();
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public void typeface() {
        tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tf1 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tf2 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tf3 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tf_arabic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public String uploadImage(String s, String s1) {
        String s2;
        s2 = null;
        File file = new File(s1);
        if (!file.isFile()) {
            LogUtils.e("uploadFile", (new StringBuilder("Source File not exist :")).append(s1).toString());
            return null;
        }
        FileInputStream fileinputstream;
        HttpURLConnection httpurlconnection;
        DataOutputStream dataoutputstream;
        try {
            fileinputstream = new FileInputStream(s1);
            URL url = new URL(s);
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Connection", "Keep-Alive");
            httpurlconnection.setRequestProperty("ENCTYPE", "multipart/form-data");
            httpurlconnection.setRequestProperty("Content-Type", (new StringBuilder("multipart/form-data;boundary=")).append("*****").toString());
            dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
            int i;
            byte abyte0[];
            int j;
            dataoutputstream.writeBytes((new StringBuilder(String.valueOf("--"))).append("*****").append("\r\n").toString());
            dataoutputstream.writeBytes((new StringBuilder("Content-Disposition: form-data; name=\"userfile\";filename=\"")).append(s1).append("\"").append("\r\n").toString());
            dataoutputstream.writeBytes("\r\n");
            i = Math.min(fileinputstream.available(), 0x100000);
            abyte0 = new byte[i];
            j = fileinputstream.read(abyte0, 0, i);
            s2 = null;
            while (j > 0) {
                int k;
                dataoutputstream.write(abyte0, 0, i);
                i = Math.min(fileinputstream.available(), 0x100000);
                k = fileinputstream.read(abyte0, 0, i);
                j = k;
            }
            BufferedReader bufferedreader;
            dataoutputstream.writeBytes("\r\n");
            dataoutputstream.writeBytes((new StringBuilder(String.valueOf("--"))).append("*****").append("--").append("\r\n").toString());
            serverResponseCode = httpurlconnection.getResponseCode();
            String s3 = httpurlconnection.getResponseMessage();
            LogUtils.i("uploadFile", (new StringBuilder("HTTP Response is : ")).append(s3).append(": ").append(serverResponseCode).toString());
            if (serverResponseCode == 200) {
                LogUtils.i("", "File Upload Complete.");
            }
            bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            while (true) {
                String s4 = bufferedreader.readLine();
                if (s4 == null)
                    break;
                s2 = s4;
            }
            LogUtils.i("urlresult", s2);
            LogUtils.e("", "File is written");
            fileinputstream.close();
            dataoutputstream.flush();
            dataoutputstream.close();
        } catch (Exception e) {
        }
        return s2;
    }

    static {
        MODE_PRIVATE = 0;
    }
    
    
   /* public void loadBanner() {
        final AdView adview = (AdView) findViewById(R.id.adMob1);

		if (adview == null)
			return;

		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("7E81A5A9B1F5AC7EF93D4A9A84599853")
				.addTestDevice("EEDFD317A6A5C55A0272C5C43E3E9791").build();
		adview.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded() {
				adview.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				adview.setVisibility(View.GONE);
			}
		});
		adview.loadAd(adRequest);
	}*/
}

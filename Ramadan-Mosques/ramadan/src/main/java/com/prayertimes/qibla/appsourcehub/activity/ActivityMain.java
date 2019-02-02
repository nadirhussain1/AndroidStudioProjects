package com.prayertimes.qibla.appsourcehub.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mobvista.msdk.MobVistaConstans;
import com.mobvista.msdk.MobVistaSDK;
import com.mobvista.msdk.out.MobVistaSDKFactory;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterMenu;
import com.prayertimes.qibla.appsourcehub.adpater.MyPagerAdapter;
import com.prayertimes.qibla.appsourcehub.adpater.ViewPagerAdapter;
import com.prayertimes.qibla.appsourcehub.mobvista.HandlerActivity;
import com.prayertimes.qibla.appsourcehub.support.AppLocationService;
import com.prayertimes.qibla.appsourcehub.support.LocationAddress;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import com.sahaab.hijricalendar.HijriCalendarDate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import muslim.prayers.time.R;

public class ActivityMain extends Utils implements MoPubInterstitial.InterstitialAdListener {

    boolean firstPageInpager = true;
    ViewPager myPager;
    private Menu menu;
    private MoPubInterstitial moPubInterstitial;
    //InterstitialAd interstitialAd;
    // com.google.android.gms.ads.InterstitialAd interstitialAd2;
    LinearLayout circle_first, circle_second;
    int imageArra[] = {R.drawable.ic_launcher, R.drawable.ic_launcher};
    SharedPreferences pref;
    static SharedPreferences.Editor editor;
    int counter;
    static Activity activity;
    private AdView adview;

    public static DrawerLayout Drawer;
    private static final int LOCATION_INTENT_CALLED = 1;
    // public static ListView category_list;
    AdRequest adRequest;
    AdapterMenu adapter;
    AppLocationService appLocationService;
    ArrayList arr_items;

    int interflag;
    ViewPagerAdapter adapter1;
    ActionBarDrawerToggle mDrawerToggle;
    LinearLayout main_layout;
    ProgressDialog showProgressDialog;
    Toolbar toolbar;
    TextView txt_date;
    TextView txt_time;
    TextView txt_title;
    String weekDay;

    public ActivityMain() {
        weekDay = "";
        arr_items = new ArrayList();
        interflag = 2;
    }

    public void getLoc() {
        Location location;
        double d;
        double d1;
        Iterator iterator;
        Address address;
        String s;
        String s1;
        String s2;
        String s3;
        try {
            location = appLocationService.getLocation();
            LogUtils.i((new StringBuilder("location ")).append(location)
                    .toString());
            if (location == null) {
                showSettingsAlert();
                SavePref(USER_CITY, "");
                SavePref(USER_STATE, "");
                SavePref(USER_STREET, "");
                SavePref(USER_COUNTRY, "");
                return;
            }
            d = location.getLatitude();
            d1 = location.getLongitude();
            saveString(USER_LAT, String.valueOf(d));
            saveString(USER_LNG, String.valueOf(d1));
            iterator = (new Geocoder(this, Locale.getDefault()))
                    .getFromLocation(d, d1, 1).iterator();
            while (iterator.hasNext()) {
                address = (Address) iterator.next();
                LogUtils.i((new StringBuilder(String.valueOf(address
                        .getLocality()))).append(" city ")
                        .append(address.getAdminArea()).append(" state ")
                        .append(address.getCountryName()).toString());
                if (address != null) {
                    s = address.getLocality();
                    s1 = address.getCountryName();
                    s2 = address.getAdminArea();
                    s3 = address.getSubLocality();
                    SavePref(USER_CITY, s);
                    SavePref(USER_STATE, s2);
                    SavePref(USER_COUNTRY, s1);
                    SavePref(USER_STREET, s3);
                }
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            SavePref(USER_CITY, "");
            SavePref(USER_STATE, "");
            SavePref(USER_STREET, "");
            SavePref(USER_COUNTRY, "");
        }
    }

    public void getLoc1() {
        appLocationService = new AppLocationService(this);
        Location location = appLocationService.getLocation();
        if (location != null) {
            double d = location.getLatitude();
            double d1 = location.getLongitude();
            LogUtils.i((new StringBuilder(String.valueOf(d)))
                    .append(" location ").append(d1).toString());
            saveString(USER_LAT, String.valueOf(d));
            saveString(USER_LNG, String.valueOf(d1));
            (new LocationAddress()).getAddressFromLocation(this);
            return;
        } else {
            showalert();
            return;
        }
    }

    public void loadlist() {
    }

    public void onActivityResult(int i, int j, Intent intent) {
        LogUtils.i((new StringBuilder(" on activity result")).append(j)
                .append(" ").append(intent).toString());
        if (j == 1) {
            LogUtils.i(" on log", "if  condition");
            getLocation();
            return;
        } else {
            getLoc();
            return;
        }
    }

    public void onBackPressed() {
        /*
         * Log.d("if rateus pref", (new
		 * StringBuilder(String.valueOf(loadBoolean("rated")))) .toString()); if
		 * (!loadBoolean("rated")) { //Rate(); return; }
		 * Log.d("else rateus pref", (new
		 * StringBuilder(String.valueOf(loadBoolean("rated")))) .toString());
		 */
        /*
		 * if (Drawer.isDrawerOpen(category_list)) {
		 * Drawer.closeDrawer(category_list); }
		 */
        if (firstPageInpager) {
            (new com.afollestad.materialdialogs.MaterialDialog.Builder(this))
                    .title(R.string.app_name)
                    .content(getString(R.string.lbl_exit_app))
                    .cancelable(false)
                    .positiveText(0x104000a)
                    .positiveColor(Color.parseColor("#27ae60"))
                    .negativeText(0x1040000)
                    .negativeColor(Color.parseColor("#27ae60"))
                    .callback(
                            new com.afollestad.materialdialogs.MaterialDialog.ButtonCallback() {
                                public void onPositive(
                                        MaterialDialog paramAnonymousMaterialDialog) {
                                    super.onPositive(paramAnonymousMaterialDialog);
                                    ActivityMain.startadInterval = 10000;
                                    ActivityMain.this.handler_ad
                                            .removeCallbacks(ActivityMain.this.runnable_ad);
                                    finish();
                                }
                            }).build().show();
        } else {
            myPager.setCurrentItem(0);
        }

    }

    public void preloadWall() {
        MobVistaSDK sdk = MobVistaSDKFactory.getMobVistaSDK();
        Map<String, Object> preloadMap = new HashMap<String, Object>();
        preloadMap.put(MobVistaConstans.PROPERTIES_LAYOUT_TYPE,
                MobVistaConstans.LAYOUT_APPWALL);
        preloadMap.put(MobVistaConstans.PROPERTIES_UNIT_ID,
                "1314");
        sdk.preload(preloadMap);

    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        mDrawerToggle.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle) {
		/* fullscreen(); */
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        activity = ActivityMain.this;

        myPager = (ViewPager) findViewById(R.id.pager);
        myPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


        initFirebaseAnalytics();
        banner_ad();
        displayMopubAd();
        //displayFacebookAds();


        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        counter = pref.getInt("appOpenNumber", 1);

        if (counter == 3) {

            showdialog();
            counter = 0;

        } else if (counter < 3) {
            counter++;
        }

        if (counter != 10) {

            editor.putInt("appOpenNumber", counter);
            editor.commit();
        }

        myPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

                if (arg0 == 0) {
                    circle_first.setVisibility(View.VISIBLE);
                    circle_second.setVisibility(View.GONE);
                    firstPageInpager = true;
                } else {
                    circle_first.setVisibility(View.GONE);
                    circle_second.setVisibility(View.VISIBLE);
                    firstPageInpager = false;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
		/*
		 * adRequest = (new AdRequest.Builder())
		 * .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		 * .addTestDevice("B5169A57FF57E5D323B4A99BC6BC7B37").build();
		 * interstitialAd = new InterstitialAd(this);
		 * interstitialAd.setAdUnitId(getString(R.string.interstitial_id));
		 * interstitialAd.loadAd(adRequest);
		 */
        circle_first = (LinearLayout) findViewById(R.id.circle_first);
        circle_second = (LinearLayout) findViewById(R.id.circle_second);

        circle_first.setVisibility(View.VISIBLE);
        circle_second.setVisibility(View.GONE);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        appLocationService = new AppLocationService(this);
        getSupportActionBar().setTitle(
                Html.fromHtml((new StringBuilder("<font color=\"#ffffff\">"))
                        .append(getString(R.string.app_name)).append("</font>")
                        .toString()));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        typeface();
        // rate();
        Analytics(getString(R.string.app_name));
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_date = (TextView) findViewById(R.id.txt_dat);
        txt_title = (TextView) findViewById(R.id.txt_title);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE",
                Locale.getDefault());
        String simpledateformatMonth = new SimpleDateFormat("MMMM",
                Locale.getDefault()).format(calendar.getTime());

        Date date = new Date();
        date.setHours(8 + date.getHours());
        weekDay = simpledateformat.format(calendar.getTime());
        txt_date.setText((new StringBuilder(String
                .valueOf(simpledateformatMonth))).append(" ")
                .append(calendar.get(5)).append(", ").append(calendar.get(1))
                .append(" ").append(weekDay).toString());

        txt_date.setTypeface(tf2);
        txt_title.setTypeface(tf2);
        txt_time.setTypeface(tf2);
        LogUtils.i((new StringBuilder("txt_time")).append(
                txt_time.getTypeface()).toString());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(5, 0);
        String s1 = (new StringBuilder(String.valueOf(HijriCalendarDate
                .getSimpleDateDay(calendar1, 0)))).append(" ")
                .append(HijriCalendarDate.getSimpleDateMonth(calendar1, 0))
                .append(" ")
                .append(HijriCalendarDate.getSimpleDateYear(calendar1, 0))
                .toString();
        txt_title.setText(s1);
        LogUtils.i((new StringBuilder(String.valueOf(LoadPref(USER_MLAT))))
                .append(" lat ").append(LoadPref(USER_MLNG)).toString());
        // category_list = (ListView) findViewById(R.id.category_list);
        Drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar1,
                R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        arr_items.clear();
        arr_items.add(getString(R.string.lblprayertime));
        arr_items.add(getString(R.string.menu_prayer_ramdantime));
        arr_items.add(getString(R.string.menu_moreapps));
        arr_items.add(getString(R.string.menu_rate_app));
        arr_items.add(getString(R.string.menu_share));
        arr_items.add(getString(R.string.menu_compass_themes));
        arr_items.add(getString(R.string.menu_facebook));
        arr_items.add(getString(R.string.menu_twitter));
        arr_items.add(getString(R.string.menu_instruction));
        arr_items.add(getString(R.string.menu_feedback));
        arr_items.add(getString(R.string.menu_about));
        arr_items.add(getString(R.string.menu_exit));
        adapter = new AdapterMenu(this, arr_items);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page, menu);
        this.menu = menu;
        return true;
    }

    protected void onDestroy() {
        if (moPubInterstitial != null) {
            moPubInterstitial.destroy();
        }
        handler_ad.removeCallbacks(runnable_ad);
        setResult(0, new Intent());
        LogUtils.i("", "destroy");
        if (appLocationService != null) {
            appLocationService.stopUsingGPS();
            stopService(new Intent(
                    this,
                    com.prayertimes.qibla.appsourcehub.support.AppLocationService.class));
        }
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
        int i = menuitem.getItemId();
        LogUtils.i((new StringBuilder("menu ")).append(i).toString());
        if (i == 0x102002c) {
            finish();
        } else if (i == R.id.action_setting) {
            startActivity(new Intent(
                    this,
                    com.prayertimes.qibla.appsourcehub.activity.ActivitySettings.class));
        }
        if (i == R.id.action_market) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.market));
            Intent intent1 = new Intent(this, HandlerActivity.class);
            startActivity(intent1);

        }
        if (mDrawerToggle.onOptionsItemSelected(menuitem)) {
            return true;
        } else {
            return super.onOptionsItemSelected(menuitem);
        }
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        mDrawerToggle.syncState();
    }

    protected void onResume() {

        try {
            preloadWall();
            font();
            if (pref_time.equals("0")) {
                String s1 = (new SimpleDateFormat("hh:mm aa",
                        Locale.getDefault())).format(new Date());
                txt_time.setText(s1);
            } else {
                String s = (new SimpleDateFormat("HH:mm")).format(new Date());
                System.out.println(s);
                txt_time.setText(s);
            }
            if (!notify.equals("0"))
                cancel();
            else
                initPrayerTimes(0);
        } catch (Exception e) {
        }
        super.onResume();

    }

    public void showProgress() {
        showProgressDialog = new ProgressDialog(this);
        showProgressDialog.setTitle(R.string.progress_dialog);
        showProgressDialog.setMessage(getString(R.string.please_wait));
        showProgressDialog.show();
    }

    public void displayMopubAd() {
        moPubInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_add_unit_id));
        moPubInterstitial.setInterstitialAdListener(this);
        moPubInterstitial.load();
    }

//    public void displayFacebookAds() {
//
//        interstitialAd = new InterstitialAd(ActivityMain.this, getString(R.string.facebookAddId));
//        interstitialAd.setAdListener(ActivityMain.this);
//        interstitialAd.loadAd();
//
//    }

//	protected void DisplayInterstitialAdGoogle() {
//		interstitialAd2 = new com.google.android.gms.ads.InterstitialAd(ActivityMain.this);
//		interstitialAd2.setAdUnitId(getResources().getString(
//				R.string.interstitial_id));
//		AdRequest adRequest = new AdRequest.Builder().build();
//		interstitialAd2.loadAd(adRequest);
//		interstitialAd2.setAdListener(new AdListener() {
//			@Override
//			public void onAdLoaded() {
//				interstitialAd2.show();
//			}
//
//			@Override
//			public void onAdClosed() {
//				// TODO Auto-generated method stub
//				super.onAdClosed();
//			}
//		});
//	}


    private void initFirebaseAnalytics() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home Screen");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Screen");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    public static void showdialog() {
        // TODO Auto-generated method stub

        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);

        // set the custom dialog components - text, image and button
        TextView tvReminderlater = (TextView) dialog
                .findViewById(R.id.tvReminderlater);
        TextView tvNothanks = (TextView) dialog.findViewById(R.id.tvNothanks);
        TextView tvRatenow = (TextView) dialog.findViewById(R.id.tvRatenow);

        // if button is clicked, close the custom dialog

        dialog.show();
        tvReminderlater.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        tvRatenow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                editor.putInt("appOpenNumber", 10);
                editor.commit();
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id="
                                + activity.getPackageName())));
                dialog.dismiss();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 100ms
                        Intent intent = new Intent(activity, RateService.class);
                        activity.startService(intent);

                    }
                }, 3000);

            }
        });
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        if (moPubInterstitial.isReady()) {
            moPubInterstitial.show();
        }
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {

    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {

    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {

    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {

    }
}

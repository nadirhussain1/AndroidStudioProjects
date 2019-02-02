package com.connectivityapps.flashlight;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.connectivityapps.adapters.NavigationAdapter;
import com.connectivityapps.fragments.BubbleFragment;
import com.connectivityapps.fragments.FlashFragment;
import com.connectivityapps.fragments.FullScreenFragment;
import com.connectivityapps.fragments.SosFragment;
import com.connectivityapps.fragments.StrobeFragment;
import com.connectivityapps.shared.SettingsController;
import com.connectivityapps.storage.FlashPreferences;
import com.connectivityapps.utils.GlobalUtil;
import com.connectivityapps.utils.ScalingUtility;
import com.flurry.android.FlurryAgent;
import com.mopub.mobileads.MoPubConversionTracker;
import com.mopub.mobileads.MoPubView;

import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {

    private static final String MOPUB_ID = "922fb3c28b2b4de6ad7fd855a495502f";
    private String[] drawerTitlesList = null;
    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerListView = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private MoPubView moPubView;
    public static ImageView batteryLevelIconView = null;
    public static TextView batteryLevelTextView = null;
    private String APP_MARKET_URL = "https://play.google.com/store/apps/details?id=";
    private static MediaPlayer soundMediaPlayer = null;
    private NotificationManager notificationManager = null;
    public static final int myNotificationId = 111;
    private int selectedFragmentPosition = 0;
    private boolean isClicked = false;
    private NavigationAdapter adapter = null;
    private SurfaceView cameraPreview = null;
    private String[] highlightedColorsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = getLayoutInflater().inflate(R.layout.main, null);
        ScalingUtility.getInstance(this).scaleView(rootView);
        setContentView(rootView);
        initCamera();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        initNavigationDrawer();
        loadAd();
        FlashPreferences.getInstance(this).loadSettingsPref();

        selectedFragmentPosition = SettingsController.getInstance().modeToOpenAtLaunchIndex;
        displayScreen(selectedFragmentPosition);
        setTitle(drawerTitlesList[selectedFragmentPosition]);

        soundMediaPlayer = MediaPlayer.create(this, R.raw.light_switch);
        if (!FlashPreferences.getInstance(this).IsConversionTrackingDone()) {
            FlashPreferences.getInstance(this).changeConversionTrackingStatus(true);
            new MoPubConversionTracker().reportAppOpen(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationManager.cancel(myNotificationId);
        registerBatteryReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FlurryAgent.onStartSession(this);
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void initCamera() {
        GlobalUtil.getCamera(this);
        if (GlobalUtil.camera != null) {
            cameraPreview = (SurfaceView) findViewById(R.id.cameraPreviewSurface);
            final SurfaceHolder surfaceHolder = cameraPreview.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    protected void onPause() {
        String message = "";
        String title = "";
        if (SettingsController.getInstance().lastSelectedScreen == 0 && FlashFragment.isOn) {
            message = getString(R.string.flashlight_noti_msg);
            title = getString(R.string.flashlight_noti_title);
            showNotification(message, title);
        } else if (SettingsController.getInstance().lastSelectedScreen == 2 && StrobeFragment.isInProgress) {
            message = getString(R.string.strobelight_noti_msg);
            title = getString(R.string.strobe_noti_title);
            showNotification(message, title);
        } else if (SettingsController.getInstance().lastSelectedScreen == 3 && SosFragment.isInProgress && SettingsController.getInstance().sosLightToFlashIndex != 1) {
            message = getString(R.string.soslight_noti_msg);
            title = getString(R.string.sos_noti_title);
            showNotification(message, title);
        }
        try {
            unregisterReceiver(batteryLevelReceiver);
        }catch(Exception exception){

        }
        super.onPause();
    }

    private void showNotification(String message, String title) {
        PendingIntent notificationIntent = preparePendingIntent();
        Notification notification = createBasicNotification(title, message, notificationIntent);
        notificationManager.notify(myNotificationId, notification);
    }

    private Notification createBasicNotification(String title, String message, PendingIntent intent) {
        NotificationCompat.Builder builder = new Builder(getApplicationContext());
        Notification notification = builder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(intent)
                .build();

        return notification;
    }

    private PendingIntent preparePendingIntent() {
        Intent intent = new Intent(getApplicationContext(), KillActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static void playSound() {
        if (SettingsController.getInstance().isSoundEnabled) {
            soundMediaPlayer.start();
        }
    }

    private void registerBatteryReceiver() {
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    private void loadAd() {
        moPubView = (MoPubView) findViewById(R.id.adView);
        moPubView.setAdUnitId(MOPUB_ID); // Enter your Ad Unit ID from www.mopub.com
        moPubView.loadAd();
    }

    private void initNavigationDrawer() {
        highlightedColorsList = getResources().getStringArray(R.array.drawer_selected_colors_list);
        mTitle = mDrawerTitle = getTitle();
        drawerTitlesList = getResources().getStringArray(R.array.drawer_title_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        adapter = new NavigationAdapter(this, drawerTitlesList);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (isClicked) {
                    displayScreen(selectedFragmentPosition);
                    adapter.notifyDataSetChanged();
                }
                if (selectedFragmentPosition < 5) {
                    setTitle(drawerTitlesList[selectedFragmentPosition]);
                }

                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(drawerTitlesList[selectedFragmentPosition]);
                isClicked = false;
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openSettingsScreen();
            return true;
        } else if (id == R.id.action_sos) {
            displayScreen(3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        GlobalUtil.releaseCamera();
        notificationManager.cancelAll();
        moPubView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
       MainActivity.this.finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            try {
                if (GlobalUtil.camera != null) {
                    GlobalUtil.camera.stopPreview();

                    List<Camera.Size> previewSizes = GlobalUtil.params.getSupportedPreviewSizes();
                    Camera.Size previewSize = previewSizes.get(0);
                    GlobalUtil.params.setPreviewSize(previewSize.width, previewSize.height);
                    GlobalUtil.camera.setParameters(GlobalUtil.params);

                    GlobalUtil.camera.setPreviewDisplay(cameraPreview.getHolder());
                    GlobalUtil.camera.startPreview();
                    GlobalUtil.isCameraReady = true;
                }
            } catch (IOException exception) {
                GlobalUtil.isCameraReady = false;
                Toast.makeText(MainActivity.this, getString(R.string.unable_to_start_camera), Toast.LENGTH_LONG).show();
            }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectedFragmentPosition = position;
            adapter.notifyDataSetChanged();
            if (position < 5) {
                isClicked = true;
                closeDrawer();
            } else {
                view.findViewById(R.id.scalingLayout).setBackgroundColor(Color.parseColor(highlightedColorsList[position]));
                displayScreen(selectedFragmentPosition);
            }

        }
    }

    private void closeDrawer() {
        mDrawerListView.setItemChecked(selectedFragmentPosition, true);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    private void displayScreen(int position) {
        SettingsController.getInstance().lastSelectedScreen = position;
        if (position == 5) {
            FlurryAgent.logEvent("Settings Screen opened from drawer");
            openSettingsScreen();
        } else if (position == 6) {
            FlurryAgent.logEvent("Share action performed");
            shareIntent();
        } else if (position == 7) {
            FlurryAgent.logEvent("Rate action performed");
            rateApp();
        } else if (position == 8) {
            FlurryAgent.logEvent("About screen opened");
            launchAboutScreen();
        } else {
            selectItem(position);
        }
    }

    private void shareIntent() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String link_val = APP_MARKET_URL + getPackageName();
        String emailBody = getString(R.string.share_app_text) + link_val;

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via_label)));
        }catch(Exception exception){

        }
    }

    private void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(myAppLinkToMarket);
    }

    private void launchAboutScreen() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void openSettingsScreen() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void selectItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("DEFAULT", false);

        Fragment fragment = null;
        if (position == 0) {
            FlurryAgent.logEvent("Flashlight  opened");
            fragment = new FlashFragment();
        } else if (position == 1) {
            FlurryAgent.logEvent("Fullscreen light  opened");
            fragment = new FullScreenFragment();
        } else if (position == 2) {
            FlurryAgent.logEvent("Strobe light  opened");
            fragment = new StrobeFragment();
        } else if (position == 3) {
            FlurryAgent.logEvent("Sos screen  opened");
            fragment = new SosFragment();
        } else if (position == 4) {
            FlurryAgent.logEvent("Bubble screen  opened");
            fragment = new BubbleFragment();
        }

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (batteryLevelIconView != null && batteryLevelTextView != null) {
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
                GlobalUtil.batteryLevelPercentage = level;
                GlobalUtil.updateBatteryStatus(MainActivity.this);
            }
        }
    };
}

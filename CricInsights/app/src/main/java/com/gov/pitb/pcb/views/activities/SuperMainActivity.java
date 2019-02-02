package com.gov.pitb.pcb.views.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.navigators.AlertDialogTwoButtonsListener;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnInProgressDbDataLoaded;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnMatchConfigured;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnNewMatchStarted;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnServerDataLoadSuccess;
import com.gov.pitb.pcb.services.LoadInProgressDbStateService;
import com.gov.pitb.pcb.services.SaveDataToDbService;
import com.gov.pitb.pcb.services.SendDataToServerService;
import com.gov.pitb.pcb.utils.GlobalUtil;
import com.gov.pitb.pcb.utils.ViewScaleHandler;
import com.gov.pitb.pcb.views.fragments.CreateMatchFragment;
import com.gov.pitb.pcb.views.fragments.MatchMainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 11/06/2017.
 */

public class SuperMainActivity extends AppCompatActivity implements AlertDialogTwoButtonsListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolBarTitleView;
    @BindView(R.id.loadingLayout)
    LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(this, R.layout.super_main_activity, null);
        new ViewScaleHandler(this).scaleRootView(rootView);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);

        setToolBar();
        loadMainContentViews();
        scheduleAlarm();
    }

    private void loadMainContentViews() {
        boolean isMatchInProgress = InsightsPreferences.isMatchInProgress(this);
        if (isMatchInProgress) {
            loadDataModelFromLocalDb();
        } else {
            loadCreateMatchFragment();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        showExitConfirmDialog();
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
    }

    private void loadCreateMatchFragment() {
        updateTitle("Select tournament");

        CreateMatchFragment fragment = new CreateMatchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    private void loadMainFragment() {
        MatchMainFragment fragment = new MatchMainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnServerDataLoadSuccess event) {
        Intent intent = new Intent(this, SaveDataToDbService.class);
        intent.putExtra(SaveDataToDbService.KEY_TOURNAMENTS_DATA, event.tournamentsData);
        intent.putExtra(SaveDataToDbService.KEY_TEAMS_DATA, event.teamsData);
        intent.putExtra(SaveDataToDbService.KEY_PLAYERS_DATA, event.playersData);

        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnMatchConfigured event) {
        loadMainFragment();
        InsightsPreferences.saveMatchInProgress(this, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnInProgressDbDataLoaded event) {
        loadingLayout.setVisibility(View.GONE);
        loadMainFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnNewMatchStarted event) {
        loadCreateMatchFragment();
    }


    public void updateTitle(String title) {
        toolBarTitleView.setText(title);
    }


    private void loadDataModelFromLocalDb() {
        loadingLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, LoadInProgressDbStateService.class);
        startService(intent);
    }

    private void showExitConfirmDialog() {
        String title = getString(R.string.confirm_exit_title);
        String message = getString(R.string.confirm_exit_msg);
        GlobalUtil.showMessageAlertWithTwoButtons(this, title, message, this, 100);
    }

    @Override
    public void onNegativeClick(int code) {

    }

    @Override
    public void onPositiveClick(int code) {
        finish();
    }


    public void scheduleAlarm() {
        Calendar cal = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(getApplicationContext(), SendDataToServerService.class);

        PendingIntent servicePendingIntent =
                PendingIntent.getService(this,
                        SendDataToServerService.SERVICE_ID,
                        serviceIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

        long interval = 1000 * 20; // After 20 seconds. Check again.
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                cal.getTimeInMillis(),
                interval,
                servicePendingIntent);

    }


}

package com.brainpixel.deliveryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.fragments.HelpFragment;
import com.brainpixel.deliveryapp.fragments.MainFragment;
import com.brainpixel.deliveryapp.fragments.MyOrdersFragment;
import com.brainpixel.deliveryapp.fragments.SettingsFragment;
import com.brainpixel.deliveryapp.handlers.OnPositiveButtonClickListener;
import com.brainpixel.deliveryapp.handlers.SwitchToMainFragmentEvent;
import com.brainpixel.deliveryapp.storage.PreferenceManager;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;


    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setActionBar();
        setupDrawer();
        loadMainFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = (TextView) headerView.findViewById(R.id.userNameView);
        TextView userEmailTextView = (TextView) headerView.findViewById(R.id.userEmailTextView);
        userNameTextView.setText(PreferenceManager.getUserName(this));
        userEmailTextView.setText(PreferenceManager.getUserEmail(this));
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                loadMainFragment();
                break;
            case R.id.nav_orders:
                loadMyOrdersFragment();
                break;
            case R.id.nav_settings:
                loadSettingsFragment();
                break;
            case R.id.nav_help:
                loadHelpFragment();
                break;
            case R.id.nav_logout:
                showLogoutAlert();
                break;
            default:

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SwitchToMainFragmentEvent event) {
        loadMainFragment();
    }


    private void loadMainFragment() {
        currentFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, currentFragment).commitAllowingStateLoss();
        navigationView.getMenu().getItem(0).setChecked(true);
        toolBarTitle.setText("Main");
    }

    private void loadMyOrdersFragment() {
        currentFragment = new MyOrdersFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, currentFragment).commitAllowingStateLoss();
        navigationView.getMenu().getItem(1).setChecked(true);
        toolBarTitle.setText("My Orders");
    }
    private void loadSettingsFragment() {
        currentFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, currentFragment).commitAllowingStateLoss();
        navigationView.getMenu().getItem(2).setChecked(true);
        toolBarTitle.setText("Settings");
    }

    private void loadHelpFragment() {
        currentFragment = new HelpFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerLayout, currentFragment).commitAllowingStateLoss();
        navigationView.getMenu().getItem(3).setChecked(true);
        toolBarTitle.setText("Help");
    }


    private void showLogoutAlert() {
        OnPositiveButtonClickListener positiveButtonClickListener = new OnPositiveButtonClickListener() {
            @Override
            public void onButtonClick() {
                doLogOut();
            }
        };

        GlobalUtil.showCustomizedAlert(this, getString(R.string.logout_title), getString(R.string.logout_msg), getString(R.string.yes_label), positiveButtonClickListener, getString(R.string.cancel_label), null);
    }

    private void doLogOut() {
        FirebaseAuth.getInstance().signOut();
        PreferenceManager.saveSignedUpStatus(this, false);
        Intent intent = new Intent(this, StaticWelcomeActivity.class);
        startActivity(intent);
        finish();
    }





}

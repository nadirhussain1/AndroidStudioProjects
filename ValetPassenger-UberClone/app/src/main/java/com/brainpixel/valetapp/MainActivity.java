package com.brainpixel.valetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.model.settings.GeneralSettingsResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.storage.ValetDbManager;
import com.brainpixel.valetapp.storage.ValetPreferences;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.PiccasoCircleTransformation;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.brainpixel.valetapp.views.activities.EditProfileActivity;
import com.brainpixel.valetapp.views.activities.HelpContentsActivity;
import com.brainpixel.valetapp.views.activities.TripsActivity;
import com.brainpixel.valetapp.views.fragments.HomeFragment;
import com.brainpixel.valetapp.views.fragments.SearchFragment;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 15/03/2017.
 */


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle drawerToggle;
    private SearchFragment searchFragment;
    private boolean isSearchFragmentAdded = false;
    private DrawerLayout drawer;

    private ImageView drawerProfileImageView;
    private TextView profileNameView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retriveUserSavedData();

        setToolBarAndDrawer();
        addHeaderToDrawer();
        loadHomeFragment();
        searchFragment = new SearchFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileInfo();
        loadSettingsFromServer();
    }

    private void retriveUserSavedData() {
        GlobalDataManager.getInstance().loggedInUserData = ValetDbManager.retrieveSavedLoggedInUser();
        if (ValetPreferences.areSettingsDownloaded(this)) {
            GlobalDataManager.getInstance().settingsDataModel = ValetDbManager.retrieveSettingsDataModel();
        }
    }

    private void setToolBarAndDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }

    public void showDrawerIcon() {
        drawerToggle.setDrawerIndicatorEnabled(true);
    }

    public void hideDrawerIcon() {
        drawerToggle.setDrawerIndicatorEnabled(false);
    }

    private void addHeaderToDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        new ScalingUtility(this).scaleRootView(headerView);

        LinearLayout profileLayout = (LinearLayout) headerView.findViewById(R.id.profileLayout);
        LinearLayout businessProfileLayout = (LinearLayout) headerView.findViewById(R.id.businesslayout);
        profileLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfileScreen();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        drawerProfileImageView = (ImageView) headerView.findViewById(R.id.profileImageView);
        profileNameView = (TextView) headerView.findViewById(R.id.profileNameView);


        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateProfileInfo() {
        String profileUrl = GlobalDataManager.getInstance().loggedInUserData.getProfileImageUrl();
        String fullName = GlobalDataManager.getInstance().loggedInUserData.getUFirstName() + " " + GlobalDataManager.getInstance().loggedInUserData.getULastName();
        if (profileUrl != null) {
            Picasso.with(this).load(profileUrl).transform(new PiccasoCircleTransformation()).into(drawerProfileImageView);
        } else {
            Picasso.with(this).load(R.drawable.default_profile_icon).transform(new PiccasoCircleTransformation()).into(drawerProfileImageView);
        }
        profileNameView.setText(fullName);
    }

    private void loadHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawerContentContainer, homeFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_help:
                switchToHelpContentsScreen();
                break;
            case R.id.nav_trips:
                switchToTripsScreen();
                break;
            case R.id.nav_payment:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchToHelpContentsScreen() {
        Intent intent = new Intent(this, HelpContentsActivity.class);
        startActivity(intent);
    }

    private void switchToTripsScreen() {
        Intent intent = new Intent(this, TripsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isSearchFragmentAdded) {
            removeSearchFragment();
            return;
        }
        super.onBackPressed();
    }

    public void addSearchFragment(int containerId) {
        isSearchFragmentAdded = true;
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.in_from_bottom, R.anim.in_from_top)
                .replace(containerId, searchFragment)
                .commit();
    }

    public void removeSearchFragment() {
        if (isSearchFragmentAdded) {
            isSearchFragmentAdded = false;
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.in_from_bottom, R.anim.in_from_top)
                    .remove(searchFragment)
                    .commit();
        }
    }

    private void loadSettingsFromServer() {
        if (!GlobalUtil.isInternetConnected(this)) {
            return;
        }

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<GeneralSettingsResponse> call = apiService.getGeneralSettings();
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<GeneralSettingsResponse>() {
            @Override
            public void onResponse(Call<GeneralSettingsResponse> call, Response<GeneralSettingsResponse> response) {
                if (response != null) {
                    GeneralSettingsResponse settingsResponse = response.body();
                    if (settingsResponse != null && settingsResponse.getSuccess()) {
                        ValetDbManager.saveSettings(settingsResponse.getData());
                        GlobalDataManager.getInstance().settingsDataModel = settingsResponse.getData();
                        ValetPreferences.saveSettingsDownloadStatus(MainActivity.this, true);
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralSettingsResponse> call, Throwable t) {

            }
        });
    }

    private void openEditProfileScreen() {
        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }
}

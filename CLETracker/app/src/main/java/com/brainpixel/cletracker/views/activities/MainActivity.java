package com.brainpixel.cletracker.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;

import com.brainpixel.cletracker.LoginActivity;
import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.model.GlobalDataManager;
import com.brainpixel.cletracker.model.UserProfileDataModel;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.brainpixel.cletracker.views.ProfileActivity;
import com.brainpixel.cletracker.views.fragments.CompletedClesFragment;
import com.brainpixel.cletracker.views.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 04/04/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        switchToHomeFragment();

    }

    private void initViews() {
        configureToolBar();
        configureDrawer();
        addDrawerHeaderView();
    }

    private void configureToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public void setToolBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void configureDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addDrawerHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.navigation_drawer_header, null);
        new ScalingUtility(this).scaleRootView(headerView);
        TextView userNameTextView = (TextView) headerView.findViewById(R.id.userNameTextView);


        if (GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel != null) {
            UserProfileDataModel userProfileDataModel = GlobalDataManager.getGlobalDataManager().loggedInUserInfoModel;
            String name = userProfileDataModel.getFirstName() + " " + userProfileDataModel.getLastName();
            userNameTextView.setText(name.toUpperCase());
        }
        navigationView.addHeaderView(headerView);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                switchToHomeFragment();
                break;
            case R.id.nav_cles:
                switchToCompletedClesFragment();
                break;
            case R.id.nav_profile:
                openProfileActivity();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchToHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, homeFragment);
        fragmentTransaction.commit();

        setToolBarTitle(getString(R.string.drawer_home));
    }

    private void switchToCompletedClesFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CompletedClesFragment homeFragment = new CompletedClesFragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, homeFragment);
        fragmentTransaction.commit();

        setToolBarTitle(getString(R.string.drawer_cles));
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        goToLoginActivity();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

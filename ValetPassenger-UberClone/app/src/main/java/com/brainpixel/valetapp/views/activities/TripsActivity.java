package com.brainpixel.valetapp.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.brainpixel.valetapp.views.fragments.PastTripsFragment;
import com.brainpixel.valetapp.views.fragments.UpcomingTripsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 02/06/2017.
 */

public class TripsActivity extends AppCompatActivity {
    @BindView(R.id.tripsPager)
    ViewPager tripsViewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private TripsPagerAdapter tripsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_trips, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);
        setUpViewPagerIndicator();

    }

    private void setUpViewPagerIndicator() {
        tripsPagerAdapter = new TripsPagerAdapter(getSupportFragmentManager());
        tripsViewPager.setAdapter(tripsPagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Past"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tripsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }

    @OnClick(R.id.backClickArea)
    public void backArrowClicked() {
        finish();
    }

    public static class TripsPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public TripsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new UpcomingTripsFragment();
                default:
                    return new PastTripsFragment();
            }


        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Past";
            } else {
                return "Upcoming";
            }

        }

    }

    private OnTabSelectedListener tabSelectedListener = new OnTabSelectedListener() {
        @Override
        public void onTabSelected(Tab tab) {
            tripsViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(Tab tab) {

        }

        @Override
        public void onTabReselected(Tab tab) {

        }
    };

}

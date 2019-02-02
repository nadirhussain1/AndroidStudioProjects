package com.brainpixel.deliveryapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.fragments.DescriptionFragment;
import com.brainpixel.deliveryapp.fragments.RatingsFragment;
import com.brainpixel.deliveryapp.fragments.SpecificationFragment;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 08/09/2018.
 */

public class TabsDetailsActivity extends FragmentActivity {
    public static final String KEY_CURRENT_TAB = "current_tab";

    public static final int TAB_RATING = 3;
    public static final int TAB_DESCRIPTION = 0;

    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.vPager)
    ViewPager viewPager;
    @BindView(R.id.pager_header)
    PagerTabStrip pagerTabStrip;

    public MainItem selectedItem;
    private int currentTab = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);
        setUpViewPager();

        if (getIntent() != null) {
            currentTab = getIntent().getIntExtra(KEY_CURRENT_TAB, 0);
            viewPager.setCurrentItem(currentTab);
        }

        selectedItem = GlobalDataManager.getInstance().selectedItem;
        titleView.setText(selectedItem.getName());
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_activity_layout, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void setUpViewPager() {
        pagerTabStrip.setTextColor(Color.BLACK);
        pagerTabStrip.setBackgroundColor(Color.WHITE);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.green_color_in_app));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }


    @Override
    public void onBackPressed() {
        goBack();
    }

    @OnClick(R.id.backIconView)
    public void onArrowClicked() {
        goBack();
    }

    @OnClick(R.id.titleView)
    public void onTitleClicked() {
        goBack();
    }

    private void goBack() {
        finish();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_ITEMS = 3;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        @Override
        public int getCount() {
            return NUM_ITEMS;
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new DescriptionFragment();
                    break;
                case 1:
                    fragment = new SpecificationFragment();
                    break;
                default:
                    fragment = new RatingsFragment();
            }
            return fragment;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Description";
                case 1:
                    return "Product Specifications";
                default:
                    return "Ratings Reviews";
            }

        }

    }

}

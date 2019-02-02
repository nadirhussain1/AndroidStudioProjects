package com.prayertimes.qibla.appsourcehub.adpater;

import com.prayertimes.qibla.appsourcehub.activity.FirstFragment;
import com.prayertimes.qibla.appsourcehub.activity.SecondFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

        case 0:
        	return FirstFragment.newInstance();
        case 1: 
        	return SecondFragment.newInstance();
        
        default:
        	return FirstFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }       
}
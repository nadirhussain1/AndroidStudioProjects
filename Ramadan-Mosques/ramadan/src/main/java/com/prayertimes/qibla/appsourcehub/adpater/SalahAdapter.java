package com.prayertimes.qibla.appsourcehub.adpater;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.*;
import android.widget.TextView;
import com.prayertimes.qibla.appsourcehub.fivepillars.SalahFragment;

public class SalahAdapter extends FragmentPagerAdapter
{

    final int PAGE_COUNT = 8;
    Bundle bundle;
    Context context;
    SalahFragment fragment;
    private String tabtitles[] = {
        "Tab1", "Tab2", "Tab3"
    };
    TextView tv;

    public SalahAdapter(FragmentManager fragmentmanager)
    {
        super(fragmentmanager);
    }

    public int getCount()
    {
        return 8;
    }

    public Fragment getItem(int i)
    {
        switch(i)
        {
        default:
            return null;

        case 0: // '\0'
            fragment = new SalahFragment();
            bundle = new Bundle();
            bundle.putString("val", "0");
            fragment.setArguments(bundle);
            return fragment;

        case 1: // '\001'
            fragment = new SalahFragment();
            bundle = new Bundle();
            bundle.putString("val", "1");
            fragment.setArguments(bundle);
            return fragment;

        case 2: // '\002'
            fragment = new SalahFragment();
            bundle = new Bundle();
            bundle.putString("val", "2");
            fragment.setArguments(bundle);
            return fragment;

        case 3: // '\003'
            fragment = new SalahFragment();
            bundle = new Bundle();
            bundle.putString("val", "3");
            fragment.setArguments(bundle);
            return fragment;

        case 4: // '\004'
            fragment = new SalahFragment();
            bundle = new Bundle();
            bundle.putString("val", "4");
            fragment.setArguments(bundle);
            return fragment;

        case 5: // '\005'
            fragment = new SalahFragment();
            bundle = new Bundle();
            bundle.putString("val", "5");
            fragment.setArguments(bundle);
            return fragment;

        case 6: // '\006'
            fragment = new SalahFragment();
            bundle = new Bundle();
            bundle.putString("val", "6");
            fragment.setArguments(bundle);
            return fragment;

        case 7: // '\007'
            fragment = new SalahFragment();
            break;
        }
        bundle = new Bundle();
        bundle.putString("val", "7");
        fragment.setArguments(bundle);
        return fragment;
    }

    public CharSequence getPageTitle(int i)
    {
        return tabtitles[i];
    }
}

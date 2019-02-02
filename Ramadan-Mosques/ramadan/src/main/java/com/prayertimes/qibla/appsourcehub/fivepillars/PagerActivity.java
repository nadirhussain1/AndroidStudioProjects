package com.prayertimes.qibla.appsourcehub.fivepillars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.prayertimes.qibla.appsourcehub.adpater.*;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import muslim.prayers.time.R;
public class PagerActivity extends Utils
{

    int i;

    public PagerActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        int j;
        String s;
        ViewPager viewpager;
        super.onCreate(bundle);
        setContentView(R.layout.activity_pager);
        Bundle bundle1 = getIntent().getExtras();
        j = bundle1.getInt("ga", 0);
        s = bundle1.getString("type");
        Actionbar(s);
        banner_ad();
        typeface();
        Analytics(s);
        viewpager = (ViewPager)findViewById(R.id.pager);
        if(s.equals("Salah"))
        	viewpager.setAdapter(new SalahAdapter(getSupportFragmentManager()));
        else if(s.equals("Fasting"))
            viewpager.setAdapter(new FastingAdapter(getSupportFragmentManager()));
        else if(s.equals("Shahadah"))
            viewpager.setAdapter(new ShahadahAdapter(getSupportFragmentManager()));
        else if(s.equals("Zakat"))
            viewpager.setAdapter(new ZakatAdapter(getSupportFragmentManager()));
        else if(s.equals("Haji"))
            viewpager.setAdapter(new HajiAdapter(getSupportFragmentManager()));
        viewpager.setCurrentItem(j);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        menu.findItem(R.id.menu_ok).setVisible(false);
        return true;
    }
}

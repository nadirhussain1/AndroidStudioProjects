package com.prayertimes.qibla.appsourcehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.prayertimes.qibla.appsourcehub.utils.Utils;

import muslim.prayers.time.R;

public class ActivitySettings extends Utils {

    Handler handle;
    boolean menu_show;
    Runnable r;
    Toolbar toolbar;


    public void onBackPressed() {
        if (!menu_show) {
            finish();
        }
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.preference_activity_custom);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Actionbar(getString(R.string.settings));
        typeface();
        Analytics(getString(R.string.settings));
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            menu_show = intent.getBooleanExtra("menu_show", false);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        MenuItem menuitem = menu.findItem(R.id.menu_ok);
        if (!menu_show) {
            menuitem.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
        if (menuitem.getItemId() == R.id.menu_ok) {
            finish();
        }
        return super.onOptionsItemSelected(menuitem);
    }


}

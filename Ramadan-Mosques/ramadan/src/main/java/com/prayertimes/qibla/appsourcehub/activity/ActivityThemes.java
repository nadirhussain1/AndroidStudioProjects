package com.prayertimes.qibla.appsourcehub.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.prayertimes.qibla.appsourcehub.adpater.AdapterCompass;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import muslim.prayers.time.R;
public class ActivityThemes extends Utils
{

    AdapterCompass compassadapter;
    ListView compasslist;

    public ActivityThemes()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_compass_themes);
        Actionbar(getString(R.string.menu_compass_themes));
        Analytics(getString(R.string.menu_compass_themes));
        compasslist = (ListView)findViewById(R.id.compasslistview);
        compasslist.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                LogUtils.i((new StringBuilder("Compass Position")).append(i).toString());
                SavePrefInt("user_compass", i);
                Toast.makeText(ActivityThemes.this, getString(R.string.toast_compasschanged), 0).show();
                finish();
            }
        });
        compassadapter = new AdapterCompass(this);
        compasslist.setAdapter(compassadapter);
    }
}

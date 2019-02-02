package com.prayertimes.qibla.appsourcehub.receiver;

import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.prayertimes.qibla.appsourcehub.activity.ActivityAlarm;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;

public class AlarmReceiver extends BroadcastReceiver
{

    String time;
    String type;

    public AlarmReceiver()
    {
    }

    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle bundle = intent.getExtras();
        try
        {
            LogUtils.i((new StringBuilder(String.valueOf(bundle.getString("type")))).append(" REceiver ").append(bundle.getString("time")).append(" noti ").append(sharedpreferences.getString("notification", "0")).toString());
            if(bundle.getString("type") != null || bundle.getString("time") != null)
            {
                type = bundle.getString("type");
                time = bundle.getString("time");
                Intent intent1 = new Intent(context, com.prayertimes.qibla.appsourcehub.activity.ActivityAlarm.class);
                intent1.setFlags(0x10000000);
                intent1.putExtra("type", type);
                intent1.putExtra("time", time);
                context.startActivity(intent1);
            }
            return;
        }
        catch(NullPointerException nullpointerexception)
        {
            return;
        }
    }
}

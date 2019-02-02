package com.prayertimes.qibla.appsourcehub.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.prayertimes.qibla.appsourcehub.activity.PrayerTimesActivity;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import muslim.prayers.time.R;
public class NotificationReceiver extends BroadcastReceiver
{

    int id;
    String time;
    String type;

    public NotificationReceiver()
    {
    }

    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle bundle = intent.getExtras();
        try
        {
            LogUtils.i((new StringBuilder(String.valueOf(bundle.getString("type")))).append(" REceiver ").append(bundle.getString("time")).append(" noti ").append(sharedpreferences.getString("notification", "1")).toString());
            if(bundle.getString("type") != null && bundle.getString("time") != null)
            {
                type = bundle.getString("type");
                time = bundle.getString("time");
                id = bundle.getInt("id");
                if(sharedpreferences.getString("notification", "0").equals("0"))
                {
                    android.support.v4.app.NotificationCompat.Builder builder = (new android.support.v4.app.NotificationCompat.Builder(context)).setSmallIcon(R.drawable.ic_launcher).setContentTitle(context.getString(R.string.app_name)).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).setContentText((new StringBuilder(String.valueOf(type))).append(" ").append(time).toString());
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, com.prayertimes.qibla.appsourcehub.activity.PrayerTimesActivity.class), 0x48000000));
                    ((NotificationManager)context.getSystemService("notification")).notify(id, builder.build());
                }
            }
            return;
        }
        catch(NullPointerException nullpointerexception)
        {
            return;
        }
    }
}

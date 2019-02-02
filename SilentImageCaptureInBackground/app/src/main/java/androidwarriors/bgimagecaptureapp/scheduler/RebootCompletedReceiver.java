package androidwarriors.bgimagecaptureapp.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidwarriors.bgimagecaptureapp.capture.ImageCaptureService;
import androidwarriors.bgimagecaptureapp.configurations.ConfigActivity;
import androidwarriors.bgimagecaptureapp.configurations.ConfigPreferences;

/**
 * Created by nadirhussain on 20/01/2019.
 */

public class RebootCompletedReceiver extends BroadcastReceiver {
    public static float selectedIntervalMultiplier = 0.0f;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DebugReboot","Inside RebootCompletedReceiver");
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            scheduleAlarm(context);
        }
    }

    private void scheduleAlarm(Context context) {
        Log.d("DebugReboot","Inside scheduleAlarm");
        ImageCaptureService.counter =0;
        calculateInterval(context);

        Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int timeInterval =(int)(selectedIntervalMultiplier*60*1000);
        Log.d("DebugReboot","Time Interval="+timeInterval);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,timeInterval , pIntent);
    }

    private void calculateInterval(Context context){
        int index= ConfigPreferences.getInstance(context).getIntervalIndex();
        selectedIntervalMultiplier= ConfigActivity.intervalFactorList[index];
    }
}

package androidwarriors.bgimagecaptureapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidwarriors.bgimagecaptureapp.capture.ImageCaptureService;
import androidwarriors.bgimagecaptureapp.configurations.ConfigActivity;
import androidwarriors.bgimagecaptureapp.configurations.ConfigPreferences;
import androidwarriors.bgimagecaptureapp.scheduler.AlarmReceiver;

// Flow of the code is as follow.
// Step1: MainActivity, ScheduleAlarm on startService Click
//Step2: Alarmreceiver, on specific periodic time intervals.
//Step3: ImageCaptureService captures Image
//Step4: ImageUploader called to upload image

public class MainActivity extends AppCompatActivity {
     public static float selectedIntervalMultiplier = 0.0f;

    // This is the first method in the activity that will be invoked.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewsClicks();
    }

    // This method binds start, stop and configuration buttons with their listeners.
    private void initViewsClicks() {
        Button startServiceButton = (Button) findViewById(R.id.startButton);
        Button stopServiceButton = (Button) findViewById(R.id.stopButton);
        Button configButton = (Button) findViewById(R.id.configButton);

        startServiceButton.setOnClickListener(startServiceButtonClickListener);
        stopServiceButton.setOnClickListener(stopServiceButtonClickListener);
        configButton.setOnClickListener(configClickListener);
    }
    // This method finds interval multiplication factor based on user configuration settings.
    private void calculateInterval(){
        int index= ConfigPreferences.getInstance(this).getIntervalIndex();
        selectedIntervalMultiplier= ConfigActivity.intervalFactorList[index];
    }
// This method configures AlarmManager which runs on periodic intervals and invokes Alarmreceiver broadcast class.
    private void scheduleAlarm() {
        ImageCaptureService.counter =0;
        calculateInterval();

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        int timeInterval =(int)(selectedIntervalMultiplier*60*1000);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,timeInterval , pIntent);
    }
   // This method is to cancel the Alarm when user clicks on stop service button
    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    private View.OnClickListener startServiceButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            scheduleAlarm();
        }
    };

    private View.OnClickListener stopServiceButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelAlarm();
        }
    };
    private View.OnClickListener configClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
             launchConfigActivity();
        }
    };

    // This launches the configuration screen that contains interval, size and flash settings.
    private void launchConfigActivity(){
        Intent intent = new Intent(MainActivity.this,ConfigActivity.class);
        startActivity(intent);
    }
}

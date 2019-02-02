package com.connectivityapps.flashlight;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.connectivityapps.fragments.SosFragment;
import com.connectivityapps.shared.SosController;
import com.connectivityapps.utils.GlobalUtil;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 05/03/2015.
 */
public class SosFullScreenActivity extends Activity {
    private NotificationManager notificationManager=null;
    public static final int myNotificationId=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        View rootView = getLayoutInflater().inflate(R.layout.sos_full_screen, null);
        ScalingUtility.getInstance(this).scaleView(rootView);
        setContentView(rootView);

        initViewAndClicks();
    }

    private void initViewAndClicks() {
        findViewById(R.id.handTouchImageView).setOnClickListener(handTouchClickListener);
        findViewById(R.id.ledOnScreenIconView).setOnClickListener(ledOffClickListener);
    }

    @Override
    public void onBackPressed() {
        SosFragment.changeScreenSettings();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MainActivity.myNotificationId);

        SosController.getInstance(this).changeContext(this);
        SosController.getInstance(this).startSos();
    }

    @Override
    protected void onPause() {
        String message="Tap to turn off sos light";
        String title="Sos light is on";
        showNotification(message,title);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        notificationManager.cancel(myNotificationId);
        GlobalUtil.isSosFullScreenShowing=false;
        super.onDestroy();
    }
    private View.OnClickListener handTouchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SosFragment.changeScreenSettings();
            SosFullScreenActivity.this.finish();
        }
    };
    private View.OnClickListener ledOffClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         SosFragment.changeLedSettings();
        }
    };

    private void showNotification(String message,String title){
        PendingIntent notificationIntent = preparePendingIntent();
        Notification notification = createBasicNotification(title,message,notificationIntent);
        notificationManager.notify(myNotificationId , notification);
    }
    private Notification createBasicNotification(String title,String message,PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        Notification notification = builder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(intent)
                .build();

        return notification;
    }
    private PendingIntent preparePendingIntent() {
        Intent intent = new Intent(getApplicationContext(), KillActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}

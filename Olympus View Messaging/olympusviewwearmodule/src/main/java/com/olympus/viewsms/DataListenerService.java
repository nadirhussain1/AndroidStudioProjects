package com.olympus.viewsms;

import android.app.Notification;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.app.PendingIntent;
import android.support.v4.app.RemoteInput;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;


public class DataListenerService extends WearableListenerService {
	private GoogleApiClient mGoogleApiClient=null;
	public static final String CHAT_HISTORY="HISTORY";
	public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
	private static final String WEAR_PATH_ID="/path/to/olympus";

	@Override
	public void onCreate() {
		super.onCreate();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addApi(Wearable.API)
		.build();
		mGoogleApiClient.connect();
	}
	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		if (!mGoogleApiClient.isConnected()) {
			ConnectionResult connectionResult = mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);
			if (!connectionResult.isSuccess()) {

				return;
			}
		}

		for (DataEvent event : dataEvents) {
			if (event.getType() == DataEvent.TYPE_CHANGED) {
				String path = event.getDataItem().getUri().getPath();
				if (WEAR_PATH_ID.equals(path)) {
                    Log.d("WearDebug","Inside DataListener service");

                    DataMapItem dataMapItem =DataMapItem.fromDataItem(event.getDataItem());
					String title = dataMapItem.getDataMap().getString(MainActivity.SENDER);
					String number=dataMapItem.getDataMap().getString(MainActivity.SENDER_NUMBER);
					String history=dataMapItem.getDataMap().getString(CHAT_HISTORY);
					String content= dataMapItem.getDataMap().getString(MainActivity.BODY);
					int themeId=dataMapItem.getDataMap().getInt(MainActivity.THEME);
					String titleColor=dataMapItem.getDataMap().getString(MainActivity.TITLE_COLOR);
					String contentColor=dataMapItem.getDataMap().getString(MainActivity.CONTENT_COLOR);


					//Custom Activity Shown on First Page when notification dragged up
					Intent notificationIntent =new Intent(this, MainActivity.class);
					notificationIntent.putExtra(MainActivity.SENDER, title);
					notificationIntent.putExtra(MainActivity.BODY, content);
					notificationIntent.putExtra(MainActivity.THEME, themeId);
					notificationIntent.putExtra(MainActivity.TITLE_COLOR, titleColor);
					notificationIntent.putExtra(MainActivity.CONTENT_COLOR, contentColor);

					PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

					Intent secondPageIntent=new Intent(this, SecondPageActivity.class);
                    SecondPageActivity.content=history;
                    SecondPageActivity.themeId=themeId;
//					secondPageIntent.putExtra(MainActivity.THEME, themeId);
//					secondPageIntent.putExtra(CHAT_HISTORY, history);

                    Log.d("WearDebug","DataListenerService ThemeId="+themeId);
                    Log.d("WearDebug","history="+history);

                    Notification secondPageNotification = new NotificationCompat.Builder(this).extend(new WearableExtender()
					.setDisplayIntent(PendingIntent.getActivity(this, 0,secondPageIntent, 0))
					.setCustomSizePreset(WearableExtender.SIZE_FULL_SCREEN)).build();

					//Reply Action Building
					Intent replyIntent = new Intent(this,WearReplyActivity.class);
					replyIntent.putExtra(MainActivity.SENDER_NUMBER, number);
					PendingIntent replyPendingIntent =PendingIntent.getActivity(this, 0, replyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

					// Create the reply action and add the remote input
					RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
					.setLabel(getString(R.string.speak_label))
					.build();

					NotificationCompat.Action action = new NotificationCompat.Action.Builder(0,getString(R.string.reply_label), replyPendingIntent)
					.addRemoteInput(remoteInput)    
					.build();

					// Main Notification that will contains all pages and actions

                    NotificationCompat.Builder mainPageNotificationBuilder =new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.app_icon)
					.setContentTitle(title);

					Notification notification = mainPageNotificationBuilder
							.extend(new NotificationCompat.WearableExtender()
							.addPage(secondPageNotification)
							.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.main_background))
							.setDisplayIntent(notificationPendingIntent)
							.setContentIcon(R.drawable.app_icon)
							.setCustomSizePreset(WearableExtender.SIZE_FULL_SCREEN)
							.addAction(action))
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000})
                            .setDefaults(Notification.DEFAULT_ALL)
                            .build();

					// Send notification to number
					int length=number.length();
					String id=number.substring(length-5);
					int notificationId=Integer.parseInt(id);
					NotificationManagerCompat notificationManager =NotificationManagerCompat.from(this);
                    notificationManager.cancel(notificationId);
					notificationManager.notify(notificationId, notification);
				}
			}
		}
	}
}



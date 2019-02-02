package com.olympus.viewsms;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.telephony.SmsManager;
import android.util.Log;

public class WearReplyActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String sendToNumber=getIntent().getExtras().getString("SEND_TO_NUMBER");
		CharSequence messageText= getMessageText(getIntent());
		sendSmsReply(sendToNumber,messageText);
		finish();
	}
	
	private CharSequence getMessageText(Intent intent) {
	    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
	    if (remoteInput != null) {
	        return remoteInput.getCharSequence(DataListenerService.EXTRA_VOICE_REPLY);
	    }
	    return "";
	}
	private void sendSmsReply(String number,CharSequence message){
		try {
			int length=number.length();
			int notificationId=Integer.valueOf(number.substring(length-5));
			NotificationManagerCompat notificationManager =NotificationManagerCompat.from(this);
			notificationManager.cancel(notificationId);
			
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(number, null, message.toString(), null, null);

			ContentValues values = new ContentValues();
			values.put("address",number);
			values.put("date", ""+System.currentTimeMillis()); 
			values.put("read", 1); 
			values.put("type", 2); 
			values.put("seen", 1); 
			values.put("body",message.toString());

			Uri uri = Uri.parse("content://sms/");
			getContentResolver().insert(uri,values);
			
		} catch (Exception e) {
			Log.d("Exception",e.toString());
			e.printStackTrace();
		}
	}
	

}

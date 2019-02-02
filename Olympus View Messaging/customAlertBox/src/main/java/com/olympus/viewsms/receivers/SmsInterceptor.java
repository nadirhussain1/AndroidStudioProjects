package com.olympus.viewsms.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.olympus.viewsms.data.ScreenDecisionMaker;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.util.Utils;

public class SmsInterceptor extends BroadcastReceiver {
	Context mContext=null;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext=context;

		String senderNumber ="";	
		String smsBody = "";
		String senderName="";
		long timestamp = 0L;


		try{
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] msgs = new SmsMessage[pdus.length];
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				senderNumber += msgs[i].getOriginatingAddress();
				smsBody += msgs[i].getMessageBody().toString();
				timestamp += msgs[i].getTimestampMillis();
			}
			//Log.i("==================", "is sms message");
		}catch(Exception e){
			Log.i("==================", "isn't sms message");
			return;
		}

		senderName=Utils.retrieveContactName(senderNumber,mContext);
		String thumbnailUri=Utils.fetchThumbnailUri(senderNumber,mContext);

		SmsData sms = new SmsData(senderNumber,senderName,smsBody,timestamp,thumbnailUri);
		new ScreenDecisionMaker().displaySmsScreen(context, sms);

        ContentValues values = new ContentValues();
        values.put("address", sms.getSenderNumber());
        values.put("date", "" + System.currentTimeMillis());
        values.put("read", 1);
        values.put("type", 1);
        values.put("seen", 1);
        values.put("body", smsBody);

        Uri uri = Uri.parse("content://sms/");

        mContext.getContentResolver().insert(uri, values);
        abortBroadcast();
		
	}



}

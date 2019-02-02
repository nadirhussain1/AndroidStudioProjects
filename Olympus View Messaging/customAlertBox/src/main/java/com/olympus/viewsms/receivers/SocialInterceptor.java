package com.olympus.viewsms.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.olympus.viewsms.data.ScreenDecisionMaker;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.util.Constants;

public class SocialInterceptor extends BroadcastReceiver{
	public static String ACTION="com.olympus.action.Socal_APPS";
	public static String SOURCE_PACKAGE_KEY="source_package_key";
	public static String MESSAGE_BODY_KEY="notification_message_key";
	public static String MESSAGE_SENDER_KEY="message_sender_key";
	public static String MESSAGE_TIME_KEY="message_time_key";

	public static String FACEBOOK_APP_PACKAGE="com.facebook.orca";
	public static String WHATS_APP_PACKAGE="com.whatsapp";
	public static String VIBER_APP_PACKAGE="com.viber.voip";
	public static String TWITTER_APP_PACKAGE="com.twitter.android";


	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getExtras() !=null){
			String sourcePackage=intent.getExtras().getString(SOURCE_PACKAGE_KEY);
			String messageBody=intent.getExtras().getString(MESSAGE_BODY_KEY);;
			String senderName=intent.getExtras().getString(MESSAGE_SENDER_KEY);;
			long time=intent.getExtras().getLong(MESSAGE_TIME_KEY);
			String thumbnailUri=null;
			
			if(messageBody.isEmpty()){
				messageBody="You've Got A Message.";
			}
			boolean shouldDisplay=false;
			if(sourcePackage.contentEquals(FACEBOOK_APP_PACKAGE)){
                thumbnailUri="0";
				shouldDisplay=true;
				
			}else if(sourcePackage.contentEquals(WHATS_APP_PACKAGE)){
				shouldDisplay=true;
                thumbnailUri="1";
			}else if(sourcePackage.contentEquals(VIBER_APP_PACKAGE)){
				shouldDisplay=true;
                thumbnailUri="2";
			}else if(sourcePackage.contentEquals(TWITTER_APP_PACKAGE)){
				shouldDisplay=true;
                thumbnailUri="3";
			}

			if(shouldDisplay){
				SmsData sms=new SmsData("", senderName, messageBody, time, thumbnailUri);
				sms.category=Constants.SOCIAL_SMS_CAT;
				sms.packageName=sourcePackage;
				new ScreenDecisionMaker().displaySmsScreen(context, sms);
			}

		}

	}

}

package com.olympus.viewsms.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.olympus.viewsms.receivers.SocialInterceptor;
import com.olympus.viewsms.util.Utils;

public class OlympusAccessibilityService extends AccessibilityService{
	private static final String TAG = "OlympusAccessibilityService";
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {


		final int eventType = event.getEventType(); 
		if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			String sourcePackageName = ""+event.getPackageName();
			Parcelable parcelable = event.getParcelableData();

			if (parcelable instanceof Notification) {
				if(sourcePackageName.contentEquals(SocialInterceptor.FACEBOOK_APP_PACKAGE) ||
						sourcePackageName.contentEquals(SocialInterceptor.WHATS_APP_PACKAGE) ||
						sourcePackageName.contentEquals(SocialInterceptor.VIBER_APP_PACKAGE) || 
						sourcePackageName.contentEquals(SocialInterceptor.TWITTER_APP_PACKAGE) ){


					String eventString=event.toString();
					Utils.saveLogsToSdCard("crash.txt", eventString);
					int contentTitleIndex=eventString.indexOf("contentTitle=");
					int contentTextIndex=eventString.indexOf("contentText=");
					int tickerTextIndex=eventString.indexOf("tickerText=");

					String senderName="";
					String messageBody="";

					if(contentTitleIndex!=-1 && contentTextIndex!=-1 && tickerTextIndex!=-1){
						senderName=eventString.substring(contentTitleIndex+13,contentTextIndex);
						messageBody=eventString.substring(contentTextIndex+12, tickerTextIndex);
					}else{
						int textIndex=eventString.indexOf("Text:");
						int descriptionIndex=eventString.indexOf("ContentDescription");

						if(textIndex !=-1 && descriptionIndex!=-1){
							String text=eventString.substring(textIndex+7, descriptionIndex-3);
							int indexDelimiter=text.indexOf(":");

							if(indexDelimiter==-1){
								senderName=text;	
							}else{
								senderName=text.substring(0, indexDelimiter);
								messageBody=text.substring(indexDelimiter+1);
							}
						}
					}
					if(!senderName.isEmpty() && !senderName.contentEquals("N ")){


						Intent mIntent = new Intent(SocialInterceptor.ACTION);
						mIntent.putExtra(SocialInterceptor.SOURCE_PACKAGE_KEY, sourcePackageName);
						mIntent.putExtra(SocialInterceptor.MESSAGE_SENDER_KEY, senderName);
						mIntent.putExtra(SocialInterceptor.MESSAGE_BODY_KEY, messageBody);
						mIntent.putExtra(SocialInterceptor.MESSAGE_TIME_KEY, event.getEventTime());

						getApplicationContext().sendBroadcast(mIntent);
					}
				}
			}
		}
	}


	@Override
	public void onInterrupt() {


	}
	public static boolean isAccessibilitySettingsOn(Context mContext) {
		int accessibilityEnabled = 0;
		final String service = "com.olympus.viewsms/com.olympus.viewsms.services.OlympusAccessibilityService";

		boolean accessibilityFound = false;
		try {
			accessibilityEnabled = Settings.Secure.getInt(
					mContext.getApplicationContext().getContentResolver(),
					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
			Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
		} catch (SettingNotFoundException e) {
			Log.e(TAG, "Error finding setting, default accessibility to not found: "
					+ e.getMessage());
		}
		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

		if (accessibilityEnabled == 1) {
			Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
			String settingValue = Settings.Secure.getString(
					mContext.getApplicationContext().getContentResolver(),
					Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
			if (settingValue != null) {
				TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
				splitter.setString(settingValue);
				while (splitter.hasNext()) {
					String accessabilityService = splitter.next();

					Log.v(TAG, "-------------- > accessabilityService :: " + accessabilityService);
					if (accessabilityService.equalsIgnoreCase(service)) {
						Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
						return true;
					}
				}
			}
		} else {
			Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
		}

		return accessibilityFound;      
	}

}

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.olympus.viewsms.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.olympus.viewsms.R;
import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.util.Utils;

public class Widget extends AppWidgetProvider {
	public static final  String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";
	public static String YOUR_AWESOME_ACTION = "YourAwesomeAction";
	public static String  WIDGET_ID="widgetId";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra(WIDGET_IDS_KEY)) {
			Log.d("OnReceive","OnReceive  Widget Id");
			int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
			this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
		} else if(intent.getAction().equals(YOUR_AWESOME_ACTION)){
			Log.d("OnReceive","OnReceive Widget AweSome");
			Utils.showSMSDialog(context);
		} else{ 
			Log.d("OnReceive","OnReceive Widget Else");
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

		int unread=0;
		unread=SharedData.getInstance().getSizeAll();  //get unread sms received

		//data will contain some predetermined data, but it may be null
		for (int widgetId : appWidgetIds) {
			remoteViews.setTextViewText(R.id.tv_unread, unread+"");

			Intent intent = new Intent(context, Widget.class);
			intent.setAction(YOUR_AWESOME_ACTION);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

//			Intent intent = new Intent(context, Widget.class);
//			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
//			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//			remoteViews.setOnClickPendingIntent(R.id.img_unread, pendingIntent);

			manager.updateAppWidget(widgetId, remoteViews);
		}
	}
}

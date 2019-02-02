package com.olympus.viewsms.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.olympus.viewsms.R;
import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.model.SharedDialog;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;

public class WidgetStack extends AppWidgetProvider {
    public static final String WIDGET_IDS_KEY = "mywidgetproviderwidgetids";
    public static final String EXTRA_LIST_VIEW_ROW_NUMBER = "mypackage.EXTRA_LIST_VIEW_ROW_NUMBER";
    public static final String LIST_VIEW_OK_ROW_NUMBER = "mypackage.LIST_VIEW_OK_ROW_NUMBER";
    public static int randomNumber = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.hasExtra(Widget.WIDGET_ID)) {
            if (intent.hasExtra(WIDGET_IDS_KEY)) {
                int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
                loadRecentMessage(context);
                this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
            } else if (intent.getAction().equals(EXTRA_LIST_VIEW_ROW_NUMBER)) {
                int viewIndex = intent.getIntExtra(EXTRA_LIST_VIEW_ROW_NUMBER, 0);
                if (viewIndex >= SharedData.getInstance().stackWidgetMessages.size()) {
                    loadRecentMessage(context);
                }
                SmsData sms_new = SharedData.getInstance().stackWidgetMessages.get(viewIndex);
                Intent replyIntent = new Intent(context, SharedDialog.getInstance().dlg_reply.getClass());
                replyIntent.putExtra("avatar", sms_new.getThumbnail());
                sms_new.setThumbnail(null);  //clear pic
                replyIntent.putExtra("SmsData", sms_new);
                replyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(replyIntent);
            } else {
                super.onReceive(context, intent);
                loadRecentMessage(context);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            }
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        randomNumber++;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_widget);
        remoteViews.setViewVisibility(R.id.loadingTextView, View.GONE);

        Intent svcIntent = new Intent(context, StackWidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));
        remoteViews.setRemoteAdapter(appWidgetId, R.id.stackWidgetView, svcIntent);

        Intent toastIntent = new Intent(context, WidgetStack.class);
        toastIntent.setAction(WidgetStack.EXTRA_LIST_VIEW_ROW_NUMBER);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        toastIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.stackWidgetView, toastPendingIntent);

        return remoteViews;
    }

    private void loadRecentMessage(Context context) {
        ArrayList<SmsData> recentList = new ArrayList<SmsData>();
        Uri mSmsInboxQueryUri = Uri.parse("content://sms");
        Cursor cursor = context.getContentResolver().query(mSmsInboxQueryUri, new String[]{"address", "date", "body"}, null, null, null);

        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            int size = recentList.size();
            boolean shouldAdd = true;
            if (size >= 10) {
                break;
            } else {
                for (int count = 0; count < size; count++) {
                    String extractedNumber = recentList.get(count).getSenderNumber();
                    if (PhoneNumberUtils.compare(address, extractedNumber)) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (shouldAdd) {
                    SmsData smsData = Utils.getSmsObject(cursor, context);
                    recentList.add(size, smsData);
                }
            }
        }

        SharedData.getInstance().stackWidgetMessages = recentList;
    }
}

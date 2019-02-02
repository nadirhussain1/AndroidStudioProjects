package com.olympus.viewsms.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.olympus.viewsms.R;
import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Utils;

public class ListProvider implements RemoteViewsFactory{

	private Context context = null;

	public ListProvider(Context context, Intent intent) {
		this.context = context;
	}
	@Override
	public void onCreate() {

	}

	@Override
	public void onDataSetChanged() {
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public int getCount() {
		return SharedData.getInstance().stackWidgetMessages.size();
	}

	@Override
	public RemoteViews getViewAt(int position) {
		SmsData sms=SharedData.getInstance().stackWidgetMessages.get(position);
		Theme cur_theme=Utils.getAppliedTheme(context, sms);
		RemoteViews remoteView=new RemoteViews(context.getPackageName(), R.layout.widget_stack_item);

		if(sms!=null){
			if(cur_theme.getId()<22){
				remoteView.setImageViewResource(R.id.img_bg, Utils.getDrawableResourceId(context, "ic"+cur_theme.getId()+"_bg"));
			}else{
				remoteView.setImageViewResource(R.id.img_bg, Utils.getDrawableResourceId(context, "ic"+cur_theme.getId()+"_flashbg"));
			}

			remoteView.setImageViewResource(R.id.img_s2, Utils.getDrawableResourceId(context, "ic"+cur_theme.getId()+"_s2"));
			remoteView.setTextColor(R.id.tv_number, Color.parseColor(cur_theme.getCtitle()));
			remoteView.setTextColor(R.id.tv_content, Color.parseColor(cur_theme.getCtext()));
			remoteView.setTextColor(R.id.tv_reply, Color.parseColor(cur_theme.getCtext()));
			remoteView.setTextViewText(R.id.tv_number, sms.getSenderName().equals("")?sms.getSenderNumber():sms.getSenderName());
			remoteView.setTextViewText(R.id.tv_content, sms.getBody());

			Intent fillInIntent = new Intent();
			fillInIntent.putExtra(WidgetStack.EXTRA_LIST_VIEW_ROW_NUMBER, position);
			remoteView.setOnClickFillInIntent(R.id.tv_reply, fillInIntent);	
		}
		return remoteView;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}

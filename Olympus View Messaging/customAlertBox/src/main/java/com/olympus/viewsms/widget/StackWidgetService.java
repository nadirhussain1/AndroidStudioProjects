package com.olympus.viewsms.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService{

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		int appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - WidgetStack.randomNumber;
		return (new ListProvider(this.getApplicationContext(), intent));
	}

}

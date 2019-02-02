/*
 * Copyright (C) 2012 Alex Kuiper
 * 
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */

package net.nightwhistler.pageturner;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import com.parse.Parse;
import com.parse.ParseObject;

import patagonia.DownloadEvent;
import patagonia.ParseAppBook;
import patagonia.ParseBook;
import patagonia.UserBook;
import patagonia.Utils;

public class PageTurner extends Application {

	private static PageTurner INSTANCE;
	private static Utils UTILS;

	@Override
	public void onCreate() {
		super.onCreate();
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		ParseObject.registerSubclass(ParseBook.class);
		ParseObject.registerSubclass(ParseAppBook.class);
        ParseObject.registerSubclass(UserBook.class);
        ParseObject.registerSubclass(DownloadEvent.class);

		Parse.initialize(new Parse.Configuration.Builder(this)
				.applicationId(getApplicationContext().getResources().getString(R.string.parse_app_id))
				.server(getApplicationContext().getResources().getString(R.string.parse_server_url))
				.enableLocalDataStore()
				.build()
		);

		INSTANCE = this;
		Utils.initialize(this);
	}

	public Boolean isConnected() {
		return getInstance().getUtils().getDeviceUtils().isConnected(this);
	}

	public Utils getUtils(){
		if (UTILS == null) {
			UTILS = Utils.getInstance();
		}
		return UTILS;
	}

	public static PageTurner getInstance(){
		return INSTANCE;
	}
	
	public static void changeLanguageSetting(Context context, Configuration pageTurnerConfig) {
		android.content.res.Configuration config = new android.content.res.Configuration(
				context.getResources().getConfiguration());
	    
		config.locale = pageTurnerConfig.getLocale();
	    context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());	    
	}
}

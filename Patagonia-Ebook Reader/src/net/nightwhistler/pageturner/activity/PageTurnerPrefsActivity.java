/*
 * Copyright (C) 2011 Alex Kuiper
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

package net.nightwhistler.pageturner.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.PageTurner;
import net.nightwhistler.pageturner.R;

public class PageTurnerPrefsActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Configuration config = Configuration.getInstance(this);
		PageTurner.changeLanguageSetting(this, config);
		setTheme( config.getTheme() );
		
		super.onCreate(savedInstanceState);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		if ( ! settings.contains("device_name") ) {
	 	   SharedPreferences.Editor editor = settings.edit();
	 	   editor.putString("device_name", Build.MODEL );
	 	   // Commit the edits!
	 	   editor.commit();			
		}
		
		addPreferencesFromResource(R.xml.pageturner_prefs);

		final PreferenceScreen screen = getPreferenceScreen();

        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {

            Preference uiPref = screen.findPreference(Configuration.KEY_DIM_SYSTEM_UI);
            PreferenceGroup group = (PreferenceGroup) screen.findPreference("visual_prefs");

            group.removePreference(uiPref);
        }
	}
}

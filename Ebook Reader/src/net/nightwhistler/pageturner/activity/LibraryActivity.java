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
package net.nightwhistler.pageturner.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.fragment.PatagoniaLibraryFragment;

import patagonia.PatagoniaFbLibraryFragment;

public class LibraryActivity extends AppCompatActivity {

    private final String LOG_TAG = LibraryActivity.class.getSimpleName();
    // private PatagoniaLibraryFragment libraryFragment;
    private PatagoniaFbLibraryFragment libraryFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_library);
        loadFirebaseFragment();

    }

    //    private void loadParseFragment() {
//        libraryFragment = new PatagoniaLibraryFragment();
//        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.container, libraryFragment).commit();
//    }
    private void loadFirebaseFragment() {
        libraryFragment = new PatagoniaFbLibraryFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, libraryFragment).commit();
    }


    @Override
    public void onBackPressed() {
        libraryFragment.onBackPressed();
    }


    public boolean onSearchRequested() {
        return true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PatagoniaLibraryFragment.PATAGONIA_AUTORS_BOOK_REQUEST_CODE) {
            if (libraryFragment != null && data != null) {
                // libraryFragment.selectBookFromAutores(data.getStringExtra(PatagoniaAutorFragment.BOOK_SELECTED));
            }
        } else if (resultCode == RESULT_OK) {
            invalidateOptionsMenu();
            libraryFragment.reloadBooks();
        }
    }

    public void startPreferences() {
        Intent intent = new Intent(this, PageTurnerPrefsActivity.class);
        startActivity(intent);
    }

}
